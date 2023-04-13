package com.redhat.ecosystemappeng.trustedcontent.model.vex;

import java.util.Date;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class RevisionHistory {
    public Date date;
    public String number;
    public String summary;
}
