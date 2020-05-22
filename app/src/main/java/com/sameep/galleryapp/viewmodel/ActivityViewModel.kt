package com.sameep.galleryapp.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sameep.galleryapp.dataclasses.Folder
import com.sameep.galleryapp.dataclasses.Media
import com.sameep.galleryapp.db.MediaDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ActivityViewModel(application: Application) : AndroidViewModel(application) {

    private var mode = MutableLiveData<Boolean>(false)
    val mediaDao=MediaDatabase.getDataBase(application,viewModelScope).mediaDao()
    private val selectedMediaList = mutableListOf<Media>()
    var allFolders: LiveData<List<Folder>>
    init {
        Log.e("MainViewModel>>>", "YEs <<<")
        allFolders=mediaDao.getAllFolders()
    }

    fun addToSharedList(media: Media) {
        selectedMediaList.add(media)
    }

    fun deleteFromSharedList(media: Media) {
        selectedMediaList.remove(media)
    }

    fun getSharedList() = selectedMediaList

    fun insertIntoFolder(folder: Folder) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val key = mediaDao.insertIntoFolder(folder)
            }
        }
    }

    fun clearSelectedList(){
        selectedMediaList.clear()
    }

    fun insertMediaList(list : MutableList<Media>){
        viewModelScope.launch(Dispatchers.IO) {
            val listToInsert = mutableListOf<Media>()
            val insertListIterator = listToInsert.listIterator()
            var iterator = list.iterator()
            while (iterator.hasNext()){
                val j = iterator.next()
                val savedList = mediaDao.checkIfExists(j.id, j.inFolder)
                Log.e("SizeChecking", "${savedList.size} >>>")
                if (savedList.isEmpty())
                    insertListIterator.add(j)
            }
//
            val i = mediaDao.insertMedia(listToInsert)
        }
    }

    fun observeActionMode() = mode
    fun inSelectionMode(inSelection: Boolean){
        mode.postValue(inSelection)
    }

}