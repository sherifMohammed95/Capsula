package com.freelance.capsoula.ui.deliveryMan.deliveryOrderDetails

import androidx.lifecycle.viewModelScope
import com.freelance.base.BaseResponse
import com.freelance.base.BaseViewModel
import com.freelance.capsoula.data.DeliveryOrder
import com.freelance.capsoula.data.repository.OrdersRepository
import com.freelance.capsoula.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DeliveryOrderDetailsViewModel(val repo: OrdersRepository) :
    BaseViewModel<DeliveryOrderDetailsNavigator>() {

    var deliveryOrderDetailsResponse = SingleLiveEvent<BaseResponse<DeliveryOrder>>()
    var mOrder = DeliveryOrder()

    init {
        initRepository(repo)
        this.deliveryOrderDetailsResponse = repo.deliveryOrderDetailsResponse
    }

    fun loadOrderDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getDeliveryOrderDetails(mOrder.orderId)
        }
    }
}