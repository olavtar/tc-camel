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

import java.util.*;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.support.DefaultMessage;

/**
 * Camel route definitions.
 */
public class Routes extends RouteBuilder {

    private final Set<Package> packages = Collections.synchronizedSet(new LinkedHashSet<>());

    public Routes() {

        /* Let's add some initial packages */
        this.packages.add(new Package("org.apache.flex.blazeds", "blazeds", "4.7.2"));
        this.packages.add(new Package("org.apache.maven", "maven-plugin-ap", "4.0.0-alpha-4"));

    }

    @Override
    public void configure() throws Exception {
        // from("platform-http:/hello").setBody(constant("Hello from Camel Quarkus!"));

        restConfiguration().bindingMode(RestBindingMode.json);
        rest("/packages").get().to("direct:getPackages")

                .post().type(Package[].class).to("direct:addPackage");

        from("direct:getPackages").setBody().constant(packages);

        // from("direct:addPackage").process().body(Package.class, packages::add).setBody().constant(packages);
        from("direct:addPackage").process(this::addRHRecommendation);
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

}
