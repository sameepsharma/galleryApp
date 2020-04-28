package com.sameep.galleryapp.dataclasses;

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
@Parcelize
data class PhotoModel (val id:Long, val secret : String, val server : String, val farm : Int, val title : String, val media:String, val url_z:String?): Parcelable