package com.redhat.ecosystemappeng.trustedcontent.model.vex;

import java.util.List;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Score {
    public CvssV3 cvss_v3;
    public List<String> products;
}
