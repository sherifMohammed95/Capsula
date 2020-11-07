package com.blueMarketing.capsula.ui.myOrders

import androidx.lifecycle.viewModelScope
import com.blueMarketing.base.BaseResponse
import com.blueMarketing.base.BaseViewModel
import com.blueMarketing.capsula.data.Order
import com.blueMarketing.capsula.data.repository.OrdersRepository
import com.blueMarketing.capsula.data.responses.OrdersResponse
import com.blueMarketing.capsula.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class MyOrdersViewModel(val repo: OrdersRepository) : BaseViewModel<MyOrdersNavigator>() {

    var ordersResponse = SingleLiveEvent<BaseResponse<OrdersResponse>>()
    var orderList = ArrayList<Order>()
    var pageNo = 1
    var currentOrderPos= -1

    init {
        initRepository(repo)
        this.ordersResponse = repo.ordersResponse
        loadOrders(true)
    }

    fun loadOrders(showLoading: Boolean) {
        viewModelScope.launch(IO) {
            repo.getOrders(pageNo, showLoading)
        }
    }
}