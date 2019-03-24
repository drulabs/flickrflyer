package com.developerdru.flickflyers.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.developerdru.flickflyers.data.PhotoRepository
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

@Suppress("UNCHECKED_CAST")
class HomeVMFactory @Inject constructor(
    private val photoRepository: PhotoRepository,
    @Named("background") private val backgroundScheduler: Scheduler,
    @Named("foreground") private val foregroundScheduler: Scheduler
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeVM(photoRepository, backgroundScheduler, foregroundScheduler) as T
    }
}