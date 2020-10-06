package com.blueMarketing.base


import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blueMarketing.capsula.data.repository.BaseRepository
import com.blueMarketing.capsula.utils.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import java.lang.ref.WeakReference

abstract class BaseViewModel<N> : ViewModel() {

    private var mNavigator: WeakReference<N>? = null
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    var isLastPage = false
    var isPagingLoadingEvent:SingleLiveEvent<Boolean> = SingleLiveEvent()
    val isLoading = ObservableBoolean(false)
    val isPagingLoading = ObservableBoolean(false)
    var progressLoading: SingleLiveEvent<Boolean> = SingleLiveEvent()
    var showLoadingLayout: SingleLiveEvent<Boolean> = SingleLiveEvent()
    var errorMessage: SingleLiveEvent<String> = SingleLiveEvent()
    var networkErrorMessage: SingleLiveEvent<String> = SingleLiveEvent()
    var repository = BaseRepository()


    var navigator: N?
        get() = mNavigator!!.get()
        set(navigator) {
            this.mNavigator = WeakReference<N>(navigator)
        }

    override fun onCleared() {
        compositeDisposable.dispose()
        if (viewModelScope.isActive) {
            viewModelScope.cancel()
        }
        super.onCleared()
    }

    fun setIsLoading(isLoading: Boolean) {
        progressLoading.value = isLoading
    }

    fun initRepository(repository: BaseRepository) {
        this.repository = repository
        errorMessage = repository.apiErrorMessage
        progressLoading = repository.progressLoading
        isPagingLoadingEvent = repository.isPagingLoadingEvent
        networkErrorMessage = repository.networkMessageError
        this.showLoadingLayout = repository.showLoadingLayout
    }

    fun getRepoAction(): Action? {
        return repository.mAction
    }
}