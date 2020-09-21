package com.blueMarketing.capsula.data

import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.utils.Domain
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DeliveryUser {

    @SerializedName("fullName")
    @Expose
    var fullName: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("phoneNumber")
    @Expose
    var phoneNumber: String? = null

    @SerializedName("nationalId")
    @Expose
    var nationalId: String? = null

    @SerializedName("vehiclePlateLetters")
    @Expose
    var vehiclePlateLetters: String? = null

    @SerializedName("vehiclePlateNumber")
    @Expose
    var vehiclePlateNumber = 0

    @SerializedName("bankAccountNumber")
    @Expose
    var bankAccountNumber: String? = null

    @SerializedName("accountHolderAddress")
    @Expose
    var accountHolderAddress: String? = null

    @SerializedName("personalPicture")
    @Expose
    var personalPicture: String? = null

    @SerializedName("driverLicensePicture")
    @Expose
    var driverLicensePicture: String? = null

    @SerializedName("idCardPicture")
    @Expose
    var idCardPicture: String? = null

    @SerializedName("carFromBehindPicture")
    @Expose
    var carFromBehindPicture: String? = null

    @SerializedName("carFromFrontPicture")
    @Expose
    var carFromFrontPicture: String? = null

    @SerializedName("carRegistrationPicture")
    @Expose
    var carRegistrationPicture: String? = null

    @SerializedName("nationalityId")
    @Expose
    var nationalityId = 0

    @SerializedName("nationalityDesc")
    @Expose
    var nationalityDesc: String? = null

    @SerializedName("carTypeId")
    @Expose
    var carTypeId = 0

    @SerializedName("carTypeDesc")
    @Expose
    var carTypeDesc: String? = null

    @SerializedName("carModelId")
    @Expose
    var carModelId = 0

    @SerializedName("carModelDesc")
    @Expose
    var carModelDesc: String? = null

    @SerializedName("yearId")
    @Expose
    var yearId = 0

    @SerializedName("yearDesc")
    @Expose
    var yearDesc: String? = null

    @SerializedName("vehicleTypeId")
    @Expose
    var vehicleTypeId = 0

    @SerializedName("vehicleTypeDesc")
    @Expose
    var vehicleTypeDesc: String? = null

    @SerializedName("latitude")
    @Expose
    var latitude = 0.0

    @SerializedName("longitude")
    @Expose
    var longitude = 0.0

    @SerializedName("addressDesc")
    @Expose
    var addressDesc: String? = null

    fun getCarModelYearText(): String {
        return Domain.application.getString(R.string.model) + " " + yearDesc
    }

    fun getCarPlatte(): String {
        return "$vehiclePlateLetters $vehiclePlateNumber"
    }

    fun getPhoneWithCode(): String {
        return "+966$phoneNumber"
    }

    fun getBankAccountText(): String {
        if (bankAccountNumber.isNullOrEmpty())
            return Domain.application.getString(R.string.bank_account)
        else
            return bankAccountNumber!!
    }
}