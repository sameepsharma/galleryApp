package com.sameep.galleryapp.singletons

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitProvider() {


    companion object {
        private val BASE_URL = "https://api.flickr.com/services/rest/"

        private var RETROFITREF:Retrofit?=null
        private val myOkHttpClient = OkHttpClient.Builder().build()
        fun getRetrofit() : Retrofit = RETROFITREF?: Retrofit.Builder().client(myOkHttpClient).baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            //.addCallAdapterFactory(CoroutinAda)
            .build()

    }

}