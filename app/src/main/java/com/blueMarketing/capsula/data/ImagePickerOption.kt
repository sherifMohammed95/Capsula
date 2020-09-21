package com.blueMarketing.capsula.data

import com.blueMarketing.capsula.custom.bottomSheet.BottomSheetModel

class ImagePickerOption constructor(optionId: Int, optionTitle: String = "") : BottomSheetModel() {
    init {
        selectionID = optionId
        text = optionTitle
    }
}