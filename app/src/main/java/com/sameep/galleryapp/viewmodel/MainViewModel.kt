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
import com.sameep.galleryapp.backgroundtasks.FetchMediaCoroutine
import com.sameep.galleryapp.backgroundtasks.FetchMediaTask
import com.sameep.galleryapp.backgroundtasks.ResultListener
import com.sameep.galleryapp.dataclasses.PictureFacer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.withContext


class MainViewModel( applicationRef: Application) : AndroidViewModel(applicationRef)//, ResultListener
 {

//    val task = FetchMediaTask(applicationRef)

    private var allMedia = MutableLiveData<List<PictureFacer>>()

    fun getAllMedia(): LiveData<List<PictureFacer>> {
        return allMedia
    }

     suspend fun getMedia(){

         val value = FetchMediaCoroutine.getAllMedia(getApplication())
         withContext(Main){
             allMedia.value=value
         }
     }

    /*fun loadMedia() {
        task.resultRef=this
        task.execute()
    }

    override fun getResult(resultMedia: List<PictureFacer>?) {
        allMedia.value = resultMedia
    }*/

}
