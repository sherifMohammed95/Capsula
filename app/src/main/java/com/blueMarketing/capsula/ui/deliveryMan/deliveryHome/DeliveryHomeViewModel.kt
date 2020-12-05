package com.blueMarketing.capsula.ui.deliveryMan.deliveryHome

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.blueMarketing.base.BaseViewModel
import com.blueMarketing.capsula.data.repository.GeneralRepository
import com.blueMarketing.capsula.data.responses.DeliveryOrdersResponse
import com.blueMarketing.capsula.data.source.local.UserDataSource
import com.blueMarketing.capsula.utils.SingleLiveEvent
import io.intercom.android.sdk.Intercom
import io.intercom.android.sdk.UserAttributes
import io.intercom.android.sdk.identity.Registration
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class DeliveryHomeViewModel(private val mRepo: GeneralRepository) :
    BaseViewModel<DeliveryHomeNavigator>() {

    var userNameText = ObservableField(UserDataSource.getDeliveryUser()?.fullName)
    var deliveryHomeDataResponse = SingleLiveEvent<DeliveryOrdersResponse>()
    var id = 0

    init {
        initRepository(mRepo)
        this.deliveryHomeDataResponse = mRepo.deliveryHomeDataResponse
        getHomeData(true)

        if (UserDataSource.getDeliveryUser() != null)
            refreshDevice()

        viewModelScope.launch(IO) {
            loginToIntercom()
        }
    }

    private fun loginToIntercom() {
        if (UserDataSource.getDeliveryUser() != null) {
            val name = UserDataSource.getDeliveryUser()?.fullName
            val email = UserDataSource.getDeliveryUser()?.email
            val phone = UserDataSource.getDeliveryUser()?.phoneNumber
            val userID = UserDataSource.getDeliveryUser()?.nationalId

            val userAttrs = UserAttributes.Builder()
                .withName(name)
                .withEmail(email)
                .withPhone(phone)
                .withUserId(userID.toString())
                .withCustomAttribute("type","Delivery man")
                .build()

            val registration = Registration.create()
                .withEmail(email!!)
                .withUserId(userID.toString())
                .withUserAttributes(userAttrs)

            Intercom.client().registerIdentifiedUser(registration)
            Intercom.client().updateUser(userAttrs)
        }
    }

    private fun refreshDevice() {
        viewModelScope.launch(IO) {
            mRepo.refreshDevice()
        }
    }

    fun getHomeData(showLoading: Boolean) {
        viewModelScope.launch(IO) {
            mRepo.getDeliveryHomeData(showLoading)
        }
    }

}