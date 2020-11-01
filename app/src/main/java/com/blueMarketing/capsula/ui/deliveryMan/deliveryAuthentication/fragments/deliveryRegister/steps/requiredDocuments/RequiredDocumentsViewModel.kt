package com.blueMarketing.capsula.ui.deliveryMan.deliveryAuthentication.fragments.deliveryRegister.steps.requiredDocuments

import android.net.Uri
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.blueMarketing.base.BaseViewModel
import com.blueMarketing.capsula.data.repository.AuthenticationRepository
import com.blueMarketing.capsula.data.requests.DeliveryRegisterRequest
import com.blueMarketing.capsula.data.source.local.UserDataSource
import com.blueMarketing.capsula.utils.Constants
import com.blueMarketing.capsula.utils.SingleLiveEvent
import com.blueMarketing.capsula.utils.addCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RequiredDocumentsViewModel(val repo: AuthenticationRepository) :
    BaseViewModel<RequiredDocumentsNavigator>() {

    var deliveryRegisterResponse = SingleLiveEvent<String>()
    var saveApiRequestEvent = SingleLiveEvent<Void>()

    var carLicenseImageUri = ObservableField(Uri.EMPTY)
    var nationalIDImageUri = ObservableField(Uri.EMPTY)
    var carFrontImageUri = ObservableField(Uri.EMPTY)
    var carBackImageUri = ObservableField(Uri.EMPTY)
    var carRegistrationImageUri = ObservableField(Uri.EMPTY)

    var currentPickedImageUri = ObservableField(Uri.EMPTY)
    var currentPickedImageIndex = ObservableField(-1)
    var carLicenseBase64 = ""
    var nationalIDBase64 = ""
    var carFrontBase64 = ""
    var carBackBase64 = ""
    var carRegistrationBase64 = ""

    var carLicenseError = ObservableBoolean(false)
    var carFrontError = ObservableBoolean(false)
    var carBackError = ObservableBoolean(false)
    var carRegistrationError = ObservableBoolean(false)
    var nationalIDError = ObservableBoolean(false)
    var termsError = ObservableBoolean(false)

    var termsAndConditions = ObservableBoolean(false)

    var isEditMode = ObservableBoolean(false)

    var carLicenseImageUrl = ObservableField("")
    var nationalIDImageUrl = ObservableField("")
    var carFrontImageUrl = ObservableField("")
    var carBackImageUrl = ObservableField("")
    var carRegistrationImageUrl = ObservableField("")

    init {
        initRepository(repo)
        this.deliveryRegisterResponse = repo.deliveryRegisterResponse
        setImageUri()
    }

    fun fillViewFromUserObject() {
        val user = UserDataSource.getDeliveryUser()
        carLicenseImageUrl.set(user?.driverLicensePicture)
        nationalIDImageUrl.set(user?.idCardPicture)
        carFrontImageUrl.set(user?.carFromFrontPicture)
        carBackImageUrl.set(user?.carFromBehindPicture)
        carRegistrationImageUrl.set(user?.carRegistrationPicture)
    }

    fun submitAction() {
        if (!validate())
            return
        navigator?.submitRequest()
    }

    fun register(request: DeliveryRegisterRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deliveryRegister(request)
        }
    }

    fun termsAction() {
        termsAndConditions.set(!termsAndConditions.get())
        termsError.set(false)
    }

    private fun validate(): Boolean {
        var isValid = true

        if (carLicenseBase64.isEmpty()) {
            isValid = false
            carLicenseError.set(true)
        }

        if (nationalIDBase64.isEmpty()) {
            isValid = false
            nationalIDError.set(true)
        }

//        if (carFrontBase64.isEmpty()) {
//            isValid = false
//            carFrontError.set(true)
//        }
//
//        if (carBackBase64.isEmpty()) {
//            isValid = false
//            carBackError.set(true)
//        }
//
//        if (carRegistrationBase64.isEmpty()) {
//            isValid = false
//            carRegistrationError.set(true)
//        }

        if (!termsAndConditions.get()) {
            isValid = false
            termsError.set(true)
        }

        return isValid
    }

    private fun setImageUri() {
        currentPickedImageUri.addCallback {
            if (it != null) {
                when (currentPickedImageIndex.get()) {
                    Constants.CAR_LICENSE -> carLicenseImageUri.set(currentPickedImageUri.get())
                    Constants.NATIONAL_ID_IMAGE -> nationalIDImageUri.set(currentPickedImageUri.get())
                    Constants.CAR_FRONT -> carFrontImageUri.set(currentPickedImageUri.get())
                    Constants.CAR_BACK -> carBackImageUri.set(currentPickedImageUri.get())
                    Constants.CAR_REGISTRATION -> carRegistrationImageUri.set(currentPickedImageUri.get())
                }
            }
        }
    }

    fun setImageBase64(encodedImage: String) {
        when (currentPickedImageIndex.get()) {
            Constants.CAR_LICENSE -> carLicenseBase64 = encodedImage
            Constants.NATIONAL_ID_IMAGE -> nationalIDBase64 = encodedImage
            Constants.CAR_FRONT -> carFrontBase64 = encodedImage
            Constants.CAR_BACK -> carBackBase64 = encodedImage
            Constants.CAR_REGISTRATION -> carRegistrationBase64 = encodedImage
        }
    }

    fun hideError() {
        when (currentPickedImageIndex.get()) {
            Constants.CAR_LICENSE -> carLicenseError.set(false)
            Constants.NATIONAL_ID_IMAGE -> nationalIDError.set(false)
            Constants.CAR_FRONT -> carFrontError.set(false)
            Constants.CAR_BACK -> carBackError.set(false)
            Constants.CAR_REGISTRATION -> carRegistrationError.set(false)
        }
    }

    fun validateForSaveRequest() {
        saveApiRequestEvent.call()

    }

}