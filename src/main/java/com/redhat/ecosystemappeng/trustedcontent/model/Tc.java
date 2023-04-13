package com.redhat.ecosystemappeng.trustedcontent.model;

import java.util.List;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Tc {

    public List<String> cves;

    public Tc() {
    }

    public List<String> getCves() {
        return cves;
    }

    public void setCves(List<String> cves) {
        this.cves = cves;
    }
}
