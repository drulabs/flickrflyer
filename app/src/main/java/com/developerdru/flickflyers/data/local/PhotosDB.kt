package com.developerdru.flickflyers.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.developerdru.flickflyers.data.entities.Photo

@Database(entities = [Photo::class], version = 1, exportSchema = false)
abstract class PhotosDB : RoomDatabase() {

    abstract fun getPhotosDAO(): PhotosDAO

    companion object {

        private var INSTANCE: PhotosDB? = null

        private val lock = Any()

        fun getInstance(context: Context): PhotosDB {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        PhotosDB::class.java, "photos.db"
                    ).build()
                }
                return INSTANCE!!
            }
        }
    }
}