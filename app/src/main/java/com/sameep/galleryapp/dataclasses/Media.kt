package com.sameep.galleryapp.dataclasses

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Media(val name:String, val thumbnailUrl:String, val type : Int, val date: String?=null
                 , val mime: String?=null): Parcelable