package com.sameep.galleryapp.backgroundtasks

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.sameep.galleryapp.dataclasses.PictureFacer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FetchMediaModule {

    companion object {
        suspend fun getAllMedia(context: Context): List<PictureFacer> {

           return withContext(Dispatchers.IO){

               val columns = arrayOf(
                   MediaStore.Files.FileColumns._ID,
                   MediaStore.Files.FileColumns.DATA,
                   MediaStore.Files.FileColumns.DATE_ADDED,
                   MediaStore.Files.FileColumns.MEDIA_TYPE,
                   MediaStore.Files.FileColumns.MIME_TYPE,
                   MediaStore.Files.FileColumns.DISPLAY_NAME
               )
               var images = mutableListOf<PictureFacer>()
               val selection = (MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                       + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                       + " OR "
                       + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                       + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)
               val orderBy = MediaStore.Files.FileColumns.DATE_ADDED
               val queryUri: Uri = MediaStore.Files.getContentUri("external")

               val cursor: Cursor? = context.getContentResolver().query(
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
                           val pic: PictureFacer = PictureFacer(
                               cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)),
                               cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)),
                               cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE)),
                               cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_ADDED)),
                               cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE)),
                               cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE))

                               // cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE))
                           )
                           images.add(pic)
                       } while (cursor!!.moveToNext())
                       cursor.close()

                   } catch (e: Exception) {
                       Log.e("ExceptionHere>> ", e.localizedMessage)
                       e.printStackTrace()
                   }
               }

               images
           }
        }

            }

}