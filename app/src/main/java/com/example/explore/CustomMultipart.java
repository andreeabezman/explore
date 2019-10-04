package com.example.explore;

import java.lang.annotation.Annotation;

import retrofit2.http.Multipart;

public class CustomMultipart implements Multipart {
    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
