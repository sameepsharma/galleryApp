package com.sameep.galleryapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sameep.galleryapp.rest.ApiInterface

class CloudViewModelFactory(private val apiClient: ApiInterface, private val isImage:Boolean) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        modelClass.getConstructor(ApiInterface::class.java, Boolean::class.java)
            .newInstance(apiClient, isImage)


}