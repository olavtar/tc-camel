package tc;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import tc.cve.*;

public class TestMain {
    public static void main(String[] args) throws Exception {

        // create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        List<String> vexIds = Arrays.asList("CVE-2022-41946", "CVE-2022-45787", "CVE-2023-0044", "CVE-2023-26464");
        ArrayList<RHContent> tcModules = new ArrayList<RHContent>();

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource("cve");
        String path = url.getPath();
        File[] listOfFiles = new File(path).listFiles();

        for (String id : vexIds) {
            boolean found = false;
            for (File f : listOfFiles) {
                if (f.getName().contains(id)) {
                    found = true;
                    System.out.println(f.getName());
                    byte[] fileData = Files.readAllBytes(f.toPath());
                    // convert json string to object
                    Root root = objectMapper.readValue(fileData, Root.class);
                    ArrayList<Vulnerability> vulnerabilities = root.vulnerabilities;
                    RHContent rhContent = new RHContent();
                    for (Vulnerability vulnerability : vulnerabilities) {
                        ProductStatus prodStatus = vulnerability.product_status;
                        if (prodStatus.fixed != null) {
                            System.out.println("Fixed ");
                            rhContent.setProductStatus("fixed");
                        } else if (prodStatus.known_not_affected != null) {
                            System.out.println("known_not_effected ");
                            rhContent.setProductStatus("known_not_effected");
                        } else if (prodStatus.known_affected != null) {
                            System.out.println("known_effected ");
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
                    System.out.println(packageData);
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
        System.out.println(tcModules.size());
        System.out.println(tcModules);
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
