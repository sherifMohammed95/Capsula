package com.freelance.capsoula.ui.addAddress

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.freelance.base.BaseViewModel
import com.freelance.capsoula.data.repository.UserRepository
import com.freelance.capsoula.utils.SingleLiveEvent
import com.freelance.capsoula.utils.ValidationUtils
import com.freelance.capsoula.utils.addCallback
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
        viewModelScope.launch(IO) {
            mRepository.addAddress(currentLocationText.get()!!, mLat, mLng)
        }
    }
}