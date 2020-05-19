package com.sameep.galleryapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.sameep.galleryapp.dataclasses.Folder
import com.sameep.galleryapp.dataclasses.Media
import com.sameep.galleryapp.db.MediaDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(val appContext: Application) : AndroidViewModel(appContext) {

    lateinit var allFolders: LiveData<List<Folder>>
    private val mediaDao = MediaDatabase.getDataBase(appContext, viewModelScope).mediaDao()

    init {
        Log.e("MainViewModel>>>", "YEs <<<")
        allFolders=mediaDao.getAllFolders()
        /*viewModelScope.launch {
            withContext(Dispatchers.IO) {
                setOnMainThread(mediaDao.getAllFolders())
            }
        }*/
    }

    fun insertMediaList(list : MutableList<Media>){
        viewModelScope.launch(Dispatchers.IO) {

            val i = mediaDao.insertMedia(list)
            Log.e("InsertListKey>>", "${i[0]} <<<")
        }
    }

    private suspend fun setOnMainThread(dbFolders: LiveData<List<Folder>>) {
        withContext(Dispatchers.Main) {
            allFolders = dbFolders
            Log.e(" MAinFolderList>>", "${allFolders.value?.size} <<<")
        }
    }

    fun insertIntoFolder(folder: Folder) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val key = mediaDao.insertIntoFolder(folder)
            }
        }
    }

}