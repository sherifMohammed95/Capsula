package com.blueMarketing.capsula.ui.notifications

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.viewModelScope
import com.blueMarketing.base.BaseResponse
import com.blueMarketing.base.BaseViewModel
import com.blueMarketing.capsula.data.repository.GeneralRepository
import com.blueMarketing.capsula.data.responses.NotificationsResponse
import com.blueMarketing.capsula.data.responses.StoresResponse
import com.blueMarketing.capsula.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationsViewModel(private val mRepository: GeneralRepository) :
    BaseViewModel<NotificationsNavigator>() {

    var notificationResponse = SingleLiveEvent<NotificationsResponse>()
    var haveNotifications = ObservableBoolean(true)

    var pageNo = 1

    init {
        initRepository(mRepository)
        this.notificationResponse = mRepository.notificationResponse
        getNotifications()
    }

    private fun getNotifications() {
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getNotifications(true, pageNo)
        }
    }
}