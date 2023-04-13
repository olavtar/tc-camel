package com.redhat.ecosystemappeng.trustedcontent.routes;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.ecosystemappeng.trustedcontent.model.MavenPackage;
import com.redhat.ecosystemappeng.trustedcontent.model.RHContent;
import com.redhat.ecosystemappeng.trustedcontent.model.Tc;
import com.redhat.ecosystemappeng.trustedcontent.model.vex.Branch;
import com.redhat.ecosystemappeng.trustedcontent.model.vex.ProductStatus;
import com.redhat.ecosystemappeng.trustedcontent.model.vex.Relationship;
import com.redhat.ecosystemappeng.trustedcontent.model.vex.Root;
import com.redhat.ecosystemappeng.trustedcontent.model.vex.Vulnerability;

import io.quarkus.runtime.annotations.RegisterForReflection;

@ApplicationScoped
@RegisterForReflection
public class TCModules {

    private static final Logger LOGGER = LoggerFactory.getLogger(Routes.class);

    private Map<String, Root> vexes = new HashMap<>();
    private final ObjectMapper mapper = new ObjectMapper();

    @ConfigProperty(name = "vex.path", defaultValue = "vex")
    String vexPath;

    @PostConstruct
    public void loadVexFiles() {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(vexPath), "CVE*.json")) {
            stream.forEach(p -> {
                try {
                    Root vex = mapper.readValue(p.toFile(), Root.class);
                    LOGGER.debug("Added new VEX file: {}", p.toFile());
                    vexes.put(vex.document.tracking.id, vex);
                } catch (IOException e) {
                    LOGGER.error("Unable to parse VEX file" + p, e);
                }
            });
        } catch (IOException e) {
            LOGGER.error("Unable to open directory cve", e);
        }
    }

    public Set<String> listCVEs() {
        return vexes.keySet();
    }

    public Root getVEX(@Header("vexId") String vexId) {
        return vexes.get(vexId);
    }

    public List<RHContent> getContent(@Body Tc tc) {
        List<RHContent> tcModules = new ArrayList<>();

        for (String cve : tc.getCves()) {
            Root root = vexes.get(cve);
            if (root == null) {
                tcModules.add(null);
            } else {
                RHContent.Builder rhContent = RHContent.builder();
                for (Vulnerability vulnerability : root.vulnerabilities) {
                    ProductStatus prodStatus = vulnerability.product_status;
                    if (prodStatus.fixed != null) {
                        rhContent.productStatus("fixed");
                    } else if (prodStatus.known_not_affected != null) {
                        rhContent.productStatus("known_not_effected");
                    } else if (prodStatus.known_affected != null) {
                        rhContent.productStatus("known_effected");
                    }
                }
                // get product_reference so we can get the purl from the branch based on it
                String productReference = null;
                for (Relationship relationship : root.product_tree.relationships) {
                    productReference = relationship.product_reference;
                }

                List<Branch> branches = root.product_tree.branches;
                Branch branch = getBranch(branches, productReference);
                String packageData = branch.product.product_identification_helper.purl;
                String[] pkgData = packageData.split("/|@|\\?");
                MavenPackage.Builder mvpkg = MavenPackage.builder();
                mvpkg.groupId(pkgData[1]).artifactId(pkgData[2]).version(pkgData[3]);

                rhContent.mavenPackage(mvpkg.build());
                tcModules.add(rhContent.build());

            }
        }
        return tcModules;
    }

    private static Branch getBranch(List<Branch> branches, String productId) {
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
