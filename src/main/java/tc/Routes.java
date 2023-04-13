/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tc;

import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.log;
import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.timer;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.support.DefaultMessage;
import tc.cve.*;

/**
 * Camel route definitions.
 */
public class Routes extends RouteBuilder {

    private final Set<Package> packages = Collections.synchronizedSet(new LinkedHashSet<>());

    private final ArrayList<String> vexIds = new ArrayList<>();

    public Routes() {

        /* Let's add some initial packages */
        this.packages.add(new Package("org.apache.flex.blazeds", "blazeds", "4.7.2"));
        this.packages.add(new Package("org.apache.maven", "maven-plugin-ap", "4.0.0-alpha-4"));

        this.vexIds.add("CVE-2022-41946");
        this.vexIds.add("CVE-2022-45787");
        this.vexIds.add("CVE-2023-0044");
        this.vexIds.add("CVE-2023-26464");

    }

    @Override
    public void configure() throws Exception {
        // restConfiguration().bindingMode(RestBindingMode.json);
        // rest("/packages").get().to("direct:getPackages")
        //
        // .post().type(Package[].class).to("direct:addPackage");
        //
        // from("direct:getPackages").setBody().constant(packages);
        //
        // // from("direct:addPackage").process().body(Package.class, packages::add).setBody().constant(packages);
        // from("direct:addPackage").process(this::addRHRecommendation);

        restConfiguration().bindingMode(RestBindingMode.json);
        rest("/tc").get().to("direct:getTc").post().type(Tc.class).to("direct:addTc");
        from("direct:getTc").setBody().constant(vexIds);
        from("direct:addTc").process(this::getTCModules);
    }

    private void addRHRecommendation(Exchange exchange) {
        Message message = new DefaultMessage(exchange.getContext());
        Package[] pac = exchange.getMessage().getBody(Package[].class);
        List list = new ArrayList();
        for (Package item : pac) {
            String rhPackage = item.getArtifactId() + item.getVersion() + "-redHatVersion";
            list.add(rhPackage);
        }

        message.setBody(list);
        exchange.setMessage(message);
    }

    private void getTCModules(Exchange exchange) throws IOException {
        Message message = new DefaultMessage(exchange.getContext());
        Tc tc = exchange.getMessage().getBody(Tc.class);

        ObjectMapper objectMapper = new ObjectMapper();

        ArrayList<RHContent> tcModules = new ArrayList<>();

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource("cve");
        String path = url.getPath();
        File[] listOfFiles = new File(path).listFiles();

        for (String cve : tc.getCves()) {
            boolean found = false;
            for (File f : listOfFiles) {
                if (f.getName().contains(cve)) {
                    found = true;
                    byte[] fileData = Files.readAllBytes(f.toPath());
                    // convert json string to object
                    Root root = objectMapper.readValue(fileData, Root.class);
                    ArrayList<Vulnerability> vulnerabilities = root.vulnerabilities;
                    RHContent rhContent = new RHContent();
                    for (Vulnerability vulnerability : vulnerabilities) {
                        ProductStatus prodStatus = vulnerability.product_status;
                        if (prodStatus.fixed != null) {
                            rhContent.setProductStatus("fixed");
                        } else if (prodStatus.known_not_affected != null) {
                            rhContent.setProductStatus("known_not_effected");
                        } else if (prodStatus.known_affected != null) {
                            rhContent.setProductStatus("known_effected");
                        }
                    }
                    // get product_reference so we can get the purl from the branch based on it
                    String productReference = null;
                    for (Relationship relationship : root.product_tree.relationships) {
                        productReference = relationship.product_reference;
                    }

                    ArrayList<Branch> branches = root.product_tree.branches;
                    Branch branch = getBranch(branches, productReference);
                    String packageData = branch.product.product_identification_helper.purl;
                    String[] pkgData = packageData.split("/|@|\\?");
                    MavenPackage mvpkg = new MavenPackage();
                    mvpkg.setGroupId(pkgData[1]);
                    mvpkg.setArtifactId(pkgData[2]);
                    mvpkg.setVersion(pkgData[3]);

                    rhContent.setMavenPackage(mvpkg);
                    tcModules.add(rhContent);
                }
            }
            if (!found) {
                tcModules.add(null);
            }
        }
        message.setBody(tcModules);
        exchange.setMessage(message);
    }

    private static Branch getBranch(ArrayList<Branch> branches, String productId) {
        for (Branch branch : branches) {
            if (branch.product != null) {
                if (productId.equals(branch.product.product_id)) {
                    return branch;
                }
            } else {
                Branch subBranch = getBranch(branch.branches, productId);
                if (subBranch != null) {
                    return subBranch;
                }
            }
        }
        return null;
    }

}
