package com.sameep.galleryapp.backgroundtasks

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.sameep.galleryapp.dataclasses.Media
import com.sameep.galleryapp.enums.MediaType

class FetchMediaModule {

    companion object {
        suspend fun getAllMedia(context: Context, mediaType:MediaType): List<Media> {

            var type = if (mediaType==MediaType.IMAGE) MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
            else MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO


               val columns = arrayOf(
                   MediaStore.Files.FileColumns._ID,
                   MediaStore.Files.FileColumns.DATA,
                   MediaStore.Files.FileColumns.DATE_ADDED,
                   MediaStore.Files.FileColumns.MEDIA_TYPE,
                   MediaStore.Files.FileColumns.MIME_TYPE,
                   MediaStore.Files.FileColumns.DISPLAY_NAME
               )
               var images = mutableListOf<Media>()

               val selection = (MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                       + type
                       )
               val orderBy = MediaStore.Files.FileColumns.DATE_ADDED
               val queryUri: Uri = MediaStore.Files.getContentUri("external")

               val cursor: Cursor? = context.contentResolver.query(
                   queryUri,
                   columns,
                   selection,
                   null,
                   orderBy + " DESC" // Sort order.
               )
               cursor?.let {
                   try {
                       cursor.moveToFirst()
                       do {
                           val pic = Media(
                               name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)),
                               thumbnailUrl = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)),
                               type = if (cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE))==MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) MediaType.IMAGE else MediaType.VIDEO,
                               date = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_ADDED)),
                               mime = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE))
                               //cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE))

                               // cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE))
                           )
                           images.add(pic)
                       } while (cursor.moveToNext())
                       cursor.close()

                   } catch (e: Exception) {
                       Log.e("ExceptionHere>> ", e.localizedMessage)
                       e.printStackTrace()
                   }
               }

              return images
        }

            }

}