package com.sameep.galleryapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sameep.galleryapp.enums.MediaType
import com.sameep.galleryapp.enums.Source
import com.sameep.galleryapp.rest.ApiInterface

class LocalViewModelFactory(
    private val app: Application,
    private val mediaType: MediaType,
    private val apiClient: ApiInterface,
    private val source: Source
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        modelClass.getConstructor(Application::class.java, MediaType::class.java, ApiInterface::class.java, Source::class.java)
            .newInstance(app, mediaType, apiClient, source)


}