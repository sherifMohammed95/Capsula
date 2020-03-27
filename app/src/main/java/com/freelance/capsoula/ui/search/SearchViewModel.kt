package com.freelance.capsoula.ui.search

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.freelance.base.BaseResponse
import com.freelance.base.BaseViewModel
import com.freelance.capsoula.data.repository.SearchRepository
import com.freelance.capsoula.data.responses.ProductsResponse
import com.freelance.capsoula.data.source.local.UserDataSource
import com.freelance.capsoula.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class SearchViewModel(private val mRepository: SearchRepository) :
    BaseViewModel<SearchNavigator>() {
    var searchResultsResponse = SingleLiveEvent<BaseResponse<ProductsResponse>>()
    var cartNumberText = ObservableField<String>("")
    var cartNumberVisibility = ObservableBoolean(false)

    var searchText = ""
    var filterType = -1
    var selectedFilterTypePos = -1
    var pageNo = 1

    init {
        initRepository(mRepository)
        this.searchResultsResponse = mRepository.searchResultsResponse
        getSearchResults()
    }

    fun openFilterList() {
        navigator!!.openFilterList()
    }

    fun getSearchResults() {
        viewModelScope.launch(IO) {
            mRepository.getSearchResults(searchText, filterType, pageNo)
        }
    }

    fun updateCartNumber() {
        if (UserDataSource.getUserCartSize() > 0) {
            cartNumberVisibility.set(true)
            cartNumberText.set(UserDataSource.getUserCartSize().toString())
        } else
            cartNumberVisibility.set(false)
    }
}