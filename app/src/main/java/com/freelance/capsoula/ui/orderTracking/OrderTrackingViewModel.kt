package com.freelance.capsoula.ui.orderTracking

import androidx.lifecycle.viewModelScope
import com.freelance.base.BaseResponse
import com.freelance.base.BaseViewModel
import com.freelance.capsoula.data.repository.OrdersRepository
import com.freelance.capsoula.data.responses.OrderTrackingResponse
import com.freelance.capsoula.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderTrackingViewModel(val repo: OrdersRepository) : BaseViewModel<OrderTrackingNavigator>() {

    var orderTrackingResponse = SingleLiveEvent<BaseResponse<OrderTrackingResponse>>()
    var orderId = -1

    init {
        initRepository(repo)
        this.orderTrackingResponse = repo.orderTrackingResponse
    }

    fun loadOrderTracking() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getOrderTracking(orderId)
        }
    }
}