package com.freelance.capsoula.ui.checkout.fragment.details

import android.graphics.Bitmap
import android.net.Uri
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.freelance.base.BaseViewModel
import com.freelance.capsoula.data.source.local.UserDataSource
import com.freelance.capsoula.utils.Constants
import com.freelance.capsoula.utils.ValidationUtils
import com.freelance.capsoula.utils.addCallback

class DetailsViewModel : BaseViewModel<DetailsNavigator>() {

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
    var selectedPaymentMethodPos = -1

    init {

        showPrescription.set(UserDataSource.getUser()?.cartContent?.find { it.isTreatment } != null)
        setImageUri()
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

     fun validate():Boolean{
        var isValid = true
        if(showPrescription.get()){
            if(!ValidationUtils.isValidText(prescriptionBase64)){
                isValid = false
                prescriptionError.set(true)
            }
        }

        if(!ValidationUtils.isValidText(paymentMethodText.get())){
            isValid = false
            paymentMethodError.set(true)
        }
        return isValid
    }

}