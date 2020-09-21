package com.blueMarketing.capsula.ui.orderDetails

import androidx.lifecycle.viewModelScope
import com.blueMarketing.base.BaseResponse
import com.blueMarketing.base.BaseViewModel
import com.blueMarketing.capsula.data.Order
import com.blueMarketing.capsula.data.repository.OrdersRepository
import com.blueMarketing.capsula.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderDetailsViewModel(val repo: OrdersRepository) : BaseViewModel<OrderDetailsNavigator>() {
    var orderDetailsResponse = SingleLiveEvent<BaseResponse<Order>>()
    var cancelOrderResponse = SingleLiveEvent<String>()
    var mOrder = Order()

    init {
        initRepository(repo)
        this.orderDetailsResponse = repo.orderDetailsResponse
        this.cancelOrderResponse = repo.cancelOrderResponse
    }

    fun loadOrderDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getOrderDetails(mOrder.id)
        }
    }

    fun buttonAction() {
        if (mOrder.hasCancelAction())
            cancelOrder()
        else
            navigator?.openCheckout()
    }

    private fun cancelOrder() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.cancelOrder(mOrder.id)
        }
    }

}