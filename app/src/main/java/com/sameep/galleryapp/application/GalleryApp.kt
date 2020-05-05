package com.sameep.galleryapp.application

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDexApplication

class GalleryApp : MultiDexApplication() {

    companion object{
        lateinit var app : GalleryApp
        private set

    }

    override fun onCreate() {
        super.onCreate()
        app = this

    }

}