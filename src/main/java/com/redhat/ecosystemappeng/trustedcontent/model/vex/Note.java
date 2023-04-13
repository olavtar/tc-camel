package com.redhat.ecosystemappeng.trustedcontent.model.vex;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Note {
    public String category;
    public String text;
    public String title;
}
