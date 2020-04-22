package com.sameep.galleryapp.singletons

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class InstanceProvider() {

    companion object {
        private var GLIDEREF:RequestManager?=null
        fun getGlide(context: Context): RequestManager = GLIDEREF?:Glide.with(context.applicationContext)

        private var RETROFITREF:Retrofit?=null
        private val myOkHttpClient = OkHttpClient.Builder().build()
        fun getRetrofit() : Retrofit = RETROFITREF?: Retrofit.Builder().client(myOkHttpClient).baseUrl("https://photoslibrary.googleapis.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            //.addCallAdapterFactory(CoroutinAda)
            .build()

    }

}