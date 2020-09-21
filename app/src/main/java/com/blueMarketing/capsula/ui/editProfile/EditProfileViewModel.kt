package com.blueMarketing.capsula.ui.editProfile

import android.net.Uri
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.blueMarketing.base.BaseViewModel
import com.blueMarketing.capsula.custom.bottomSheet.BottomSheetModel
import com.blueMarketing.capsula.data.repository.UserRepository
import com.blueMarketing.capsula.data.requests.CompleteProfileRequest
import com.blueMarketing.capsula.data.source.local.UserDataSource
import com.blueMarketing.capsula.utils.SingleLiveEvent
import com.blueMarketing.capsula.utils.ValidationUtils
import com.blueMarketing.capsula.utils.addCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditProfileViewModel(val repo: UserRepository) : BaseViewModel<EditProfileNavigator>() {

    var fullNameText = ObservableField<String>("")
    var emailText = ObservableField<String>("")
    var phoneText = ObservableField<String>("")
    var imageUrl = ObservableField<String>("")
    var userAddress = ObservableField<String>("")

    var personalPhotoUri = ObservableField(Uri.EMPTY)
    var personalPhotoBase64 = ""

    var selectedAddress = BottomSheetModel()
    var selectedUserAddressPos = -1

    var hasFullNameError = ObservableBoolean(false)
    var hasEmailError = ObservableBoolean(false)
    var hasPhoneError = ObservableBoolean(false)

    var updateDefaultAddressResponse = SingleLiveEvent<Void>()
    var updateUserProfileResponse = SingleLiveEvent<Void>()

    init {
        initRepository(repo)
        this.updateDefaultAddressResponse = repo.updateDefaultAddressResponse
        this.updateUserProfileResponse = repo.updateUserProfileResponse
        fillView()

        fullNameText.addCallback {
            if (it != null)
                hasFullNameError.set(false)
        }

        emailText.addCallback {
            if (it != null)
                hasEmailError.set(false)
        }

        phoneText.addCallback {
            if (it != null)
                hasPhoneError.set(false)
        }
    }

    fun setSelectedUserAddressPos() {
        run loop@{
            UserDataSource.getUser()?.userAddresses?.forEachIndexed { index, userAddress ->
                if (userAddress.addressId == UserDataSource.getUser()?.defaultAddress?.addressId) {
                    selectedUserAddressPos = index
                    return@loop
                }
            }
        }
    }

    private fun fillView() {
        val user = UserDataSource.getUser()
        fullNameText.set(user?.name)
        emailText.set(user?.email)
        phoneText.set(user?.phone)
        imageUrl.set(user?.imagePath)
        userAddress.set(user?.defaultAddress?.addressDesc)
        setSelectedUserAddressPos()
    }

    fun saveAction() {
        if (!validate()) return
        updateUserProfile()
    }

    fun updateDefaultAddress() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.updateDefaultAddress(selectedAddress.selectionID!!)
        }
    }

    private fun updateUserProfile() {
        val request = CompleteProfileRequest()
        request.email = emailText.get().toString()
        request.image = personalPhotoBase64
        request.name = fullNameText.get().toString()
        request.phone = phoneText.get().toString()
        viewModelScope.launch(Dispatchers.IO) {
            repo.updateUserProfile(request)
        }
    }


    private fun validate(): Boolean {
        var isValid = true

        if (!ValidationUtils.isValidName(fullNameText.get())) {
            isValid = false
            hasFullNameError.set(true)
        }

        if (!ValidationUtils.isValidEmail(emailText.get()!!)) {
            isValid = false
            hasEmailError.set(true)
        }

        if (!ValidationUtils.isValidSaudiMobile(phoneText.get()!!)) {
            isValid = false
            hasPhoneError.set(true)
        }

        return isValid
    }
}