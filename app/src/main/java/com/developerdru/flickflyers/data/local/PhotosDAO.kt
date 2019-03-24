package com.developerdru.flickflyers.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.developerdru.flickflyers.data.entities.Photo
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface PhotosDAO {

    @Query("SELECT * FROM flickr_pics WHERE tag = :tag")
    fun getPhotosByTag(tag: String): Observable<List<Photo>>

    @Query("SELECT * FROM flickr_pics WHERE photo_id = :id")
    fun getPhotosById(id: String): Observable<Photo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savePhotos(photoList: List<Photo>)

    @Query("DELETE FROM flickr_pics")
    fun clearCache()

}
