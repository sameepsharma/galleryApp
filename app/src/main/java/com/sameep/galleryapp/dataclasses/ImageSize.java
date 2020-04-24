package com.sameep.galleryapp.dataclasses;

/**
 * Created by gturedi on 10.02.2017.
 */
public enum ImageSize {

    MEDIUM,
    LARGE;

    public String getValue() {
        if (this == MEDIUM) return "z";
        return "h";
    }

}