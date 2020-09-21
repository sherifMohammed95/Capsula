package com.blueMarketing.capsula.ui.deliveryMan.history

import androidx.lifecycle.viewModelScope
import com.blueMarketing.base.BaseViewModel
import com.blueMarketing.capsula.data.repository.GeneralRepository
import com.blueMarketing.capsula.data.responses.DeliveryOrdersResponse
import com.blueMarketing.capsula.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class HistoryViewModel (private val repo:GeneralRepository): BaseViewModel<HistoryNavigator>() {

    var currentMonth = 0
    var currentYear = 0
    var historyResponse = SingleLiveEvent<DeliveryOrdersResponse>()

    init {
        initRepository(repo)
        this.historyResponse = repo.historyResponse
        val cal: Calendar = Calendar.getInstance()
        currentMonth = cal.get((Calendar.MONTH)) + 1
        currentYear = cal.get((Calendar.YEAR))
        loadHistory()
    }

    fun loadHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getHistory(1, "$currentYear/$currentMonth")
        }
    }
}