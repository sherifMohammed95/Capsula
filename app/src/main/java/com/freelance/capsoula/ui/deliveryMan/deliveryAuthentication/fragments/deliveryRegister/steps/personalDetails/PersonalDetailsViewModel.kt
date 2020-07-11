package com.freelance.capsoula.ui.deliveryMan.deliveryAuthentication.fragments.deliveryRegister.steps.personalDetails

import android.net.Uri
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.freelance.base.BaseViewModel
import com.freelance.capsoula.data.SpinnerModel
import com.freelance.capsoula.data.UserAddress
import com.freelance.capsoula.data.repository.GeneralRepository
import com.freelance.capsoula.data.responses.NationalitiesResponse
import com.freelance.capsoula.utils.SingleLiveEvent
import com.freelance.capsoula.utils.ValidationUtils
import com.freelance.capsoula.utils.addCallback
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class PersonalDetailsViewModel(val repo: GeneralRepository) :
    BaseViewModel<PersonalDetailsNavigator>() {

    var nationalitiesResponse = SingleLiveEvent<NationalitiesResponse>()
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

    var hasFullNameError = ObservableBoolean(false)
    var hasEmailError = ObservableBoolean(false)
    var hasPhoneError = ObservableBoolean(false)
    var hasCitizenshipError = ObservableBoolean(false)
    var hasNationalIdError = ObservableBoolean(false)
    var hasFullAddressError = ObservableBoolean(false)
    var hasPersonalPhotoError = ObservableBoolean(false)

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

        if (!ValidationUtils.isValidText(citizenshipText.get())) {
            isValid = false
            hasCitizenshipError.set(true)
        }

        if (!ValidationUtils.isValidSaudiIDNumber(nationalIdText.get()!!)) {
            isValid = false
            hasNationalIdError.set(true)
        }

        if (!ValidationUtils.isValidText(fullAddressText.get())) {
            isValid = false
            hasFullAddressError.set(true)
        }

        if (personalPhotoBase64.isEmpty()) {
            isValid = false
            hasPersonalPhotoError.set(true)
        }

        return isValid
    }
}