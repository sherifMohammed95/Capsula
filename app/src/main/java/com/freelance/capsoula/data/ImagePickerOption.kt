package com.freelance.capsoula.data

import com.freelance.capsoula.custom.bottomSheet.BottomSheetModel

class ImagePickerOption constructor(optionId: Int, optionTitle: String = "") : BottomSheetModel() {
    init {
        selectionID = optionId
        text = optionTitle
    }
}