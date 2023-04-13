package com.redhat.ecosystemappeng.trustedcontent.model.vex;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class CvssV3 {
    public String attackComplexity;
    public String attackVector;
    public String availabilityImpact;
    public double baseScore;
    public String baseSeverity;
    public String confidentialityImpact;
    public String integrityImpact;
    public String privilegesRequired;
    public String scope;
    public String userInteraction;
    public String vectorString;
    public String version;
}
