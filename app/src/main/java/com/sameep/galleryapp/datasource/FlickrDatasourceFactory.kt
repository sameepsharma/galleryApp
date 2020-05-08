package com.sameep.galleryapp.datasource

import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import com.sameep.galleryapp.dataclasses.Media
import com.sameep.galleryapp.enums.MediaType
import com.sameep.galleryapp.rest.ApiInterface
import kotlinx.coroutines.CoroutineScope

class FlickrDatasourceFactory(private val query :String,private val viewModelScope:CoroutineScope, private val mediaType:MediaType): DataSource.Factory<Long, Media>() {

    override fun create(): DataSource<Long, Media> {
        return MediaDataSource(query, viewModelScope, mediaType)

    }

}