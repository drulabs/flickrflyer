package com.developerdru.flickflyers.data.remote

import com.developerdru.flickflyers.data.entities.PhotoResponseWrapper
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrService {

    @GET("rest/?method=flickr.photos.search&format=json&nojsoncallback=1")
    fun getPhotos(
        @Query("tags") searchText: String,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String
    ): Observable<PhotoResponseWrapper>

}