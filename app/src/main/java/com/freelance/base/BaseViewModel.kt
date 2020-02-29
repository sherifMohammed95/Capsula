package com.freelance.base


import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.freelance.capsoula.data.repository.BaseRepository
import com.freelance.capsoula.utils.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import java.lang.ref.WeakReference

abstract class BaseViewModel<N> : ViewModel() {

    private var mNavigator: WeakReference<N>? = null
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val isLoading = ObservableBoolean(false)
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
//        this.isLoading.set(isLoading)
        progressLoading.value = isLoading
    }

    fun initRepository(repository: BaseRepository) {
        this.repository = repository
        errorMessage = repository.apiErrorMessage
        progressLoading = repository.progressLoading
        networkErrorMessage = repository.networkMessageError
        this.showLoadingLayout = repository.showLoadingLayout
    }

    fun getRepoAction(): Action? {
        return repository.mAction
    }
}