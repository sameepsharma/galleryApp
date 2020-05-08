package com.sameep.galleryapp.datasource

import android.util.Log
import androidx.paging.ItemKeyedDataSource
import com.sameep.galleryapp.dataclasses.FlickrResp
import com.sameep.galleryapp.dataclasses.Media
import com.sameep.galleryapp.enums.MediaType
import com.sameep.galleryapp.rest.ApiInterface
import com.sameep.galleryapp.singletons.RetrofitProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MediaDataSource(private val query:String, private val scope:CoroutineScope, private val mediaType: MediaType): ItemKeyedDataSource<Long, Media>() {

    //the size of a page that we want
    val PAGE_SIZE = 50

    //we will start from the first page which is 1
    private var PAGE = 1

    override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<Media>) {
        scope.launch {
            Log.e(
                "ProcessingQuery>>>", query
            )
            val response = RetrofitProvider.getRetrofit().create(ApiInterface::class.java).getImagesForQuery(ApiInterface.key, query, PAGE, PAGE_SIZE)
            if (response.isSuccessful){
                response?.let { it1 ->
                    if (it1.isSuccessful) {
                        PAGE++
                        val respBody = it1.body()
                        val listOfMedia = getListFromResponse(respBody)

                        callback.onResult(listOfMedia)

                    } else {
                        val code = response.code()
                        val errorBody = response.errorBody()?.string()
                        Log.e("CodeAndError>>> ", "$code >> $errorBody")
                    }

                }
            }else{
                Log.e("ErrorImagesQuery>>",response.errorBody()?.string()+" <<<")
            }
        }
    }

    private fun getListFromResponse(respBody: FlickrResp?): List<Media> {

        val setData = mutableListOf<Media>()
        respBody?.photos?.let {

            for (i in 0 until it.photo.size - 1) {
                val obj = it.photo[i]

                val url = when (mediaType) {
                    MediaType.IMAGE -> "https://farm${obj.farm}.staticflickr.com/${obj.server}/${obj.id}_${obj.secret}_z.jpg"
                    MediaType.VIDEO -> obj.url_z
                }
                val item = Media(
                    name = obj.title,
                    thumbnailUrl = url,
                    type = mediaType,
                    tags = if (obj.tags == null || obj.tags=="") null else obj.tags,
                    id = obj.id
                )
                setData.add(item)
            }
        }
        return setData
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Media>) {
        Log.e("AfterCallled<<<", "YES<<<")
        scope.launch {
            val response = RetrofitProvider.getRetrofit().create(ApiInterface::class.java).getImagesForQuery(ApiInterface.key, query, PAGE, PAGE_SIZE)
            if (response.isSuccessful){
                response?.let { it1 ->
                    if (it1.isSuccessful) {
                        PAGE++
                        val respBody = it1.body()

                        val listOfMedia = getListFromResponse(respBody)
                        callback.onResult(listOfMedia)

                    } else {
                        val code = response.code()
                        val errorBody = response.errorBody()?.string()
                        Log.e("CodeAndError>>> ", "$code >> $errorBody")
                    }

                }
            }else{
                Log.e("ErrorImagesQuery>>",response.errorBody()?.string()+" <<<")
            }
        }
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Media>) {
//
    }

    override fun getKey(item: Media): Long {
        return item.id
    }
}