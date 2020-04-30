package com.sameep.galleryapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sameep.galleryapp.rest.ApiInterface

class LocalViewModelFactory(private val app: Application, private val isImage:Boolean) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        modelClass.getConstructor(Application::class.java, Boolean::class.java)
            .newInstance(app, isImage)


}