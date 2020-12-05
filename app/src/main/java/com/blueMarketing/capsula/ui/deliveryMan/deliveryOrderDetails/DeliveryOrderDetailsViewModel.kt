package com.blueMarketing.capsula.ui.deliveryMan.deliveryOrderDetails

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.viewModelScope
import com.blueMarketing.base.BaseResponse
import com.blueMarketing.base.BaseViewModel
import com.blueMarketing.capsula.data.DeliveryOrder
import com.blueMarketing.capsula.data.OrderStatus
import com.blueMarketing.capsula.data.repository.OrdersRepository
import com.blueMarketing.capsula.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DeliveryOrderDetailsViewModel(val repo: OrdersRepository) :
    BaseViewModel<DeliveryOrderDetailsNavigator>() {

    var deliveryOrderDetailsResponse = SingleLiveEvent<BaseResponse<DeliveryOrder>>()
    var changeOrderStatusResponse = SingleLiveEvent<Void>()
    var askDeliveryEvent = SingleLiveEvent<Void>()
    var mOrder = DeliveryOrder()
    var fromHistory = ObservableBoolean(false)
    var isBtnClicked = false

    init {
        initRepository(repo)
        this.deliveryOrderDetailsResponse = repo.deliveryOrderDetailsResponse
        this.changeOrderStatusResponse = repo.changeOrderStatusResponse
    }

    fun takeAction() {
        isBtnClicked = true
        if (mOrder.statusId?.value == OrderStatus.DELIVERED.value)
            askDeliveryEvent.call()
        else
            changeOrderStatus()
    }


    fun loadOrderDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getDeliveryOrderDetails(mOrder.orderId)
        }
    }

    private fun changeOrderStatus() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.changeOrderStatus(mOrder.orderId, mOrder.getNextDeliveryAction())
        }
    }

    fun endDelivery(statusId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.changeOrderStatus(mOrder.orderId, statusId)
        }
    }
}