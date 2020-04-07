package com.sameep.galleryapp.activities

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sameep.galleryapp.R
import com.sameep.galleryapp.adapters.GalleryAdapter
import kotlinx.android.synthetic.main.activity_main.*


private const val REQUEST_PERMISSIONS = 1001

private var count = 0
private lateinit var thumbnails: Array<Bitmap?>
private var thumbnailsselection: BooleanArray? = null
private lateinit var arrPath: Array<String?>
private lateinit var arrName: Array<String?>
private lateinit var arrDate: Array<String?>
private lateinit var arrMime: Array<String?>
private lateinit var typeMedia: Array<Int?>
private var galleryAdapter: GalleryAdapter? = null
lateinit var loader: ProgressBar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViews()
        checkpermission()


    }

    private fun setupViews() {
       main_loader.visibility=View.VISIBLE

        val  layoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
        main_rv.layoutManager=layoutManager

        main_swipe.setOnRefreshListener {
            getMedia()
            main_swipe.isRefreshing=false
        }

    }

    private fun checkpermission() {
        /*RUN TIME PERMISSIONS*/
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@MainActivity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) && ActivityCompat.shouldShowRequestPermissionRationale(
                    this@MainActivity,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
            } else {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                    REQUEST_PERMISSIONS
                )
            }
        } else {
            Log.e("Else", "Else")
            getMedia()
        }
    }



    private fun getMedia() {

        val columns = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.DATE_ADDED,
            MediaStore.Files.FileColumns.MEDIA_TYPE,
            MediaStore.Files.FileColumns.MIME_TYPE,
            MediaStore.Files.FileColumns.DISPLAY_NAME
        )
        val selection = (MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                + " OR "
                + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)
        val orderBy = MediaStore.Files.FileColumns.DATE_ADDED
        val queryUri: Uri = MediaStore.Files.getContentUri("external")

        val imagecursor: Cursor = this.managedQuery(
            queryUri,
            columns,
            selection,
            null,  // Selection args (none).
            MediaStore.Files.FileColumns.DATE_ADDED + " DESC" // Sort order.
        )

        val image_column_index: Int = imagecursor.getColumnIndex(MediaStore.Files.FileColumns._ID)
        count = imagecursor.getCount()
        thumbnails = arrayOfNulls<Bitmap>(count)
        arrPath = arrayOfNulls<String>(count)
        typeMedia = arrayOfNulls<Int>(count)
        thumbnailsselection = BooleanArray(count)
        arrName= arrayOfNulls<String>(count)
        arrDate= arrayOfNulls(count)
        arrMime= arrayOfNulls(count)

        for (i in 0 until count) {
            imagecursor.moveToPosition(i)
            val id: Int = imagecursor.getInt(image_column_index)
            val dateCol = imagecursor.getColumnIndex(MediaStore.Files.FileColumns.DATE_ADDED)
            arrDate[i] = imagecursor.getString(dateCol)
            val mimeCol = imagecursor.getColumnIndex(MediaStore.Files.FileColumns.MIME_TYPE)
            arrMime[i] = imagecursor.getString(mimeCol)
            val dataColumnIndex: Int = imagecursor.getColumnIndex(MediaStore.Files.FileColumns.DATA)
            val bmOptions = BitmapFactory.Options()
            bmOptions.inSampleSize = 4
            bmOptions.inPurgeable = true
            val type: Int = imagecursor.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE)
            val t: Int = imagecursor.getInt(type)
            typeMedia[i] = t
            if (t == 1) {thumbnails[i] = MediaStore.Images.Thumbnails.getThumbnail(
                this.getContentResolver(), id.toLong(),
                MediaStore.Images.Thumbnails.MINI_KIND, bmOptions
            )
                val nameColumn: Int = imagecursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
                arrName[i]=imagecursor.getString(nameColumn)

            } else if (t == 3) {thumbnails[i] = MediaStore.Video.Thumbnails.getThumbnail(
                this.getContentResolver(), id.toLong(),
                MediaStore.Video.Thumbnails.MINI_KIND, bmOptions
            )
                val nameColumn: Int = imagecursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME)
                arrName[i]=imagecursor.getString(nameColumn)

            }
            arrPath[i] = imagecursor.getString(dataColumnIndex)

        }
        Log.e("Size>>>", "${thumbnails.size}<<<")
        val galleryAdapter = GalleryAdapter(this@MainActivity, thumbnails, arrName, arrPath, typeMedia, arrDate, typeMedia, arrMime)
        main_rv.adapter=galleryAdapter
        main_loader.visibility=View.GONE
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQUEST_PERMISSIONS->{
                for (i in 0 until  grantResults.size) {
                    if (grantResults.size > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    getMedia();
                } else {
                    Toast.makeText(this, "The app was not allowed to read or write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
            }
        }
        }
    }

}
