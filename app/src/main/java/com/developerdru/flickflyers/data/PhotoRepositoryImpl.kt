package com.developerdru.flickflyers.data

import android.content.Context
import android.content.SharedPreferences
import com.developerdru.flickflyers.BuildConfig
import com.developerdru.flickflyers.data.entities.Photo
import com.developerdru.flickflyers.data.local.PhotosDAO
import com.developerdru.flickflyers.data.remote.FlickrService
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(
    context: Context,
    photosDAO: PhotosDAO,
    flickrService: FlickrService
) : PhotoRepository {

    private val localSource: PhotosDAO = photosDAO
    private val remoteSource: FlickrService = flickrService

    private val prefs: SharedPreferences =
        context.getSharedPreferences("local_prefs", Context.MODE_PRIVATE)

    private var disposable: Disposable? = null

    override fun getPhotos(searchText: String, page: Int): Observable<List<Photo>> {

        // Dispose of any active operations
        disposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }

        var queryText: String
        var resultPage: Int

        if (searchText.isBlank() || page <= 0) {
            prefs.let {
                queryText = it.getString("searchText", "sunrise")!!
                resultPage = it.getInt("page", 1)
            }
        } else {
            queryText = searchText
            resultPage = page

            prefs.edit().let {
                it.putString("searchText", queryText)
                it.putInt("page", resultPage)
                it.apply()
            }
        }

        // Get data from API and save it locally
        disposable = remoteSource.getPhotos(queryText, resultPage, BuildConfig.API_KEY)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .flatMap { Observable.just(it.photoResponse) }
            .flatMap { Observable.just(it.photos) }
            .subscribe({ pics ->
                localSource.savePhotos(pics.map {
                    it.tag = queryText
                    return@map it
                })
            }, {
                println("ERROR: $it")
            })

        // return the live observable from local data source
        return localSource.getPhotosByTag(searchText)
    }

    override fun clearCache(): Completable {
        return Completable.fromAction {
            localSource.clearCache()
        }
    }
}