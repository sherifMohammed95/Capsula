package com.freelance.capsoula.ui.deliveryMan.editDeliveryProfile

import androidx.lifecycle.viewModelScope
import com.freelance.base.BaseViewModel
import com.freelance.capsoula.data.repository.AuthenticationRepository
import com.freelance.capsoula.data.requests.DeliveryRegisterRequest
import com.freelance.capsoula.data.requests.EditDeliveryProfileRequest
import com.freelance.capsoula.utils.Constants
import com.freelance.capsoula.utils.SingleLiveEvent
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