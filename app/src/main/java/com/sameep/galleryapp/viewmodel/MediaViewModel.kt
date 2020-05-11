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
import com.sameep.galleryapp.datasource.LocalDatasourceFactory
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

    //private var _flickrMedia = MutableLiveData<PagedList<Media>>()
    lateinit var flickrMedia: LiveData<PagedList<Media>>

    //get() = _flickrMedia
    lateinit var localMedia: LiveData<PagedList<Media>>

    private val config = PagedList.Config.Builder()
        .setPageSize(30)
        .setEnablePlaceholders(false)
        .build()

    init {

        if (source == Source.FLICKR) {

            searchMediaByQuery(ApiInterface.DEFAULT_QUERY)
        } else
            fetchLocalMedia()

    }

    fun searchMediaByQuery(query: String) = getMedia(query)

    fun fetchLocalMedia() {
        val localFactory = LocalDatasourceFactory(viewModelScope, mediaType, appContext)
        localMedia = LivePagedListBuilder(localFactory, config).build()

    }

    private fun getMedia(query: String){

        Log.e("QUERYFUNC>>", query + "<<<")

        val dataSourceFactory = FlickrDatasourceFactory(query, viewModelScope, mediaType)

        flickrMedia = LivePagedListBuilder<Long, Media>(dataSourceFactory, config).build()
    }
}
