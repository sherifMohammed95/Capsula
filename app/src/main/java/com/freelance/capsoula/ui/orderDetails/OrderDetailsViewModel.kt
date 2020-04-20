package com.freelance.capsoula.ui.orderDetails

import androidx.lifecycle.viewModelScope
import com.freelance.base.BaseResponse
import com.freelance.base.BaseViewModel
import com.freelance.capsoula.data.Order
import com.freelance.capsoula.data.repository.OrdersRepository
import com.freelance.capsoula.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderDetailsViewModel(val repo: OrdersRepository) : BaseViewModel<OrderDetailsNavigator>() {
    var orderDetailsResponse = SingleLiveEvent<BaseResponse<Order>>()
    var orderId = -1

    init {
        initRepository(repo)
        this.orderDetailsResponse = repo.orderDetailsResponse
    }

    fun loadOrderDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getOrderDetails(orderId)
        }
    }
}