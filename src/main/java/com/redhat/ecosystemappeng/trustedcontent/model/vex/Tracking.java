package com.redhat.ecosystemappeng.trustedcontent.model.vex;

import java.util.ArrayList;
import java.util.Date;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Tracking {
    public Date current_release_date;
    public Generator generator;
    public String id;
    public Date initial_release_date;
    public ArrayList<RevisionHistory> revision_history;
    public String status;
    public String version;
}
