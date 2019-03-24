package com.developerdru.flickflyers.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.developerdru.flickflyers.data.PhotoRepository
import com.developerdru.flickflyers.data.entities.Photo
import com.developerdru.flickflyers.presentation.ViewState
import com.developerdru.flickflyers.presentation.ViewStateLiveData
import io.reactivex.Scheduler


class HomeVM(
    photoRepository: PhotoRepository,
    backgroundScheduler: Scheduler,
    foregroundScheduler: Scheduler
) : ViewModel() {

    private val requestLiveData = MutableLiveData<PhotoRequest>()

    private var photosLiveData: LiveData<ViewState<List<Photo>>>

    init {
        this.photosLiveData = Transformations.switchMap(requestLiveData) {
            val viewStateLiveData = ViewStateLiveData(
                photoRepository,
                backgroundScheduler,
                foregroundScheduler
            )
            viewStateLiveData.setSearchParams(it.searchText, it.page)
            return@switchMap viewStateLiveData
        }
    }

    fun getViewState() = photosLiveData

    fun searchPhotoByTag(searchText: String) {
        requestLiveData.postValue(
            PhotoRequest(searchText, 1)
        )
    }

    private inner class PhotoRequest(var searchText: String, var page: Int)
}