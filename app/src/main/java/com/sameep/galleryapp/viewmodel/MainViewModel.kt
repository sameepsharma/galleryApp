package com.sameep.galleryapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sameep.galleryapp.backgroundtasks.FetchMediaModule
import com.sameep.galleryapp.backgroundtasks.FetchMediaModule.Companion.getAllMedia
import com.sameep.galleryapp.dataclasses.PictureFacer
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainViewModel(applicationRef: Application) :
    AndroidViewModel(applicationRef)//, ResultListener
{

//    val task = FetchMediaTask(applicationRef)

    private var allMedia = MutableLiveData<List<PictureFacer>>()

    fun getAllMedia(): LiveData<List<PictureFacer>> {
        return allMedia
    }

    fun getMedia() {
        var value = emptyList<PictureFacer>()
        viewModelScope.launch {
            value = getAllMedia(getApplication())
        }

        allMedia.value = value

    }

    /*fun loadMedia() {
        task.resultRef=this
        task.execute()
    }

    override fun getResult(resultMedia: List<PictureFacer>?) {
        allMedia.value = resultMedia
    }*/

}
