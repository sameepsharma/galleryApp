package com.sameep.galleryapp.rest

import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface {

    /*@POST("https://www.googleapis.com/oauth2/v4/token")
    suspend fun getAuthToken(@Field("grant_type") grantType : String, @Field("client_id") clientId: String
                             , @Field("client_secret") secret : String, @Field("redirect_uri") redirect : String
                             , @Field(""))*/

    @POST("?format=json&nojsoncallback=1&method=flickr.photos.getRecent&per_page=500&extras=media")

    suspend fun getMediaFromFlickr(@Query("api_key") api_key:String) : Response<JsonObject>

}