package com.sameep.galleryapp.dataclasses

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PictureFacer(
    val picturName: String, val picturePath: String
    , val mediaType: Int, val date: String
    , val mime: String, val type: Int
) : Parcelable {

}