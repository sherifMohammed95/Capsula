package com.freelance.capsoula.custom.bottomSheet

open class BottomSheetModel {

    var selectionID: Int? = 0
    var text: String? = ""
    var selected: Boolean? = false
    var paymentIcon:Int = -1

    fun initialize(selectionID: Int, text: String?) {
        this.selectionID = selectionID
        this.text = text
    }

}