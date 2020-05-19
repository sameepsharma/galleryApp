package com.sameep.galleryapp.datasource

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.sameep.galleryapp.dataclasses.FlickrResp
import com.sameep.galleryapp.dataclasses.Media
import com.sameep.galleryapp.enums.MediaType
import com.sameep.galleryapp.rest.ApiInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MediaDataSource(
    private val query: String,
    private val scope: CoroutineScope,
    private val mediaType: MediaType,
    val apiClient: ApiInterface
): PageKeyedDataSource<Int, Media>() {

    private val FIRST_PAGE = 1


    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Media>
    ) {
        scope.launch {

            val response = if (mediaType == MediaType.IMAGE) apiClient.getImagesForQuery(
                ApiInterface.key,
                query,
                FIRST_PAGE,
                params.requestedLoadSize
            )
            else apiClient.getVideoSearchResult(ApiInterface.key, query, FIRST_PAGE, params.requestedLoadSize)
            if (response.isSuccessful) {
                response?.let { it1 ->
                    if (it1.isSuccessful) {
                        val respBody = it1.body()
                        val listOfMedia = getListFromResponse(respBody)

                        callback.onResult(listOfMedia,null,FIRST_PAGE+1)

                    } else {
                        val code = response.code()
                        val errorBody = response.errorBody()?.string()
                        Log.e("CodeAndError>>> ", "$code >> $errorBody")
                    }

                }
            } else {
                Log.e("ErrorImagesQuery>>", response.errorBody()?.string() + " <<<")
            }
        }
    }

        override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Media>) {
            Log.e("AfterCallled<<<", "${params.key}<<<")
            scope.launch {
                val response = apiClient.getImagesForQuery(ApiInterface.key, query, params.key, params.requestedLoadSize)
                if (response.isSuccessful){
                    response.let { it1 ->
                        if (it1.isSuccessful) {

                            val respBody = it1.body()

                            val listOfMedia = getListFromResponse(respBody)
                            callback.onResult(listOfMedia, params.key+1)

                        } else {
                            val code = response.code()
                            val errorBody = response.errorBody()?.string()
                            Log.e("CodeAndError>>> ", "$code >> $errorBody")
                        }

                    }
                }else{
                    Log.e("ErrorImagesQuery>>",response.errorBody()?.string()+" <<<")
                }
            }        }

        override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Media>) {
            TODO("Not yet implemented")
        }

        private fun getListFromResponse(respBody: FlickrResp?): List<Media> {

            val setData = mutableListOf<Media>()
            respBody?.photos?.let {

                for (i in 0 until it.photo.size - 1) {
                    val obj = it.photo[i]

                    val url = when (mediaType) {
                        MediaType.IMAGE -> "https://farm${obj.farm}.staticflickr.com/${obj.server}/${obj.id}_${obj.secret}_z.jpg"
                        MediaType.VIDEO -> obj.url_z
                        else->""
                    }
                    val item = Media(
                        name = obj.title,
                        thumbnailUrl = url,
                        type = mediaType,
                        tags = if (obj.tags == null || obj.tags=="") null else obj.tags,
                        id = obj.id,
                        inFolder = null
                    )
                    setData.add(item)
                }
            }
            return setData
        }


    /*override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<Media>) {

    }


    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Media>) {

    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Media>) {
//
    }

    /*override fun getKey(item: Media): Long {
        return item.id
    }*/*/
}