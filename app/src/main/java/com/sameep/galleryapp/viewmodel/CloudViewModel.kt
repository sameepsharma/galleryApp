package com.sameep.galleryapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.sameep.galleryapp.dataclasses.PhotoModel
import com.sameep.galleryapp.rest.ApiInterface
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Retrofit

class CloudViewModel : ViewModel() {


    private val COLUMN_PHOTO = "photo"
    private val COLUMN_PHOTOS = "photos"
    private var flickrResp = MutableLiveData<ArrayList<PhotoModel>>()
    private val key : String="8b766ab7b7e827c11516eb191be5f8a1"


    fun getMediaFromFlickr(retrofit: Retrofit) {
        viewModelScope.launch {
            val service = retrofit.create(ApiInterface::class.java)

            withContext(IO) {
                val response = service.getMediaFromFlickr(key)
                response?.let {
                    if (it.isSuccessful) {
                        Log.e("Succcessfull >> ", "YES <<<")
                        val respBody = it.body().toString()
                        Log.e("AfterTosTring>>", "YES <<<")
                        val obj = JSONObject(respBody)
                        val arr = obj.optJSONObject(COLUMN_PHOTOS).optJSONArray(COLUMN_PHOTO)
                        val gson = GsonBuilder().create()
                        val size = arr.length()
                        val parsedList = ArrayList<PhotoModel>()

                        for (i in 0 until size-1){
                            val mainObj = arr.optJSONObject(i)
                            if (i==0)
                                Log.e("JsonObject>>> ", mainObj.toString()+" <0<<")
                            val item = gson.fromJson<PhotoModel>(mainObj.toString(), PhotoModel::class.java)
                            parsedList.add(item)
                        }

                        withContext(Main){
                            flickrResp.value=parsedList
                            Log.e("Size>>", "${parsedList.size}<<<")
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

    fun observeFlickrResp() : MutableLiveData<ArrayList<PhotoModel>>{
        return flickrResp
    }

}