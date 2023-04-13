package com.redhat.ecosystemappeng.trustedcontent.model.vex;

import java.util.List;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class ProductStatus {
    public List<String> fixed;
    public List<String> known_affected;
    public List<String> known_not_affected;

    @Override
    public String toString() {
        return "ProductStatus{" + "fixed=" + fixed + ", known_affected=" + known_affected + ", known_not_affected="
                + known_not_affected + '}';
    }

}
