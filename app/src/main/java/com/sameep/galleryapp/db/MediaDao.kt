package com.sameep.galleryapp.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.sameep.galleryapp.dataclasses.Folder
import com.sameep.galleryapp.dataclasses.Media

@Dao
interface MediaDao {

    @Insert
    suspend fun insertMedia(mediaList: List<Media>):List<Long>

    @Insert(entity = Folder::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIntoFolder(folder: Folder): Long

    @Query("SELECT * FROM folders_table")
    fun getAllFolders(): LiveData<List<Folder>>

    @Query("SELECT * FROM media_table")
    fun getSavedMedia() : LiveData<List<Media>>

    /*@Query("SELECT * FROM folders_table")
    suspend fun getAllFiles():List<Media>*/

    @Query("DELETE FROM folders_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM media_table WHERE inFolder = :name")
    fun getSavedMediaByFolder(name: String): LiveData<List<Media>>
}