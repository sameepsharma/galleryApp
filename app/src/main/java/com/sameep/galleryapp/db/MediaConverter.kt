package com.sameep.galleryapp.db

import android.provider.MediaStore
import androidx.room.TypeConverter
import com.sameep.galleryapp.enums.MediaType

class MediaConverter {

    @TypeConverter
    fun fromMediaTypeToInt(mediaType: MediaType): Int {
        return when (mediaType) {
            MediaType.VIDEO -> MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
            MediaType.IMAGE -> MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
            else -> 0
        }
    }

    @TypeConverter
    fun fromIntToMediaType(type: Int): MediaType {
        return when (type) {
            MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO -> MediaType.VIDEO
            MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE -> MediaType.IMAGE
            else -> MediaType.DEFAULT
        }
    }

}