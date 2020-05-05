package com.sameep.galleryapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sameep.galleryapp.backgroundtasks.FetchMediaModule
import com.sameep.galleryapp.dataclasses.Media
import com.sameep.galleryapp.enums.MediaType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LocalMediaViewModel(private val appContext: Application, private val mediaType: MediaType) :
    AndroidViewModel(appContext) {
    // TODO: Implement the ViewModel

    private val allMedia : MutableLiveData<List<Media>> by lazy {
        MutableLiveData<List<Media>>() .also {
            getMedia()
            Log.e("After getMedia>>", "Yes<<<")
        }
    }


    /*init {
        getMedia()
    }*/


    fun observeAllMedia(): LiveData<List<Media>> {
        return allMedia
    }


    fun getMedia() {
        Log.e("LocalMEdia", " Statement 1 ")
        viewModelScope.launch {
            Log.e("LocalMEdia", " Statement 2 ")

            withContext(Dispatchers.IO) {
                Log.e("LocalMEdia", " Statement 3 ")

                var mediaList = listOf<Media>()

                    mediaList = FetchMediaModule.getAllMedia(
                        getApplication(),
                        mediaType
                    )
                //allMedia.value=mediaList

                setDataOnMainThread(mediaList)
            }
            Log.e("LocalMEdia", " Statement 4 ")

        }
        Log.e("LocalMEdia", " Statement 5 ")

    }

private suspend fun setDataOnMainThread(localMedia: List<Media>) {
    withContext(Main) {
        allMedia.value=localMedia
    }
}


}
