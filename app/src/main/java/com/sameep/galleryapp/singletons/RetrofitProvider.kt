package com.sameep.galleryapp.singletons

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitProvider() {

    companion object {
        private var RETROFITREF:Retrofit?=null
        private val myOkHttpClient = OkHttpClient.Builder().build()
        fun getRetrofit() : Retrofit = RETROFITREF?: Retrofit.Builder().client(myOkHttpClient).baseUrl("https://api.flickr.com/services/rest/")
            .addConverterFactory(GsonConverterFactory.create())
            //.addCallAdapterFactory(CoroutinAda)
            .build()

    }

}