package com.sameep.galleryapp.application

import android.app.Application
import android.content.Context

class  GalleryApp : Application() {

    companion object{
        lateinit var app : GalleryApp
        private set

    }

    override fun onCreate() {
        super.onCreate()
        app = this

    }

}