package com.redhat.ecosystemappeng.trustedcontent.model.vex;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Relationship {
    public String category;
    public FullProductName full_product_name;
    public String product_reference;
    public String relates_to_product_reference;
}
