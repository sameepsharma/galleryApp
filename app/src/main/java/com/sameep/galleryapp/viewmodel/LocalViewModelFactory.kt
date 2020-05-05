package com.sameep.galleryapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sameep.galleryapp.enums.MediaType

class LocalViewModelFactory(private val app: Application, private val mediaType:MediaType) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        modelClass.getConstructor(Application::class.java, MediaType::class.java)
            .newInstance(app, mediaType)


}