package com.developerdru.flickflyers.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.developerdru.flickflyers.data.PhotoRepository
import com.developerdru.flickflyers.data.entities.Photo
import com.developerdru.flickflyers.presentation.ViewState
import com.developerdru.flickflyers.presentation.ViewStateLiveData
import com.developerdru.flickflyers.presentation.ViewType
import io.reactivex.Scheduler


class HomeVM(
    photoRepository: PhotoRepository,
    backgroundScheduler: Scheduler,
    foregroundScheduler: Scheduler
) : ViewModel() {

    private val requestLiveData = MutableLiveData<PhotoRequest>()

    private var photosLiveData: LiveData<ViewState<List<Photo>>>

    private lateinit var photoSearchRequest: PhotoRequest

    init {
        this.photosLiveData = Transformations.switchMap(requestLiveData) {
            val viewStateLiveData = ViewStateLiveData(
                photoRepository,
                backgroundScheduler,
                foregroundScheduler
            )
            viewStateLiveData.setSearchParams(it.searchText, it.page, it.viewType)
            return@switchMap viewStateLiveData
        }
    }

    fun getViewState() = photosLiveData

    fun searchPhotoByTag(searchText: String) {
        photoSearchRequest = PhotoRequest(searchText, 1)
        requestLiveData.postValue(photoSearchRequest)
    }

    fun toggleView() {
        photoSearchRequest.viewType = when(photoSearchRequest.viewType) {
            ViewType.LIST -> ViewType.GRID
            ViewType.GRID -> ViewType.LIST
        }
        requestLiveData.postValue(photoSearchRequest)
    }

    private inner class PhotoRequest(
        var searchText: String,
        var page: Int,
        var viewType: ViewType = ViewType.LIST)
}