package tc.cve;

// import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
// import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
Root root = om.readValue(myJsonString, Root.class); */
import java.util.ArrayList;

public class Root {
    public Document document;
    public ProductTree product_tree;
    public ArrayList<Vulnerability> vulnerabilities = new ArrayList<Vulnerability>();

}
