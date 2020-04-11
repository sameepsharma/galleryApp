package com.sameep.galleryapp.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidcodeman.simpleimagegallery.utils.GalleryAdapter
import com.sameep.galleryapp.R
import com.sameep.galleryapp.dataclasses.PictureFacer
import com.sameep.galleryapp.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*


private const val REQUEST_PERMISSIONS = 1001
private lateinit var mainViewModel: MainViewModel

class MainActivity : AppCompatActivity(), GalleryAdapter.onClickItem {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = MainViewModel(application)

        setupViews()
        if (hasPermission()) {
            mainViewModel.loadMedia()
            setUpObserver()
        } else
            checkpermission()
        //getMedia()


    }

    private fun setUpObserver() {

        mainViewModel.getAllMedia().observe(this, Observer {
            setAdapter(it)
        })
    }

    private fun hasPermission(): Boolean {

        return (ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
                )
    }

    private fun setAdapter(data: List<PictureFacer>) {

        val adapterTwo = GalleryAdapter(data, this)
        adapterTwo.delegate=this
        main_rv.adapter = adapterTwo
        main_loader.visibility = View.GONE
    }

    private fun setupViews() {
        main_loader.visibility = View.VISIBLE

        val layoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
        main_rv.layoutManager = layoutManager
        main_rv.hasFixedSize()

        main_swipe.setOnRefreshListener {
            main_loader.visibility = View.VISIBLE

            mainViewModel.loadMedia()

            main_swipe.isRefreshing = false
        }

    }

    private fun checkpermission() {
        requestPermissions(
            this@MainActivity,
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),
            REQUEST_PERMISSIONS
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_PERMISSIONS -> {

                mainViewModel.loadMedia()
                setUpObserver()
            }
        }
    }

    override fun fireIntent(image: PictureFacer) {
        var intent:Intent?=null
       // if (image.mediaType==1)
        intent = Intent(this@MainActivity, ImageDetailActivity::class.java)
       /* else{ // Some other target activity
             }*/

        intent?.putExtra(ImageDetailActivity.dataKey.INTENT_DATA, image)

        startActivity(intent)
    }
}
