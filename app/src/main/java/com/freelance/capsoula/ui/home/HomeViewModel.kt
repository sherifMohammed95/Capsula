package com.freelance.capsoula.ui.home

import android.content.Intent
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.freelance.base.BaseViewModel
import com.freelance.capsoula.R
import com.freelance.capsoula.data.repository.GeneralRepository
import com.freelance.capsoula.data.responses.HomeResponse
import com.freelance.capsoula.data.source.local.UserDataSource
import com.freelance.capsoula.ui.checkout.CheckoutActivity
import com.freelance.capsoula.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class HomeViewModel(private val repo: GeneralRepository) : BaseViewModel<HomeNavigator>() {
    var homeDataResponse = SingleLiveEvent<HomeResponse>()
    var cartNumberText = ObservableField<String>("")
    var userNameText = ObservableField<String>("User")
    var cartNumberVisibility = ObservableBoolean(false)
    var emptyCartMessage = SingleLiveEvent<Void>()

    init {
        initRepository(repo)
        this.homeDataResponse = repo.homeDataResponse
        loadHomeData()
    }

    fun cartAction() {
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

    fun openSearch() {
        navigator?.openSearch()
    }

    fun openAllBrands() {
        navigator?.openAllBrands()
    }

    fun openAllCategories() {
        navigator?.openAllCategories()
    }

    fun openAllStores() {
        navigator?.openAllStores()
    }

    fun openTopRated() {
        navigator?.openTopRated()
    }

    fun openBestSeller() {
        navigator?.openBestSeller()
    }

    fun openFreeDelivery() {
        navigator?.openFreeDelivery()
    }

    fun openMore() {
        navigator?.openMore()
    }

    private fun loadHomeData() {
        viewModelScope.launch(IO) {
            repo.getHomeData()
        }
    }

    fun loadUpdatedCart() {
        viewModelScope.launch(IO) {
            repo.getUpdatedCart()
        }
    }

    private fun updateCartNumber() {

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


    fun updateToolbarData() {
        if (UserDataSource.getUser() != null)
            userNameText.set(UserDataSource.getUser()?.name)
        updateCartNumber()
    }
}