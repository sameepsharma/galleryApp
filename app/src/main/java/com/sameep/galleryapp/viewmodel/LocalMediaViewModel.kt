package com.sameep.galleryapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sameep.galleryapp.backgroundtasks.FetchMediaModule
import com.sameep.galleryapp.dataclasses.FlickrResp
import com.sameep.galleryapp.dataclasses.Media
import com.sameep.galleryapp.enums.MediaType
import com.sameep.galleryapp.rest.ApiInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class LocalMediaViewModel(
    private val appContext: Application,
    private val mediaType: MediaType,
    private val apiClient: ApiInterface,
    private val isLocal: Boolean
) :
    AndroidViewModel(appContext) {
    private val key: String by lazy {
        "8b766ab7b7e827c11516eb191be5f8a1"
    }
    private val allMedia: MutableLiveData<List<Media>> by lazy {
        MutableLiveData<List<Media>>().also {
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

        viewModelScope.launch {

            withContext(Dispatchers.IO) {
                if (isLocal) {
                    var mediaList = listOf<Media>()

                    mediaList = FetchMediaModule.getAllMedia(
                        getApplication(),
                        mediaType
                    )
                    setDataOnMainThread(mediaList)
                } else {
                    var response: Response<FlickrResp>? = null

                    var isImage = false
                    if (mediaType == MediaType.IMAGE) {
                        response = apiClient.getMediaFromFlickr(key)
                        isImage = true
                    } else if (mediaType == MediaType.VIDEO) {
                        response = apiClient.getVideoSearchResult(key)
                        isImage = false
                    }
                    response?.let { it1 ->
                        if (it1.isSuccessful) {
                            val respBody = it1.body()
                            var setData = mutableListOf<Media>()
                            respBody?.photos?.let {
                                var url : String?
                                for (i in 0 until it.photo.size - 1) {
                                    val obj = it.photo[i]
                                    when(mediaType){
                                        MediaType.IMAGE -> url = "https://farm${obj.farm}.staticflickr.com/${obj.server}/${obj.id}_${obj.secret}_z.jpg"
                                        MediaType.VIDEO -> url =obj.url_z
                                    }
                                    url?.let {
                                        val item = Media(
                                            name = obj.title,
                                            thumbnailUrl = url,
                                            type = MediaType.IMAGE
                                        )
                                        setData.add(item)
                                    }

                                }
                                setDataOnMainThread(setData)
                            }
                        } else {
                            val code = response.code()
                            val errorBody = response.errorBody()?.string()
                            Log.e("CodeAndError>>> ", "$code >> $errorBody")


                        }

                    }
                }
            }

        }

    }

    private suspend fun setDataOnMainThread(localMedia: List<Media>) {
        withContext(Main) {
            allMedia.value = localMedia
        }
    }


}
