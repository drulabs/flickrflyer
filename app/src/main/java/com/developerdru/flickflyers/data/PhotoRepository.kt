package com.developerdru.flickflyers.data

import com.developerdru.flickflyers.data.entities.Photo
import io.reactivex.Completable
import io.reactivex.Observable


interface PhotoRepository {

    fun getPhotos(searchText: String, page: Int): Observable<List<Photo>>

    fun clearCache(): Completable

}