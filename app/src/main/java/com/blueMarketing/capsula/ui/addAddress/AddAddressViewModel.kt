package com.blueMarketing.capsula.ui.addAddress

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.blueMarketing.base.BaseViewModel
import com.blueMarketing.capsula.data.repository.UserRepository
import com.blueMarketing.capsula.utils.Constants
import com.blueMarketing.capsula.utils.SingleLiveEvent
import com.blueMarketing.capsula.utils.ValidationUtils
import com.blueMarketing.capsula.utils.addCallback
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class AddAddressViewModel(private val mRepository: UserRepository) :
    BaseViewModel<AddAddressNavigator>() {
    var addAddressResponse = SingleLiveEvent<Void>()
    var currentLocationText = ObservableField<String>("")
    var mLat = 0.0
    var mLng = 0.0
    var fromWhere = -1

    var currentLocationError = ObservableBoolean(false)

    init {
        initRepository(mRepository)
        this.addAddressResponse = mRepository.addAddressResponse

        currentLocationText.addCallback {
            currentLocationError.set(false)
        }
    }

    fun onAddAddressClick() {
        if (!ValidationUtils.isValidText(currentLocationText.get())) {
            currentLocationError.set(true)
            return
        }
        if (fromWhere == Constants.FROM_DELIVERY_PERSONAL_DETAILS) {
            navigator?.backWithResult()
            return
        }
        viewModelScope.launch(IO) {
            mRepository.addAddress(currentLocationText.get()!!, mLat, mLng)
        }
    }
}