package com.sameep.galleryapp.datasource

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import com.sameep.galleryapp.dataclasses.Media
import com.sameep.galleryapp.enums.MediaType
import com.sameep.galleryapp.rest.ApiInterface
import kotlinx.coroutines.CoroutineScope

class LocalDatasourceFactory(private val viewModelScope:CoroutineScope
                             , private val mediaType:MediaType, private val context:Context): DataSource.Factory<Int,Media>() {
    override fun create(): DataSource<Int, Media>{
        Log.e("LocalFactory>>", "Yes<<<")
        return LocalMediaDataSource(mediaType,viewModelScope, context)
    }

}