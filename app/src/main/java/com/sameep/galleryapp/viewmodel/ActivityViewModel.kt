package com.sameep.galleryapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sameep.galleryapp.dataclasses.Media

class ActivityViewModel() : ViewModel() {

    private val selectedMediaList = mutableListOf<Media>()

    fun addToSharedList(media: Media) {
        selectedMediaList.add(media)
    }

    fun deleteFromSharedList(media: Media) {
        selectedMediaList.remove(media)
    }

    fun getSharedList() = selectedMediaList
}