package com.sameep.galleryapp.singletons

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager

class Util() {

    companion object {
        private var GLIDEREF:RequestManager?=null
        fun getGlide(context: Context): RequestManager = GLIDEREF?:Glide.with(context.applicationContext)
    }

}