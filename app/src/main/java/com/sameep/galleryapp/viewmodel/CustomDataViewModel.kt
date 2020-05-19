package com.sameep.galleryapp.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sameep.galleryapp.dataclasses.Folder
import com.sameep.galleryapp.db.MediaDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CustomDataViewModel(val appContext: Application) : AndroidViewModel(appContext) {
    val mediaDao = MediaDatabase.getDataBase(appContext, viewModelScope).mediaDao()
    lateinit var folderList: LiveData<List<Folder>>

    init {
        viewModelScope.launch {

            folderList = mediaDao.getAllFolders()
            Log.e("SizeFromDb>>", "${folderList.value?.size} <<<")
        }
    }
}