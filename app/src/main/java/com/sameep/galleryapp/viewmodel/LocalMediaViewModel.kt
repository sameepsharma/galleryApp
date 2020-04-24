package com.sameep.galleryapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sameep.galleryapp.backgroundtasks.FetchMediaModule
import com.sameep.galleryapp.dataclasses.PictureFacer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LocalMediaViewModel(val appContext: Application) : AndroidViewModel(appContext) {
    // TODO: Implement the ViewModel


    private var allMedia = MutableLiveData<List<PictureFacer>>()

    fun getAllMedia(): LiveData<List<PictureFacer>> {
        return allMedia
    }

    fun getMedia() {

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val mediaLocal = FetchMediaModule.getAllMedia(appContext)
                setDataOnMainThread(mediaLocal)
            }
        }

        //= value

    }

    private suspend fun setDataOnMainThread(mediaLocal: List<PictureFacer>) {
        withContext(Main){
            allMedia.value=mediaLocal
        }
    }


}
