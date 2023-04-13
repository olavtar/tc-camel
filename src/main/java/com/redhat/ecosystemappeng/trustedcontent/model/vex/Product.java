package com.redhat.ecosystemappeng.trustedcontent.model.vex;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Product {
    public String name;
    public String product_id;
    public ProductIdentificationHelper product_identification_helper;
}
