package com.redhat.ecosystemappeng.trustedcontent.model.vex;

import java.util.List;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Threat {
    public String category;
    public String details;
    public List<String> product_ids;
}
