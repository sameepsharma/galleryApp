package com.sameep.galleryapp.datasource

import android.content.Context
import android.util.Log
import androidx.paging.PositionalDataSource
import com.sameep.galleryapp.backgroundtasks.FetchMediaModule
import com.sameep.galleryapp.dataclasses.Media
import com.sameep.galleryapp.enums.MediaType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LocalMediaDataSource(private val mediaType: MediaType, private val scope: CoroutineScope, private val context: Context): PositionalDataSource<Media>() {

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Media>) {
        var list = listOf<Media>()
        scope.launch {
            val media = FetchMediaModule.getAllMedia(context,mediaType,params.loadSize,params.startPosition)
            withContext(Dispatchers.Main){
                list = media
                callback.onResult(list)

            }
        }
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Media>) {
        var list = listOf<Media>()
        scope.launch {
            val media = FetchMediaModule.getAllMedia(context,mediaType,params.requestedLoadSize, params.requestedStartPosition)
            Log.e("LocalSize>>>", "${media.size} <<<<")
            withContext(Dispatchers.Main){
                list = media
                callback.onResult(list,0)

                Log.e("LocalSizeMain>>>", "${list.size} <<<<")
            }
        }
    }
}