package com.sameep.galleryapp.rest

import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {

    /*@POST("https://www.googleapis.com/oauth2/v4/token")
    suspend fun getAuthToken(@Field("grant_type") grantType : String, @Field("client_id") clientId: String
                             , @Field("client_secret") secret : String, @Field("redirect_uri") redirect : String
                             , @Field(""))*/

    @GET("mediaItems/")
    suspend fun getAllMedia(@Header("Authorization") Authorization: String?) : Response<String>

}