package com.developerdru.flickflyers.presentation

import android.os.Handler
import androidx.lifecycle.LiveData
import com.developerdru.flickflyers.data.PhotoRepository
import com.developerdru.flickflyers.data.entities.Photo
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

class ViewStateLiveData(
    private val repository: PhotoRepository,
    private val backgroundScheduler: Scheduler,
    private val foregroundScheduler: Scheduler
) : LiveData<ViewState<List<Photo>>>() {

    companion object {
        const val DEFAULT_SEARCH_TERM = "sunrise"
        const val DEFAULT_PAGE_NUM = 1
        const val DELAY_FOR_DISPOSE: Long = 2000
    }

    private var isDisposePending = false
    private val disposerHandler = Handler()

    private val disposer = Runnable {
        if (!disposables.isDisposed) {
            disposables.dispose()
        }
        isDisposePending = false
    }

    //private lateinit var viewState: ViewState<List<Photo>>
    private lateinit var viewType: ViewType

    private val disposables = CompositeDisposable()

    private var disposableObserver = DisposableRxObserver()

    var photos = repository.getPhotos(DEFAULT_SEARCH_TERM, DEFAULT_PAGE_NUM)

    fun setSearchParams(searchText: String, page: Int, viewType: ViewType = ViewType.LIST) {
        this.viewType = viewType
        photos = repository.getPhotos(searchText, page)
    }

    override fun onInactive() {
        disposerHandler.postDelayed(disposer, DELAY_FOR_DISPOSE)
        isDisposePending = true
    }

    override fun onActive() {
        if (isDisposePending) {
            disposerHandler.removeCallbacks(disposer)
        } else {
            updateData(ViewState(null, true, null))
            if (disposableObserver.isDisposed) {
                disposableObserver = DisposableRxObserver()
            }
            disposables.add(
                photos.subscribeOn(backgroundScheduler)
                    .observeOn(foregroundScheduler)
                    .subscribeWith(disposableObserver)
            )
        }
    }

    private inner class DisposableRxObserver : DisposableObserver<List<Photo>>() {
        override fun onComplete() {}

        override fun onNext(pics: List<Photo>) {
            updateData(ViewState(pics, false, null))
        }

        override fun onError(e: Throwable) {
            updateData(ViewState(null, false, e))
        }
    }

    private fun updateData(viewState: ViewState<List<Photo>>) {
        viewState.viewType = viewType
        postValue(viewState)
    }

}