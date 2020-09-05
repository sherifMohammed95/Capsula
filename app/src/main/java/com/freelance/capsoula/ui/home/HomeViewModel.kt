package com.freelance.capsoula.ui.home

import android.content.Intent
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.freelance.base.BaseResponse
import com.freelance.base.BaseViewModel
import com.freelance.capsoula.R
import com.freelance.capsoula.data.Store
import com.freelance.capsoula.data.repository.GeneralRepository
import com.freelance.capsoula.data.responses.HomeResponse
import com.freelance.capsoula.data.responses.StoresResponse
import com.freelance.capsoula.data.source.local.UserDataSource
import com.freelance.capsoula.ui.checkout.CheckoutActivity
import com.freelance.capsoula.utils.Domain
import com.freelance.capsoula.utils.SingleLiveEvent
import io.intercom.android.sdk.Intercom
import io.intercom.android.sdk.UserAttributes
import io.intercom.android.sdk.identity.Registration
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class HomeViewModel(private val repo: GeneralRepository) : BaseViewModel<HomeNavigator>() {
    var homeDataResponse = SingleLiveEvent<HomeResponse>()
    var storesResponse = SingleLiveEvent<BaseResponse<StoresResponse>>()
    var cartNumberText = ObservableField("")
    var userNameText = ObservableField(Domain.application.getString(R.string.user))
    var cartNumberVisibility = ObservableBoolean(false)
    var emptyCartMessage = SingleLiveEvent<Void>()
    var storesList = ArrayList<Store>()

    init {
        initRepository(repo)
        this.homeDataResponse = repo.homeDataResponse
        this.storesResponse = repo.storesResponse
//        loadHomeData()

        viewModelScope.launch(IO) {
            loginToIntercom()
        }
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

    fun loadStores(showLoading: Boolean) {
        viewModelScope.launch(IO) {
            repo.getStores(showLoading, 1)
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
                UserDataSource.getUser()?.cartContent?.size!! > 0 -> {
                    cartNumberVisibility.set(true)
                    cartNumberText.set(UserDataSource.getUser()?.cartContent?.size!!.toString())
                }
                UserDataSource.getUserCartSize() > 0 -> {
                    cartNumberVisibility.set(true)
                    cartNumberText.set(UserDataSource.getUserCartSize().toString())
                }
                else -> cartNumberVisibility.set(false)
            }
        }
    }


    fun updateToolbarData() {
        if (UserDataSource.getUser() != null)
            userNameText.set(UserDataSource.getUser()?.name)
        updateCartNumber()
    }


    private suspend fun loginToIntercom() {
        if (UserDataSource.getUser() != null) {
            val name = UserDataSource.getUser()?.name
            val email = UserDataSource.getUser()?.email
            val phone = UserDataSource.getUser()?.phone
            val userID = UserDataSource.getUser()?.userId

            val userAttrs = UserAttributes.Builder()
                .withName(name)
                .withEmail(email)
                .withPhone(phone)
                .withUserId(userID.toString())
                .build()

            val registration = Registration.create()
                .withEmail(email!!)
                .withUserId(userID.toString())
                .withUserAttributes(userAttrs)

            Intercom.client().registerIdentifiedUser(registration)
            Intercom.client().updateUser(userAttrs)
        }
    }
}