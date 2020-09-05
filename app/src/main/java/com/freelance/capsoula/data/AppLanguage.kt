package com.freelance.capsoula.data

import com.freelance.capsoula.custom.bottomSheet.BottomSheetModel

class AppLanguage(id: Int, name:String):BottomSheetModel() {

    init {
        selectionID = id
        text = name
    }
}