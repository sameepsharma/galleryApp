package com.sameep.galleryapp.rest

import androidx.paging.DataSource
import com.sameep.galleryapp.dataclasses.FlickrResp
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface {


    companion object{
        const val DEFAULT_QUERY = "Cars"
        val key: String by lazy {
            "8b766ab7b7e827c11516eb191be5f8a1"
        }
    }

    /*@POST("https://www.googleapis.com/oauth2/v4/token")
    suspend fun getAuthToken(@Field("grant_type") grantType : String, @Field("client_id") clientId: String
                             , @Field("client_secret") secret : String, @Field("redirect_uri") redirect : String
                             , @Field(""))*/

    @POST("?format=json&nojsoncallback=1&method=flickr.photos.getRecent&per_page=10&extras=media,tags")
    suspend fun getMediaFromFlickr(@Query("api_key") api_key:String) : Response<FlickrResp>

    @POST("?format=json&nojsoncallback=1&method=flickr.photos.search&media=photos&extras=media,tags")
    suspend fun getImagesForQuery(@Query("api_key") api_key:String, @Query("text") text :String
                                  , @Query("page") page : Int=1, @Query("per_page") per_page : Int=30) : Response<FlickrResp>


    @POST("?format=json&nojsoncallback=1&method=flickr.photos.search&media=video&extras=media,url_z,tags")
    suspend fun getVideoSearchResult(@Query("api_key") api_key:String, @Query("text") text :String, @Query("page") page : Int=1
                                     , @Query("per_page") per_page : Int=30) : Response<FlickrResp>

}