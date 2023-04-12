package tc;

import tc.MavenPackage;

public class RHContent {
    public MavenPackage mavenPackage = new MavenPackage();
    public String productStatus;

    public RHContent() {
    }

    public MavenPackage getMavenPackage() {
        return mavenPackage;
    }

    public void setMavenPackage(MavenPackage mavenPackage) {
        this.mavenPackage = mavenPackage;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }

    @Override
    public String toString() {
        return "RHContent{" + "mavenPackage=" + mavenPackage + ", productStatus='" + productStatus + '\'' + '}';
    }
}
