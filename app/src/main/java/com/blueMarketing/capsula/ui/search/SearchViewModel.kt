package com.blueMarketing.capsula.ui.search

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.blueMarketing.base.BaseResponse
import com.blueMarketing.base.BaseViewModel
import com.blueMarketing.capsula.data.requests.CartRequest
import com.blueMarketing.capsula.data.Product
import com.blueMarketing.capsula.data.repository.SearchRepository
import com.blueMarketing.capsula.data.responses.ProductsResponse
import com.blueMarketing.capsula.data.source.local.UserDataSource
import com.blueMarketing.capsula.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class SearchViewModel(private val mRepository: SearchRepository) :
    BaseViewModel<SearchNavigator>() {
    var searchResultsResponse = SingleLiveEvent<BaseResponse<ProductsResponse>>()
    var cartResponse = SingleLiveEvent<ArrayList<Product>>()
    var cartNumberText = ObservableField<String>("")
    var cartNumberVisibility = ObservableBoolean(false)
    var emptyCartMessage = SingleLiveEvent<Void>()
    var mProduct = Product()

    var searchResultList = ArrayList<Product>()

    var searchText = ""
    var filterType = -1
    var selectedFilterTypePos = -1
    var pageNo = 1

    init {
        initRepository(mRepository)
        this.searchResultsResponse = mRepository.searchResultsResponse
        this.cartResponse = mRepository.cartResponse
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

    fun addProductToCart() {
        val cart = CartRequest()
        cart.mainId = mProduct.mainId
        cart.quantity = 1
        viewModelScope.launch(IO) {
            mRepository.updateCart(cart)
        }
    }

    fun updateCartNumber() {

        if (UserDataSource.getUser() == null) {
            if (UserDataSource.getUserCartSize() > 0) {
                cartNumberVisibility.set(true)
                cartNumberText.set(UserDataSource.getUserCartSize().toString())
            } else
                cartNumberVisibility.set(false)
        } else {
            when {
                UserDataSource.getUserCartSize() > 0 -> {
                    cartNumberVisibility.set(true)
                    cartNumberText.set(UserDataSource.getUserCartSize().toString())
                }
                UserDataSource.getUser()?.cartContent?.size!! > 0 -> {
                    cartNumberVisibility.set(true)
                    cartNumberText.set(UserDataSource.getUser()?.cartContent?.size!!.toString())
                }
                else -> cartNumberVisibility.set(false)
            }
        }

//        if (UserDataSource.getUserCartSize() > 0) {
//            cartNumberVisibility.set(true)
//            cartNumberText.set(UserDataSource.getUserCartSize().toString())
//        } else
//            cartNumberVisibility.set(false)
    }

    fun checoutAction() {
        if (UserDataSource.getUser() == null) {
            if (UserDataSource.getUserCartSize() > 0)
                navigator?.openCheckout()
            else
                emptyCartMessage.call()
        } else {
            when {
                UserDataSource.getUserCartSize() > 0 ->
                    navigator?.openCheckout()
                UserDataSource.getUser()?.cartContent?.size!! > 0 ->
                    navigator?.openCheckout()
                else -> emptyCartMessage.call()
            }
        }
    }
}