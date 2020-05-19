package com.sameep.galleryapp.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.sameep.galleryapp.dataclasses.Media
import com.sameep.galleryapp.db.MediaDatabase

class CustomActivityViewModel(appContext : Application, folderName : String):AndroidViewModel(appContext) {

    val dao = MediaDatabase.getDataBase(appContext, viewModelScope).mediaDao()
    private var savedMedia : LiveData<List<Media>>

    init {
        savedMedia=dao.getSavedMediaByFolder(folderName)
    }

    fun observeMedia()=savedMedia

}