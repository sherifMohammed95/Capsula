package com.blueMarketing.capsula.ui.deliveryMan.deliveryOrderDetails

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.viewModelScope
import com.blueMarketing.base.BaseResponse
import com.blueMarketing.base.BaseViewModel
import com.blueMarketing.capsula.data.DeliveryOrder
import com.blueMarketing.capsula.data.repository.OrdersRepository
import com.blueMarketing.capsula.utils.Constants.ORDER_IS_PROCESSING
import com.blueMarketing.capsula.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DeliveryOrderDetailsViewModel(val repo: OrdersRepository) :
    BaseViewModel<DeliveryOrderDetailsNavigator>() {

    var deliveryOrderDetailsResponse = SingleLiveEvent<BaseResponse<DeliveryOrder>>()
    var startDeliveryResponse = SingleLiveEvent<Void>()
    var finishDeliveryResponse = SingleLiveEvent<Void>()
    var mOrder = DeliveryOrder()
    var fromHistory = ObservableBoolean(false)

    init {
        initRepository(repo)
        this.deliveryOrderDetailsResponse = repo.deliveryOrderDetailsResponse
        this.startDeliveryResponse = repo.startDeliveryResponse
        this.finishDeliveryResponse = repo.finishDeliveryResponse
    }

    fun takeAction(){
//        if(mOrder.statusId == ORDER_IS_PROCESSING)
//            finishDelivery()
//        else
//            startDelivery()
    }

   private fun startDelivery(){
        viewModelScope.launch(Dispatchers.IO) {
            repo.startDelivery(mOrder.orderId)
        }
    }

  private  fun finishDelivery(){
        viewModelScope.launch(Dispatchers.IO) {
            repo.finishDelivery(mOrder.orderId)
        }
    }

    fun loadOrderDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getDeliveryOrderDetails(mOrder.orderId)
        }
    }
}