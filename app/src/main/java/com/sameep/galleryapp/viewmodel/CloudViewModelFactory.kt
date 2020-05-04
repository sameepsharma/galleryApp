package com.sameep.galleryapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sameep.galleryapp.rest.ApiInterface

class CloudViewModelFactory(private val apiClient: ApiInterface, private val mediaType:Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        modelClass.getConstructor(ApiInterface::class.java, Int::class.java)
            .newInstance(apiClient, mediaType)


}