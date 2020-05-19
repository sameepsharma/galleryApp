package com.sameep.galleryapp.db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sameep.galleryapp.dataclasses.Folder
import com.sameep.galleryapp.dataclasses.Media
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Folder::class, Media::class], exportSchema = false, version = 1)
@TypeConverters(MediaConverter::class)
abstract class MediaDatabase : RoomDatabase() {

    companion object {
        private const val dbName = "media_db"
        private var DBINSTANCE: MediaDatabase? = null
        fun getDataBase(context: Context, scope: CoroutineScope) =
            DBINSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context.applicationContext, MediaDatabase::class.java, dbName)
                    .fallbackToDestructiveMigration()
                    //.addCallback(MediaDatabaseCallback(scope))
                    .build()
            }
    }

    abstract fun mediaDao(): MediaDao

    private class MediaDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            DBINSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateDatabase(database.mediaDao())
                }
            }
        }

        suspend fun populateDatabase(mediaDao: MediaDao) {
            // Not needed if you only populate on creation.

            var folder =
                Folder("Default", System.currentTimeMillis(), null, System.currentTimeMillis())
            mediaDao.insertIntoFolder(folder)
            folder = Folder("General", System.currentTimeMillis(), null, System.currentTimeMillis())
            mediaDao.insertIntoFolder(folder)
            Log.e("Poulating>>", "DONE <<<")
        }
    }

}