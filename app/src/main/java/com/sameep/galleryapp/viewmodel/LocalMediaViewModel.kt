package com.sameep.galleryapp.viewmodel

import android.app.Application
import android.provider.MediaStore
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


    private var allImages = MutableLiveData<List<PictureFacer>>()
    private var allVideos = MutableLiveData<List<PictureFacer>>()

    fun observeAllImages(): LiveData<List<PictureFacer>> {
        return allImages
    }

    fun observeAllVideos(): LiveData<List<PictureFacer>>{
        return allVideos
    }

    fun getMedia() {

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val mediaLocal = FetchMediaModule.getAllMedia(appContext)
                mediaLocal?.let {
                    sortMedia(mediaLocal)
                }
            }
        }
    }

    private suspend fun sortMedia(mediaLocal: List<PictureFacer>) {
        val imageList = ArrayList<PictureFacer>()
        val videoList = ArrayList<PictureFacer>()
        for (i in 0 until mediaLocal.size)
        {
            if (mediaLocal.get(i).mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE )
                imageList.add(mediaLocal.get(i))
            else
                videoList.add(mediaLocal.get(i))
        }

        setDataOnMainThread(imageList, videoList)
    }

    private suspend fun setDataOnMainThread(
        imageList: List<PictureFacer>,
        videoList: List<PictureFacer>
    ) {
        withContext(Main){
            allImages.value=imageList
            allVideos.value=videoList
        }
    }


}
