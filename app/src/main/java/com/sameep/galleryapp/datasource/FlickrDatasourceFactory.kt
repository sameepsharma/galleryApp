
package com.sameep.galleryapp.datasource

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import com.sameep.galleryapp.dataclasses.Media
import com.sameep.galleryapp.enums.MediaType
import com.sameep.galleryapp.rest.ApiInterface
import kotlinx.coroutines.CoroutineScope

class FlickrDatasourceFactory(
    private val query: String,
    private val viewModelScope: CoroutineScope,
    private val mediaType: MediaType,
    val apiClient: ApiInterface
): DataSource.Factory<Int,Media>() {
    override fun create(): DataSource<Int, Media>{
        Log.e("LocalFactory>>", "Yes<<<")
        return MediaDataSource(query,viewModelScope,mediaType,apiClient)
    }

}

/*
package com.sameep.galleryapp.datasource

import android.util.Log
import androidx.paging.DataSource
import com.sameep.galleryapp.dataclasses.Media
import com.sameep.galleryapp.enums.MediaType
import com.sameep.galleryapp.rest.ApiInterface
import kotlinx.coroutines.CoroutineScope

class FlickrDatasourceFactory(
    private val query: String,
    private val viewModelScope: CoroutineScope,
    private val mediaType: MediaType,
    val apiClient: ApiInterface
): DataSource.Factory<Long, Media>() {

    override fun create(): DataSource<Long, Media> {
        Log.e("InsideFactory>>",query+"<<<")
        return MediaDataSource(query, viewModelScope, mediaType, apiClient)

    }

}*/
