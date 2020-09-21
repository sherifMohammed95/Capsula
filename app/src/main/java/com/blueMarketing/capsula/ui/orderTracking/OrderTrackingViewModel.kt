package com.blueMarketing.capsula.ui.orderTracking

import androidx.lifecycle.viewModelScope
import com.blueMarketing.base.BaseResponse
import com.blueMarketing.base.BaseViewModel
import com.blueMarketing.capsula.data.repository.OrdersRepository
import com.blueMarketing.capsula.data.responses.OrderTrackingResponse
import com.blueMarketing.capsula.utils.SingleLiveEvent
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