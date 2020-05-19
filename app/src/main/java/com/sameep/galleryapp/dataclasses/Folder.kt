package com.sameep.galleryapp.dataclasses

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "folders_table")
data class Folder(@PrimaryKey val folderName:String
                  , val dateCreated : Long, val dateDeleted : Long?, val dateUpdated:Long) :
    Parcelable