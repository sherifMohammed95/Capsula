package com.freelance.capsoula.data

import com.freelance.capsoula.custom.bottomSheet.BottomSheetModel
import com.google.gson.annotations.SerializedName

class SpinnerModel : BottomSheetModel() {
    var id = 0
    var value = ""

    fun initialize(items: ArrayList<SpinnerModel>?) {
        items?.forEach {
            it.initialize(it.id, it.value)
        }
    }
}