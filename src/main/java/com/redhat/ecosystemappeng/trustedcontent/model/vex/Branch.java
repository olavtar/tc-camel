package com.redhat.ecosystemappeng.trustedcontent.model.vex;

import java.util.List;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Branch {
    public List<Branch> branches;
    public String category;
    public String name;
    public Product product;
}
