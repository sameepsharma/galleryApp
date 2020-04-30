package com.sameep.galleryapp.viewmodel

import android.app.Application
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sameep.galleryapp.backgroundtasks.FetchMediaModule
import com.sameep.galleryapp.dataclasses.Media
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LocalMediaViewModel(val appContext: Application, val isImage: Boolean) :
    AndroidViewModel(appContext) {
    // TODO: Implement the ViewModel


    private val allMedia : MutableLiveData<List<Media>> by lazy {
        MutableLiveData<List<Media>>() .also {
            getMedia()
        }
    }

    /*init {
        getMedia()
    }*/


    fun observeAllMedia(): LiveData<List<Media>> {
        return allMedia
    }


    fun getMedia() {

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                var mediaList = listOf<Media>()
                if (isImage) {
                    mediaList = FetchMediaModule.getAllMedia(
                        appContext,
                        MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                    )
                } else {
                    mediaList = FetchMediaModule.getAllMedia(
                        appContext,
                        MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
                    )

                }
                setDataOnMainThread(mediaList)
            }
    }
}

private suspend fun setDataOnMainThread(localMedia: List<Media>) {
    withContext(Main) {
        allMedia.value=localMedia
    }
}


}
