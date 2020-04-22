package com.sameep.galleryapp.activities.ui.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.photos.library.v1.PhotosLibraryClient
import com.google.photos.types.proto.MediaItem
import com.kogicodes.sokoni.models.custom.Resource
import com.sameep.galleryapp.rest.ApiInterface
import com.sameep.galleryapp.utils.PhotosLibraryClientFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import java.util.*

class DashboardViewModel : ViewModel() {

    private var client: PhotosLibraryClient?=null
    private val mediaItemObservable = MutableLiveData<Resource<List<MediaItem>>>()
    private var loading = false


    fun getMediaFromGoogle(retrofit: Retrofit,token: String?) {
        viewModelScope.launch {
            val service = retrofit.create(ApiInterface::class.java)

            withContext(IO){

                val mediaResp = service.getAllMedia("Bearer $token")
                withContext(Main){
                    if (mediaResp.isSuccessful){
                        Log.e("RespMedia >>>", "${mediaResp.body()}")
                    }else{
                        Log.e("FailCode>>> ", "${mediaResp.code()}")
                        Log.e("ErrorBody>>>", "${mediaResp.errorBody()?.string()}")

                    }

                }
            }

        }
    }

    fun observeMediaItem():LiveData<Resource<List<MediaItem>>> {
        return mediaItemObservable
    }
    fun getMediaItem(googleSignInAccount : String)  {
        mediaItemObservable.value= Resource.loading("Loading Images",null)
        viewModelScope.launch() {

            client = googleSignInAccount.let { it1 -> PhotosLibraryClientFactory.createClient(it1) }

            val response = client?.searchMediaItems("")

            val mediaItems = LinkedList<MediaItem>()

            if (response != null) {
                for (m in response.iterateAll()) {
                    mediaItems.add(m)
                    m.baseUrl
                }
            } else {
                mediaItemObservable.postValue( Resource.error("Response is null", mediaItems))

            }
            if (mediaItems.size > 0) {
                mediaItemObservable.postValue(  Resource.success("",mediaItems))
            } else {
                mediaItemObservable.postValue( Resource.error("No Media Found", mediaItems))

            }


        }
    }


    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text
}