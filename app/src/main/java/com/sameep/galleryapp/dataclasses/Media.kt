package com.sameep.galleryapp.dataclasses

import android.os.Parcelable
import com.sameep.galleryapp.enums.MediaType
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Media(val name:String, val thumbnailUrl:String?, val type : MediaType, val date: String?=null
                 , val mime: String?=null, val tags : String?, val id:Long): Parcelable