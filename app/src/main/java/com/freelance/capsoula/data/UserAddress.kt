package com.freelance.capsoula.data

import com.freelance.capsoula.custom.bottomSheet.BottomSheetModel

class UserAddress : BottomSheetModel() {
    var addressId = 0
    var addressDesc = ""
    var latitude = 0.0
    var longitude = 0.0

    fun initialize(addressList: ArrayList<UserAddress>?) {
        addressList?.forEach {
            it.initialize(it.addressId, it.addressDesc)
        }
    }
}