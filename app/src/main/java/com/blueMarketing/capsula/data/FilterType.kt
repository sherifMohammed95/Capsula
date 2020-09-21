package com.blueMarketing.capsula.data

import com.blueMarketing.capsula.custom.bottomSheet.BottomSheetModel

class FilterType(val filterId: Int, val name: String) : BottomSheetModel() {

    init {
        selectionID = filterId
        text = name
    }

}