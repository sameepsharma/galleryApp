package com.sameep.galleryapp.viewmodel

import android.app.Application
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sameep.galleryapp.backgroundtasks.FetchMediaModule
import com.sameep.galleryapp.dataclasses.ImageToShow
import com.sameep.galleryapp.dataclasses.PictureFacer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LocalMediaViewModel(val appContext: Application) : AndroidViewModel(appContext) {
    // TODO: Implement the ViewModel


    private var allImages = MutableLiveData<List<ImageToShow>>()
    private var allVideos = MutableLiveData<List<ImageToShow>>()

    fun observeAllImages(): LiveData<List<ImageToShow>> {
        return allImages
    }

    fun observeAllVideos(): LiveData<List<ImageToShow>>{
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
        val imageList = ArrayList<ImageToShow>()
        val videoList = ArrayList<ImageToShow>()
        for (i in 0 until mediaLocal.size)
        {
            val localObj = mediaLocal[i]
            if (localObj.mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE )
                {

                    //imageList.add(localObj)
                    val obj = ImageToShow(localObj.picturName,localObj.picturePath, localObj.mediaType)
                    imageList.add(obj)
                }
            else{
                val obj = ImageToShow(localObj.picturName, localObj.picturePath, localObj.mediaType)
                videoList.add(obj)
            }
        }

        setDataOnMainThread(imageList, videoList)
    }

    private suspend fun setDataOnMainThread(
        imageList: List<ImageToShow>,
        videoList: List<ImageToShow>
    ) {
        withContext(Main){
            allImages.value=imageList
            allVideos.value=videoList
        }
    }


}
