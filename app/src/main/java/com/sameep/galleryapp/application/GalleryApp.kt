package com.sameep.galleryapp.application

import android.app.Application
import android.content.Context

class GalleryApp : Application() {

    companion object{
        lateinit var app : Application

        val Context.galleryApp: GalleryApp
            get() = applicationContext as GalleryApp
    }

    override fun onCreate() {
        super.onCreate()
        app = this

    }

}