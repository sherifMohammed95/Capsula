package com.blueMarketing.capsula.data

import com.blueMarketing.capsula.custom.bottomSheet.BottomSheetModel

class SpinnerModel : BottomSheetModel() {
    var id = 0
    var value = ""

    fun initialize(items: ArrayList<SpinnerModel>?) {
        items?.forEach {
            it.initialize(it.id, it.value)
        }
    }
}