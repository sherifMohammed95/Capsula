package com.blueMarketing.capsula.ui.deliveryMan.deliveryAuthentication.fragments.deliveryRegister.steps.personalDetails

import android.net.Uri
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.blueMarketing.base.BaseViewModel
import com.blueMarketing.capsula.data.SpinnerModel
import com.blueMarketing.capsula.data.UserAddress
import com.blueMarketing.capsula.data.repository.GeneralRepository
import com.blueMarketing.capsula.data.responses.NationalitiesResponse
import com.blueMarketing.capsula.data.source.local.UserDataSource
import com.blueMarketing.capsula.utils.SingleLiveEvent
import com.blueMarketing.capsula.utils.ValidationUtils
import com.blueMarketing.capsula.utils.addCallback
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class PersonalDetailsViewModel(val repo: GeneralRepository) :
    BaseViewModel<PersonalDetailsNavigator>() {

    var nationalitiesResponse = SingleLiveEvent<NationalitiesResponse>()
    var saveApiRequestEvent = SingleLiveEvent<Void>()
    var selectedNatiolityPos = -1
    var nationalitiesList = ArrayList<SpinnerModel>()

    var fullAddressObj = UserAddress()

    var fullNameText = ObservableField<String>("")
    var emailText = ObservableField<String>("")
    var phoneText = ObservableField<String>("")
    var citizenshipText = ObservableField<String>("")
    var nationalIdText = ObservableField<String>("")
    var bankAccountText = ObservableField<String>("")
    var fullAddressText = ObservableField<String>("")

    var personalImageUrl = ObservableField("")

    var hasFullNameError = ObservableBoolean(false)
    var hasEmailError = ObservableBoolean(false)
    var hasPhoneError = ObservableBoolean(false)
    var hasCitizenshipError = ObservableBoolean(false)
    var hasNationalIdError = ObservableBoolean(false)
    var hasFullAddressError = ObservableBoolean(false)
    var hasPersonalPhotoError = ObservableBoolean(false)

    var isEditMode = ObservableBoolean(false)

    var personalPhotoUri = ObservableField(Uri.EMPTY)
    var personalPhotoBase64 = ""

    init {
        initRepository(repo)
        loadNationalities()
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

        citizenshipText.addCallback {
            if (it != null)
                hasCitizenshipError.set(false)
        }

        nationalIdText.addCallback {
            if (it != null)
                hasNationalIdError.set(false)
        }

        fullAddressText.addCallback {
            if (it != null)
                hasFullAddressError.set(false)
        }

        this.nationalitiesResponse = repo.nationalitiesResponse
    }

    fun fillViewFromUserObject() {
        val user = UserDataSource.getDeliveryUser()
        personalImageUrl.set(user?.personalPicture)
        emailText.set(user?.email)
        phoneText.set(user?.phoneNumber)
        citizenshipText.set(user?.nationalityDesc)
        fullAddressText.set(user?.addressDesc)
        fullAddressObj.addressDesc = user?.addressDesc!!
        fullAddressObj.latitude = user.latitude
        fullAddressObj.longitude = user.longitude
        bankAccountText.set(user.bankAccountNumber)
    }

    fun setSelectedNationalityPos() {
        val user = UserDataSource.getDeliveryUser()
        val nationalityID = user?.nationalityId
        this.nationalitiesList.forEachIndexed { index, item ->
            if (item.id == nationalityID) {
                selectedNatiolityPos = index
                return@forEachIndexed
            }
        }
    }

    fun nextAction() {
        if (!validate()) return
        navigator?.openNextStep()
    }

    private fun loadNationalities() {
        viewModelScope.launch(IO) {
            repo.getNationalities()
        }
    }

    private fun validate(): Boolean {
        var isValid = true

        if (!isEditMode.get()) {
            if (!ValidationUtils.isValidName(fullNameText.get())) {
                isValid = false
                hasFullNameError.set(true)
            }
        }

        if (!ValidationUtils.isValidEmail(emailText.get()!!)) {
            isValid = false
            hasEmailError.set(true)
        }

        if (!ValidationUtils.isValidSaudiMobile(phoneText.get()!!)) {
            isValid = false
            hasPhoneError.set(true)
        }

        if (!ValidationUtils.isValidText(citizenshipText.get())) {
            isValid = false
            hasCitizenshipError.set(true)
        }

        if (!isEditMode.get()) {
            if (!ValidationUtils.isValidSaudiIDNumber(nationalIdText.get()!!)) {
                isValid = false
                hasNationalIdError.set(true)
            }
        }

        if (!ValidationUtils.isValidText(fullAddressText.get())) {
            isValid = false
            hasFullAddressError.set(true)
        }

        if (!isEditMode.get()) {
            if (personalPhotoBase64.isEmpty()) {
                isValid = false
                hasPersonalPhotoError.set(true)
            }
        }


        return isValid
    }

    fun validateForSaveRequest() {
        var isValid = true

        if (!ValidationUtils.isValidEmail(emailText.get()!!)) {
            isValid = false
            hasEmailError.set(true)
        }

        if (!ValidationUtils.isValidSaudiMobile(phoneText.get()!!)) {
            isValid = false
            hasPhoneError.set(true)
        }

        if (!ValidationUtils.isValidText(citizenshipText.get())) {
            isValid = false
            hasCitizenshipError.set(true)
        }

        if (!ValidationUtils.isValidText(fullAddressText.get())) {
            isValid = false
            hasFullAddressError.set(true)
        }

        if (isValid) {
            saveApiRequestEvent.call()
        }
    }
}