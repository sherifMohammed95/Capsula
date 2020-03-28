package com.freelance.capsoula.ui.search

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.freelance.base.BaseResponse
import com.freelance.base.BaseViewModel
import com.freelance.capsoula.data.Cart
import com.freelance.capsoula.data.Product
import com.freelance.capsoula.data.repository.CartRepository
import com.freelance.capsoula.data.repository.SearchRepository
import com.freelance.capsoula.data.responses.ProductsResponse
import com.freelance.capsoula.data.source.local.UserDataSource
import com.freelance.capsoula.utils.SingleLiveEvent
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
        val cart = Cart()
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