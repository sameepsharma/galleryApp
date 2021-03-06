package com.sameep.galleryapp.backgroundtasks

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.AsyncTask
import android.provider.MediaStore
import android.util.Log
import com.sameep.galleryapp.dataclasses.PictureFacer
//Get Result
open interface ResultListener {
    fun getResult(resultMedia: List<PictureFacer>?)
}
open class FetchMediaTask internal constructor(val context: Context) :
    AsyncTask<Unit, Unit, List<PictureFacer>>() {



    var resultRef: ResultListener? = null

    override fun doInBackground(vararg params: Unit?): List<PictureFacer> {
        return getAllMedia()
    }

    override fun onPostExecute(result: List<PictureFacer>) {
        super.onPostExecute(result)

        resultRef?.getResult(result)
        //MainViewModel.allMedia.value = result
    }

    fun getAllMedia(): List<PictureFacer> {
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

        Log.e("BeforeSize>>>", "${images.size}<<<")
        return images
    }

}
