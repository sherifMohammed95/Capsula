package com.freelance.capsoula.ui.deliveryMan.history

import androidx.lifecycle.viewModelScope
import com.freelance.base.BaseViewModel
import com.freelance.capsoula.data.repository.GeneralRepository
import com.freelance.capsoula.data.responses.DeliveryOrdersResponse
import com.freelance.capsoula.utils.SingleLiveEvent
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