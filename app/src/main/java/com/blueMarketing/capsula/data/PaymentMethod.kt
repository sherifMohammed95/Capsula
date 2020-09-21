package com.blueMarketing.capsula.data

import com.blueMarketing.capsula.custom.bottomSheet.BottomSheetModel

class PaymentMethod(id: Int, imageSrc: Int, name:String) : BottomSheetModel() {

    init {
        selectionID = id
        text = name
        paymentIcon = imageSrc
    }
}