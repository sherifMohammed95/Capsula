package com.blueMarketing.capsula.ui.checkout.fragment.details

import android.net.Uri
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.blueMarketing.base.BaseViewModel
import com.blueMarketing.capsula.custom.bottomSheet.BottomSheetModel
import com.blueMarketing.capsula.data.repository.UserRepository
import com.blueMarketing.capsula.data.requests.CheckoutDetailsRequest
import com.blueMarketing.capsula.data.responses.PaymentDetailsResponse
import com.blueMarketing.capsula.data.source.local.UserDataSource
import com.blueMarketing.capsula.utils.Constants
import com.blueMarketing.capsula.utils.SingleLiveEvent
import com.blueMarketing.capsula.utils.ValidationUtils
import com.blueMarketing.capsula.utils.addCallback
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class DetailsViewModel(val repo: UserRepository) : BaseViewModel<DetailsNavigator>() {

    var insuranceNumberImageUri = ObservableField(Uri.EMPTY)
    var prescriptionImageUri = ObservableField(Uri.EMPTY)

    var currentPickedImageUri = ObservableField(Uri.EMPTY)
    var currentPickedImageIndex = ObservableField(-1)
    var insuranceNumberBase64 = ""
    var prescriptionBase64 = ""

    var showPrescription = ObservableBoolean(true)
    var prescriptionError = ObservableBoolean(false)
    var paymentMethodError = ObservableBoolean(false)

    var paymentMethodText = ObservableField("")
    var deliveryAddressText = ObservableField(UserDataSource.getUser()?.defaultAddress?.addressDesc)
    var selectedAddress = BottomSheetModel()
    var selectedPaymentMethodPos = -1
    var selectedPaymentMethodValue = -1
    var selectedDeliveryAddressPos = -1
    var resoursePath: String? = ""

    var updateDefaultAddressResponse = SingleLiveEvent<Void>()
    var successEvent = SingleLiveEvent<Void>()
    var checkTotalCostEvent = SingleLiveEvent<Void>()
    var paymentDetailsResponse = SingleLiveEvent<PaymentDetailsResponse>()
    var checkoutIdResponse = SingleLiveEvent<String>()
    var validateDeliveryAddressResponse = SingleLiveEvent<Void>()

    init {
        initRepository(repo)
        this.updateDefaultAddressResponse = repo.updateDefaultAddressResponse
        this.successEvent = repo.successEvent
        this.paymentDetailsResponse = repo.paymentDetailsResponse
        this.checkoutIdResponse = repo.checkoutIdResponse
        this.validateDeliveryAddressResponse = repo.validateDeliveryAddressResponse
        showPrescription.set(UserDataSource.getUser()?.cartContent?.find { it.isTreatment } != null)
        setImageUri()
        setSelectedAddressPos()

        paymentMethodText.addCallback {
            if (!it.isNullOrEmpty()) {
                paymentMethodError.set(false)
            }
        }
    }

    fun setSelectedAddressPos() {
        run loop@{
            UserDataSource.getUser()?.userAddresses?.forEachIndexed { index, userAddress ->
                if (userAddress.addressId == UserDataSource.getUser()?.defaultAddress?.addressId) {
                    selectedDeliveryAddressPos = index
                    return@loop
                }
            }
        }
    }

    private fun setImageUri() {
        currentPickedImageUri.addCallback {
            if (it != null) {
                when (currentPickedImageIndex.get()) {
                    Constants.PRESCRIPTION_ID -> prescriptionImageUri.set(
                        currentPickedImageUri.get()
                    )
                    Constants.INSURANCE_NUMBER_ID -> insuranceNumberImageUri.set(
                        currentPickedImageUri.get()
                    )
                }
            }
        }
    }

    fun setImageBase64(encodedImage: String) {
        when (currentPickedImageIndex.get()) {
            Constants.PRESCRIPTION_ID -> prescriptionBase64 = encodedImage
            Constants.INSURANCE_NUMBER_ID -> insuranceNumberBase64 = encodedImage
        }
    }

    fun nextAction() {
        if (!validate())
            return
        validateDeliveryAddress()
    }

    private fun validate(): Boolean {
        var isValid = true
        if (showPrescription.get()) {
            if (!ValidationUtils.isValidText(prescriptionBase64)) {
                isValid = false
                prescriptionError.set(true)
            }
        }

        if (!ValidationUtils.isValidText(paymentMethodText.get())) {
            isValid = false
            paymentMethodError.set(true)
        }
        return isValid
    }

    fun updateDefaultAddress() {
        viewModelScope.launch(IO) {
            repo.updateDefaultAddress(selectedAddress.selectionID!!)
        }
    }

    fun getDeliveryCost() {
        viewModelScope.launch(IO) {
            repo.getDeliveryCost()
        }
    }

    private fun validateDeliveryAddress() {
        viewModelScope.launch(IO) {
            repo.validateDeliveryAddress()
        }
    }

    fun submitCheckoutDetails() {
        val request = CheckoutDetailsRequest()
        request.insuranceNumberImage = insuranceNumberBase64
        request.prescriptionImage = prescriptionBase64
        request.paymentMethodType = this.selectedPaymentMethodValue
        request.resourcePath = this.resoursePath

        viewModelScope.launch(IO) {
            repo.submitCheckoutDetails(request)
        }
    }

    fun prepareCheckout() {
        viewModelScope.launch(IO) {
            repo.prepareCheckout(selectedPaymentMethodValue)
        }
    }

}