package com.redhat.ecosystemappeng.trustedcontent.model.vex;

import java.util.List;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class ProductTree {
    public List<Branch> branches;
    public List<Relationship> relationships;
}
