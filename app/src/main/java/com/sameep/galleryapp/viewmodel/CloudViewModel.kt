package com.sameep.galleryapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.sameep.galleryapp.dataclasses.ImageToShow
import com.sameep.galleryapp.dataclasses.PhotoModel
import com.sameep.galleryapp.rest.ApiInterface
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Retrofit

class CloudViewModel(private val apiClient: ApiInterface) : ViewModel() {

    private var flickrImages = MutableLiveData<ArrayList<ImageToShow>>()
    private var flickrVideos = MutableLiveData<ArrayList<ImageToShow>>()
    private val key: String = "8b766ab7b7e827c11516eb191be5f8a1"


    fun getLatestMediaFromFlickr() {
        viewModelScope.launch {

            withContext(IO) {
                val response = apiClient.getMediaFromFlickr(key)
                response?.let {
                    if (it.isSuccessful) {
                        Log.e("Succcessfull >> ", "YES <<<")
                        val respBody = it.body()
                        Log.e("Body >> ", "${respBody?.photos?.photo?.get(0)?.media} <<<")
                        var setData = ArrayList<ImageToShow>()
                        respBody?.photos?.let {
                            for (i in 0 until it.photo.size-1)
                            {
                                val obj = it.photo[i]
                                val url = "https://farm${obj.farm}.staticflickr.com/${obj.server}/${obj.id}_${obj.secret}_z.jpg"
                            val item = ImageToShow(obj.title, url, 1)
                                setData.add(item)
                            }
                        }


                        withContext(Main) {
                            flickrImages.value = setData
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

    }

    fun getVideosFromFlickr() {
        viewModelScope.launch {
           val response = apiClient.getVideoSearchResult(key)
            response?.let {
                if (it.isSuccessful) {
                    Log.e("Succcessfull >> ", "YES <<<")
                    val respBody = it.body()
                    Log.e("BodyVideo >> ", "${respBody?.photos?.photo?.get(0)?.media} <<<")
                    var setData = ArrayList<ImageToShow>()
                    respBody?.photos?.let {
                        for (i in 0 until it.photo.size-1)
                        {
                            val obj = it.photo[i]
                            obj.url_z?.let {
                                val item = ImageToShow(obj.title, obj.url_z, 1)
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

    fun observeLatestFlickr(): MutableLiveData<ArrayList<ImageToShow>> {
        return flickrImages
    }

    fun observeVideos(): MutableLiveData<ArrayList<ImageToShow>> {

        return flickrVideos


    }

}