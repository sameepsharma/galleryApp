package com.sameep.galleryapp.viewmodel


import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.sameep.galleryapp.backgroundtasks.FetchMediaModule
import com.sameep.galleryapp.dataclasses.Media
import com.sameep.galleryapp.datasource.FlickrDatasourceFactory
import com.sameep.galleryapp.enums.MediaType
import com.sameep.galleryapp.enums.Source
import com.sameep.galleryapp.rest.ApiInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MediaViewModel(
    private val appContext: Application,
    private val mediaType: MediaType,
    private val apiClient: ApiInterface,
    private val source: Source
) :
    AndroidViewModel(appContext) {

    private val searchQuery= MutableLiveData<String>()
    private lateinit var allMedia: LiveData<PagedList<Media>>
    private val localMedia = MutableLiveData<List<Media>>()
    val config = PagedList.Config.Builder()
        .setPageSize(50)
        .setEnablePlaceholders(false)
        .build()

    init {

        if (source==Source.FLICKR){

            searchMediaByQuery(ApiInterface.DEFAULT_QUERY)
        }
        else
            fetchLocalMedia()

    }

    fun searchMediaByQuery(query: String) {
        allMedia  = getMedia(query).build()
    }

    private fun fetchLocalMedia() {

        viewModelScope.launch {
            withContext(Dispatchers.IO){
                localMedia.postValue(getLocalMedia())
            }
        }

    }

    private fun getMedia(query:String):LivePagedListBuilder<Long, Media> {

        Log.e("QUERYFUNC>>",query+"<<<")

        val dataSourceFactory = FlickrDatasourceFactory(query, viewModelScope, mediaType)

        Log.e("AfterFactory>>>", "YES")
        return LivePagedListBuilder<Long,Media>(dataSourceFactory,config)
    }

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

    fun invalidateData() {
        TODO("Not yet implemented")
    }
}
