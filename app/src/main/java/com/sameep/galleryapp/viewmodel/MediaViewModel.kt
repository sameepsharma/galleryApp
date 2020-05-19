package com.sameep.galleryapp.viewmodel


import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.sameep.galleryapp.dataclasses.Folder
import com.sameep.galleryapp.dataclasses.Media
import com.sameep.galleryapp.datasource.FlickrDatasourceFactory
import com.sameep.galleryapp.datasource.LocalDatasourceFactory
import com.sameep.galleryapp.db.MediaDao
import com.sameep.galleryapp.db.MediaDatabase
import com.sameep.galleryapp.enums.MediaType
import com.sameep.galleryapp.enums.Source
import com.sameep.galleryapp.rest.ApiInterface
import kotlinx.coroutines.launch


class MediaViewModel(
    private val appContext: Application,
    private val mediaType: MediaType,
    private val apiClient: ApiInterface,
    private val source: Source
) :
    AndroidViewModel(appContext) {

    companion object {

        const val EMPTYQUERY = ""
    }

    val mediator = MediatorLiveData<PagedList<Media>>()

    lateinit var media: LiveData<PagedList<Media>>

    private val config = PagedList.Config.Builder()
        .setPageSize(30)
        .setEnablePlaceholders(false)
        .build()
    var vmCount = 0

    init {
        if (source == Source.FLICKR) {
            searchMediaByQuery(ApiInterface.DEFAULT_QUERY)
        } else {
            fetchMedia(EMPTYQUERY)
        }

    }

    fun searchMediaByQuery(query: String) = fetchMedia(query)

    private fun fetchMedia(query: String) {

        val mediaFactory =
            if (source == Source.LOCAL) LocalDatasourceFactory(
                viewModelScope,
                mediaType,
                appContext
            )
            else FlickrDatasourceFactory(query, viewModelScope, mediaType, apiClient)

        media = LivePagedListBuilder(mediaFactory, config).build()
        mediator.addSource(media, Observer {
            mediator.postValue(it)
        })
    }
}
