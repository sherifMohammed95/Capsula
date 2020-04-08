package com.freelance.capsoula.data

import com.freelance.capsoula.custom.bottomSheet.BottomSheetModel

class PaymentMethod(id: Int, imageSrc: Int, name:String) : BottomSheetModel() {

    init {
        selectionID = id
        text = name
        paymentIcon = imageSrc
    }
}