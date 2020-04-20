package com.freelance.capsoula.ui.myOrders

import androidx.lifecycle.viewModelScope
import com.freelance.base.BaseResponse
import com.freelance.base.BaseViewModel
import com.freelance.capsoula.data.Order
import com.freelance.capsoula.data.Product
import com.freelance.capsoula.data.repository.OrdersRepository
import com.freelance.capsoula.data.responses.OrdersResponse
import com.freelance.capsoula.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class MyOrdersViewModel(val repo: OrdersRepository) : BaseViewModel<MyOrdersNavigator>() {

    var ordersResponse = SingleLiveEvent<BaseResponse<OrdersResponse>>()
    var orderList = ArrayList<Order>()

    init {
        initRepository(repo)
        this.ordersResponse = repo.ordersResponse
        loadOrders(true)
    }

    fun loadOrders(showLoading:Boolean) {
        viewModelScope.launch(IO) {
            repo.getOrders(showLoading)
        }
    }
}