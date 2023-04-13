package com.redhat.ecosystemappeng.trustedcontent.model.vex;

import java.util.ArrayList;
import java.util.List;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Root {
    public Document document;
    public ProductTree product_tree;
    public List<Vulnerability> vulnerabilities = new ArrayList<>();

}
