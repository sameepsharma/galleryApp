package com.sameep.galleryapp.viewmodel


import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.sameep.galleryapp.backgroundtasks.FetchMediaModule
import com.sameep.galleryapp.dataclasses.Media
import com.sameep.galleryapp.datasource.FlickrDatasourceFactory
import com.sameep.galleryapp.datasource.MediaDataSource
import com.sameep.galleryapp.enums.MediaType
import com.sameep.galleryapp.enums.Source
import com.sameep.galleryapp.rest.ApiInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext



class MediaViewModel(
    private val appContext: Application,
    private val mediaType: MediaType,
    private val apiClient: ApiInterface,
    private val source: Source
) :
    AndroidViewModel(appContext) {
    private val key: String by lazy {
        "8b766ab7b7e827c11516eb191be5f8a1"
    }

    private val searchQuery= MutableLiveData<String>()
    private lateinit var allMedia: LiveData<PagedList<Media>>
    private val localMedia = MutableLiveData<List<Media>>()
    val config = PagedList.Config.Builder()
        .setPageSize(30)
        .setEnablePlaceholders(false)
        .build()

    init {
        if (source==Source.FLICKR){

            setAllMedia(ApiInterface.DEFAULT_QUERY)
        }
        else
            fetchLocalMedia()

    }

    fun setAllMedia(query: String?) {
        allMedia  = getMedia(query).build()
        Log.e("AllMEdiaaSize>>", "${allMedia.value?.size}")
    }

    private fun fetchLocalMedia() {

        viewModelScope.launch {
            withContext(Dispatchers.IO){
                localMedia.postValue(getLocalMedia())
            }
        }

    }




    private fun getMedia(query:String?):LivePagedListBuilder<Long, Media> {

        Log.e("QUERYFUNC>>",query+"<<<")

        val dataSourceFactory = FlickrDatasourceFactory(query?:ApiInterface.DEFAULT_QUERY, viewModelScope, mediaType)

        return LivePagedListBuilder<Long,Media>(dataSourceFactory,config)
    }


    /*init {
        getMedia()
    }*/

    fun observeAllMedia(): LiveData<PagedList<Media>> {
        return allMedia
    }


    private suspend fun getLocalMedia(): List<Media> {
        return FetchMediaModule.getAllMedia(
            getApplication(),
            mediaType
        )
    }

    fun setQuery(text: String) {
        searchQuery.value=text
    }

    fun observeSearchQuery() :MutableLiveData<String>{
        return searchQuery
    }

    fun observeLocalMedia(): MutableLiveData<List<Media>> {
        return localMedia

    }
}
