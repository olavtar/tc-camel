package tc.cve;

import java.util.ArrayList;
import java.util.Date;
import tc.cve.Generator;
import tc.cve.RevisionHistory;

public class Tracking {
    public Date current_release_date;
    public Generator generator;
    public String id;
    public Date initial_release_date;
    public ArrayList<RevisionHistory> revision_history;
    public String status;
    public String version;
}
