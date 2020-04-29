package com.sameep.galleryapp.rest

import com.google.gson.JsonObject
import com.sameep.galleryapp.dataclasses.FlickrResp
import com.sameep.galleryapp.dataclasses.Photos
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface {

    /*@POST("https://www.googleapis.com/oauth2/v4/token")
    suspend fun getAuthToken(@Field("grant_type") grantType : String, @Field("client_id") clientId: String
                             , @Field("client_secret") secret : String, @Field("redirect_uri") redirect : String
                             , @Field(""))*/

    @POST("?format=json&nojsoncallback=1&method=flickr.photos.getRecent&per_page=10&extras=media")
    suspend fun getMediaFromFlickr(@Query("api_key") api_key:String) : Response<FlickrResp>

    @POST("?format=json&nojsoncallback=1&method=flickr.photos.search&media=video&per_page=10&extras=media,url_z")
    suspend fun getVideoSearchResult(@Query("api_key") api_key:String) : Response<FlickrResp>

}