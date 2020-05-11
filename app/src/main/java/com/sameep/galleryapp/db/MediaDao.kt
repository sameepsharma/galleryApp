package com.sameep.galleryapp.db

import androidx.room.Insert
import com.sameep.galleryapp.dataclasses.Media

interface MediaDao {

@Insert
suspend fun insertMedia(mediaList:List<Media>)
}