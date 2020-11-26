package com.blueMarketing.capsula.ui.deliveryMan.deliveryHome

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.blueMarketing.base.BaseViewModel
import com.blueMarketing.capsula.data.repository.GeneralRepository
import com.blueMarketing.capsula.data.responses.DeliveryOrdersResponse
import com.blueMarketing.capsula.data.source.local.UserDataSource
import com.blueMarketing.capsula.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class DeliveryHomeViewModel(private val mRepo: GeneralRepository) :
    BaseViewModel<DeliveryHomeNavigator>() {

    var userNameText = ObservableField(UserDataSource.getDeliveryUser()?.fullName)
    var deliveryHomeDataResponse = SingleLiveEvent<DeliveryOrdersResponse>()

    init {
        initRepository(mRepo)
        this.deliveryHomeDataResponse = mRepo.deliveryHomeDataResponse
        getHomeData(true)

        if (UserDataSource.getDeliveryUser() != null)
            refreshDevice()
    }

    private fun refreshDevice() {
        viewModelScope.launch(IO) {
            mRepo.refreshDevice()
        }
    }

    fun getHomeData(showLoading:Boolean) {
        viewModelScope.launch(IO) {
            mRepo.getDeliveryHomeData(showLoading)
        }
    }

}