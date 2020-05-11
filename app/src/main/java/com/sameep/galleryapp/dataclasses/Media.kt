package com.sameep.galleryapp.dataclasses

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sameep.galleryapp.enums.MediaType
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "media_table")
data class Media(val name:String, val thumbnailUrl:String?, val type : MediaType, val date: String?=null
                 , val mime: String?=null, val tags : String?, @PrimaryKey @ColumnInfo(name = "id") val id:Long): Parcelable