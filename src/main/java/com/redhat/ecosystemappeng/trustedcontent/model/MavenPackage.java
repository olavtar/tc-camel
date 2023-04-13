package com.redhat.ecosystemappeng.trustedcontent.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record MavenPackage (String groupId, String artifactId, String version) {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String groupId;
        private String artifactId;
        private String version;

        private Builder(){}

        public Builder groupId(String groupId) {
            this.groupId = groupId;
            return this;
        }

        public Builder artifactId(String artifactId) {
            this.artifactId = artifactId;
            return this;
        }

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public MavenPackage build() {
            return new MavenPackage(groupId, artifactId, version);
        }
    }
}
