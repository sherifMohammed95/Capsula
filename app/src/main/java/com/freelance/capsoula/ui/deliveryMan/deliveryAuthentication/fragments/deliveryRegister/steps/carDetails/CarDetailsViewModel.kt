package com.freelance.capsoula.ui.deliveryMan.deliveryAuthentication.fragments.deliveryRegister.steps.carDetails

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.freelance.base.BaseViewModel
import com.freelance.capsoula.data.SpinnerModel
import com.freelance.capsoula.data.repository.GeneralRepository
import com.freelance.capsoula.data.responses.DeliveryRegisterBasicResponse
import com.freelance.capsoula.data.responses.NationalitiesResponse
import com.freelance.capsoula.utils.SingleLiveEvent
import com.freelance.capsoula.utils.ValidationUtils
import com.freelance.capsoula.utils.addCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CarDetailsViewModel(val repo: GeneralRepository) : BaseViewModel<CarDetailsNavigator>() {

    var deliveryRegisterBasicResponse = SingleLiveEvent<DeliveryRegisterBasicResponse>()
    var carModelsResponse = SingleLiveEvent<NationalitiesResponse>()

    var selectedCarBrandPos = -1
    var selectedCarModelPos = -1
    var selectedModelYearPos = -1
    var selectedLicenseTypePos = -1

    var carBrands = ArrayList<SpinnerModel>()
    var carModels = ArrayList<SpinnerModel>()
    var modelYears = ArrayList<SpinnerModel>()
    var licenseTypes = ArrayList<SpinnerModel>()

    var carBrandText = ObservableField<String>("")
    var carModelText = ObservableField<String>("")
    var modelYearText = ObservableField<String>("")
    var licenseTypeText = ObservableField<String>("")
    var plateNumberText = ObservableField<String>("")
    var plateLetterText = ObservableField<String>("")

    var hasCarBrandError = ObservableBoolean(false)
    var hasCarModelError = ObservableBoolean(false)
    var hasModelYearError = ObservableBoolean(false)
    var hasLicenseTypeError = ObservableBoolean(false)
    var hasVehiclePlateError = ObservableBoolean(false)

    init {
        initRepository(repo)
        this.deliveryRegisterBasicResponse = repo.deliveryRegisterBasicResponse
        this.carModelsResponse = repo.carModelsResponse
        loadRegisterBasicData()
        carBrandText.addCallback {
            if (it != null)
                hasCarBrandError.set(false)
        }

        carModelText.addCallback {
            if (it != null)
                hasCarModelError.set(false)
        }

        modelYearText.addCallback {
            if (it != null)
                hasModelYearError.set(false)
        }

        licenseTypeText.addCallback {
            if (it != null)
                hasLicenseTypeError.set(false)
        }

        plateLetterText.addCallback {
            if (it != null)
                hasVehiclePlateError.set(false)
        }

        plateNumberText.addCallback {
            if (it != null)
                hasVehiclePlateError.set(false)
        }
    }

    fun nextAction() {
        if (!validate()) return
        navigator?.openNextStep()
    }

    private fun loadRegisterBasicData() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getRegisterBasicData()
        }
    }

    fun loadCarModels() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getCarModels(carId = carBrands[selectedCarBrandPos].selectionID!!)
        }
    }

    private fun validate(): Boolean {
        var isValid = true

        if (!ValidationUtils.isValidText(carBrandText.get())) {
            isValid = false
            hasCarBrandError.set(true)
        }

        if (!ValidationUtils.isValidText(carModelText.get()!!)) {
            isValid = false
            hasCarModelError.set(true)
        }

        if (!ValidationUtils.isValidText(modelYearText.get()!!)) {
            isValid = false
            hasModelYearError.set(true)
        }

        if (!ValidationUtils.isValidText(modelYearText.get()!!)) {
            isValid = false
            hasModelYearError.set(true)
        }

        if (!ValidationUtils.isValidText(licenseTypeText.get()!!)) {
            isValid = false
            hasLicenseTypeError.set(true)
        }

        if (plateNumberText.get()!!.length <= 2) {
            isValid = false
            hasVehiclePlateError.set(true)
        }

        if (!ValidationUtils.isValidSaudiVehiclePlateLetters(plateLetterText.get()!!)) {
            isValid = false
            hasVehiclePlateError.set(true)
        }
        return isValid
    }
}