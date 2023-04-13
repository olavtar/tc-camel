package com.redhat.ecosystemappeng.trustedcontent.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record RHContent (MavenPackage mavenPackage, String productStatus) {
   
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private MavenPackage mavenPackage;
        private String productStatus;

        private Builder() {}

        public Builder mavenPackage(MavenPackage mavenPackage) {
            this.mavenPackage = mavenPackage;
            return this;
        }

        public Builder productStatus(String productStatus) {
            this.productStatus = productStatus;
            return this;
        }

        public RHContent build() {
            return new RHContent(mavenPackage, productStatus);
        }
    }
}
