package com.freelance.capsoula.data.requests

import com.freelance.capsoula.data.source.local.UserDataSource

class EditDeliveryProfileRequest {
    var email: String? = ""
    var phoneNumber: String? = ""
    var vehiclePlateLetters: String? = ""
    var vehiclePlateNumber: Int? = 0
    var bankAccountNumber: String? = ""
    var accountHolderAddress: String? = ""
    var personalPicture: String? = ""
    var driverLicensePicture: String? = ""
    var idCardPicture: String? = ""
    var carFromBehindPicture: String? = ""
    var carFromFrontPicture: String? = ""
    var carRegistrationPicture: String? = ""
    var nationalityId: Int? = 0
    var carTypeId: Int? = 0
    var carModelId: Int? = 0
    var yearId: Int? = 0
    var vehicleTypeId: Int? = 0
    var latitude: Double? = 0.0
    var longitude: Double? = 0.0
    var addressDesc: String? = ""

    init {
        val user = UserDataSource.getDeliveryUser()
        email = user?.email
        phoneNumber = user?.phoneNumber
        vehiclePlateLetters = user?.vehiclePlateLetters
        vehiclePlateNumber = user?.vehiclePlateNumber
        bankAccountNumber = user?.bankAccountNumber
        accountHolderAddress = user?.accountHolderAddress
        nationalityId = user?.nationalityId
        carTypeId = user?.carTypeId
        carModelId = user?.carModelId
        yearId = user?.yearId
        vehicleTypeId = user?.vehicleTypeId
        latitude = user?.latitude
        longitude = user?.longitude
        addressDesc = user?.addressDesc
    }
}