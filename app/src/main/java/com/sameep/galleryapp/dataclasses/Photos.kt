package com.sameep.galleryapp.dataclasses

data class Photos(
    val page : Int,
    val pages : Int,
    val perpage : Int,
    val total : Int,
    val photo : ArrayList<PhotoModel>)