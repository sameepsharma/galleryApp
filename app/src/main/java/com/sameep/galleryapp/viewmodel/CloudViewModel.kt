
package com.sameep.galleryapp.viewmodel

import android.provider.MediaStore
import android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sameep.galleryapp.dataclasses.FlickrResp
import com.sameep.galleryapp.dataclasses.Media
import com.sameep.galleryapp.rest.ApiInterface
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response



class CloudViewModel(private val apiClient: ApiInterface,private val isImage:Boolean) : ViewModel() {

    private val key: String by lazy {
        "8b766ab7b7e827c11516eb191be5f8a1"
    }
    private var flickrMedia = MutableLiveData<ArrayList<Media>>()


    fun getLatestMediaFromFlickr() {
        viewModelScope.launch {
            var response:Response<FlickrResp>
            withContext(IO) {
                if (isImage)
                    response = apiClient.getMediaFromFlickr(key)
                else
                    response = apiClient.getVideoSearchResult(key)
                response?.let { it1 ->
                    if (it1.isSuccessful) {
                        Log.e("Succcessfull >> ", "YES <<<")
                        val respBody = it1.body()
                        var setData = ArrayList<Media>()
                        respBody?.photos?.let {
                            for (i in 0 until it.photo.size-1) {
                                val obj = it.photo[i]
                                if (isImage) {

                                    val url =
                                        "https://farm${obj.farm}.staticflickr.com/${obj.server}/${obj.id}_${obj.secret}_z.jpg"
                                    val item = Media(
                                        name = obj.title,
                                        thumbnailUrl = url,
                                        type = MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                                    )
                                    setData.add(item)
                                } else {
                                    obj.url_z?.let {
                                        val item = Media(
                                            name = obj.title,
                                            thumbnailUrl = obj.url_z,
                                            type = MEDIA_TYPE_VIDEO
                                        )
                                        setData.add(item)
                                    }
                                }
                            }
                            withContext(Main) {
                                flickrMedia.value = setData
                                //Log.e("Size>>", "${parsedList.size}<<<")
                            }
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

    /*fun getVideosFromFlickr() {
        viewModelScope.launch {
           val response = apiClient.getVideoSearchResult(key)
            response?.let {
                if (it.isSuccessful) {
                    Log.e("Succcessfull >> ", "YES <<<")
                    val respBody = it.body()
                    Log.e("BodyVideo >> ", "${respBody?.photos?.photo?.get(0)?.media} <<<")
                    var setData = ArrayList<Media>()
                    respBody?.photos?.let {
                        for (i in 0 until it.photo.size-1)
                        {
                            val obj = it.photo[i]
                            obj.url_z?.let {
                                val item = Media(obj.title, obj.url_z, MEDIA_TYPE_VIDEO)
                                setData.add(item)
                            }

                        }
                    }
                    withContext(Main) {
                        flickrVideos.value = setData
                        //Log.e("Size>>", "${parsedList.size}<<<")
                    }

                } else {
                    val code = response.code()
                    val errorBody = response.errorBody()?.string()
                    Log.e("CodeAndError>>> ", "$code >> $errorBody")


                }


            }
        }
    }

    fun observeLatestFlickr(): MutableLiveData<ArrayList<Media>> {
        return flickrImages
    }*/

    fun observeMedia(): MutableLiveData<ArrayList<Media>> {

        return flickrMedia


    }

}