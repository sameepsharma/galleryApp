package com.sameep.galleryapp.enums

enum class MediaType(val value:Int) {
    DEFAULT(0),
    IMAGE(1),
    //AUDIO(2),
    VIDEO(3),
    CUSTOM(4);

    fun getVal():Int{
        return value
    }

}