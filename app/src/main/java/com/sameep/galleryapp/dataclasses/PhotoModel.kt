package com.sameep.galleryapp.dataclasses;

import android.os.Parcelable
import com.sameep.galleryapp.dataclasses.ImageSize
import kotlinx.android.parcel.Parcelize

/**
 * Created by gturedi on 7.02.2017.
 */
@Parcelize
data class PhotoModel (val id:Long, val secret : String, val server : String, val farm : Int, var title : String, var media:String): Parcelable