package com.sameep.galleryapp.dataclasses

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImageToShow(val name:String, val url:String, val type : Int): Parcelable