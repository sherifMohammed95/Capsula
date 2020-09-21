package com.blueMarketing.capsula.ui.deliveryMan.editDeliveryProfile

import androidx.lifecycle.viewModelScope
import com.blueMarketing.base.BaseViewModel
import com.blueMarketing.capsula.data.repository.AuthenticationRepository
import com.blueMarketing.capsula.data.requests.EditDeliveryProfileRequest
import com.blueMarketing.capsula.utils.Constants
import com.blueMarketing.capsula.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditDeliveryProfileViewModel(private val repo: AuthenticationRepository) :
    BaseViewModel<EditDeliveryProfileNavigator>() {

    var request = EditDeliveryProfileRequest()

    var currentPage = Constants.PERSONAL_DETAILS_STEP

    var updateDeliveryProfileResponse = SingleLiveEvent<Void>()

    init {
        initRepository(repo)
        this.updateDeliveryProfileResponse = repo.updateDeliveryProfileResponse
    }

    fun updateProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.updateDeliveryProfile(request)
        }
    }
}