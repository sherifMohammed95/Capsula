package com.freelance.capsoula.ui.deliveryMan.deliveryHome

import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.freelance.base.BaseViewModel
import com.freelance.capsoula.data.repository.GeneralRepository
import com.freelance.capsoula.data.responses.DeliveryOrdersResponse
import com.freelance.capsoula.data.source.local.UserDataSource
import com.freelance.capsoula.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class DeliveryHomeViewModel(private val mRepo: GeneralRepository) :
    BaseViewModel<DeliveryHomeNavigator>() {

    var userNameText = ObservableField(UserDataSource.getDeliveryUser()?.fullName)
    var deliveryHomeDataResponse = SingleLiveEvent<DeliveryOrdersResponse>()

    init {
        initRepository(mRepo)
        this.deliveryHomeDataResponse = mRepo.deliveryHomeDataResponse
        getHomeData()
    }

    fun getHomeData() {
        viewModelScope.launch(IO) {
            mRepo.getDeliveryHomeData()
        }
    }

}