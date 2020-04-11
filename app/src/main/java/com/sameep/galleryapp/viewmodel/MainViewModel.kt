package com.sameep.galleryapp.viewmodel

import android.app.Application
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.AsyncTask
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sameep.galleryapp.activities.MainActivity
import com.sameep.galleryapp.backgroundtasks.FetchMediaTask
import com.sameep.galleryapp.dataclasses.PictureFacer
import kotlinx.android.synthetic.main.activity_main.*

class MainViewModel(val applicationRef: Application) : AndroidViewModel(applicationRef), FetchMediaTask.AsyncResponse {

    var allMedia = MutableLiveData<List<PictureFacer>>()

    fun getAllMedia(): LiveData<List<PictureFacer>> {
        return allMedia
    }

    fun loadMedia() {
        val task = FetchMediaTask(applicationRef)
        task.delegate=this
        task.execute()
    }

    override fun processFinish(resultMedia: List<PictureFacer>?) {
        allMedia.value = resultMedia
    }

}
