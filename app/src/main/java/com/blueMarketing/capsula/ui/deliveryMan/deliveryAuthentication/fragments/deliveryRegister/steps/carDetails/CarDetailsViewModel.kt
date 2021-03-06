package com.blueMarketing.capsula.ui.deliveryMan.deliveryAuthentication.fragments.deliveryRegister.steps.carDetails

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.blueMarketing.base.BaseViewModel
import com.blueMarketing.capsula.data.SpinnerModel
import com.blueMarketing.capsula.data.repository.GeneralRepository
import com.blueMarketing.capsula.data.responses.DeliveryRegisterBasicResponse
import com.blueMarketing.capsula.data.responses.NationalitiesResponse
import com.blueMarketing.capsula.data.source.local.UserDataSource
import com.blueMarketing.capsula.utils.SingleLiveEvent
import com.blueMarketing.capsula.utils.ValidationUtils
import com.blueMarketing.capsula.utils.addCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CarDetailsViewModel(val repo: GeneralRepository) : BaseViewModel<CarDetailsNavigator>() {

    var deliveryRegisterBasicResponse = SingleLiveEvent<DeliveryRegisterBasicResponse>()
    var carModelsResponse = SingleLiveEvent<NationalitiesResponse>()
    var saveApiRequestEvent = SingleLiveEvent<Void>()

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

    var isEditMode = ObservableBoolean(false)

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

    fun fillViewFromUserObject() {
        val user = UserDataSource.getDeliveryUser()
        carBrandText.set(user?.carTypeDesc)
        carModelText.set(user?.carModelDesc)
        modelYearText.set(user?.yearDesc)
        plateNumberText.set(user?.vehiclePlateNumber.toString())
        plateLetterText.set(user?.vehiclePlateLetters)
        licenseTypeText.set(user?.vehicleTypeDesc)
    }

    fun setSelectedPos() {
        val user = UserDataSource.getDeliveryUser()
        val carBrandId = user?.carTypeId
        carBrands.forEachIndexed { index, item ->
            if (item.id == carBrandId) {
                selectedCarBrandPos = index
                return@forEachIndexed
            }
        }

        val carYearId = user?.yearId
        modelYears.forEachIndexed { index, item ->
            if (item.id == carYearId) {
                selectedModelYearPos = index
                return@forEachIndexed
            }
        }

        val carLicenceId = user?.vehicleTypeId
        licenseTypes.forEachIndexed { index, item ->
            if (item.id == carLicenceId) {
                selectedLicenseTypePos = index
                return@forEachIndexed
            }
        }
        loadCarModels()
    }

    fun setCarModelSelectedPos() {
        val user = UserDataSource.getDeliveryUser()
        val carModelId = user?.carModelId
        carModels.forEachIndexed { index, item ->
            if (item.id == carModelId) {
                selectedCarModelPos = index
                return@forEachIndexed
            }
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

    fun validateForSaveRequest() {
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
        if (isValid) {
            saveApiRequestEvent.call()
        }
    }
}