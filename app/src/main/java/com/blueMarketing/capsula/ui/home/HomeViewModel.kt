package com.blueMarketing.capsula.ui.home

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.blueMarketing.base.BaseResponse
import com.blueMarketing.base.BaseViewModel
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.data.Store
import com.blueMarketing.capsula.data.repository.GeneralRepository
import com.blueMarketing.capsula.data.responses.HomeResponse
import com.blueMarketing.capsula.data.responses.StoresResponse
import com.blueMarketing.capsula.data.source.local.UserDataSource
import com.blueMarketing.capsula.utils.Domain
import com.blueMarketing.capsula.utils.SingleLiveEvent
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
    var notificationsIconVisibility = ObservableBoolean(false)
    var emptyCartMessage = SingleLiveEvent<Void>()
    var storesList = ArrayList<Store>()
    var pageNo = 1

    init {
        initRepository(repo)
        this.homeDataResponse = repo.homeDataResponse
        this.storesResponse = repo.storesResponse
        loadStores(true)
        if (UserDataSource.getUser() != null)
            refreshDevice()

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
            repo.getStores(showLoading, pageNo)
        }
    }

    fun loadUserData() {
        viewModelScope.launch(IO) {
            repo.getUserData()
        }
    }

   private fun refreshDevice() {
        viewModelScope.launch(IO) {
            repo.refreshDevice()
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