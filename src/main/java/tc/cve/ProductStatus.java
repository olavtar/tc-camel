package tc.cve;

import java.util.ArrayList;

public class ProductStatus {
    public ArrayList<String> fixed;
    public ArrayList<String> known_affected;
    public ArrayList<String> known_not_affected;

    @Override
    public String toString() {
        return "ProductStatus{" + "fixed=" + fixed + ", known_affected=" + known_affected + ", known_not_affected="
                + known_not_affected + '}';
    }

    public ProductStatus() {
    }
}
