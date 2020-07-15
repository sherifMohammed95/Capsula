package com.freelance.capsoula.ui.deliveryMan.deliveryOrderDetails

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.viewModelScope
import com.freelance.base.BaseResponse
import com.freelance.base.BaseViewModel
import com.freelance.capsoula.data.DeliveryOrder
import com.freelance.capsoula.data.repository.OrdersRepository
import com.freelance.capsoula.utils.Constants
import com.freelance.capsoula.utils.SingleLiveEvent
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
        if(mOrder.statusId == 2)
            finishDelivery()
        else
            startDelivery()
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