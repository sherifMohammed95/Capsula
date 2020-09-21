package com.blueMarketing.capsula.data

import com.blueMarketing.capsula.custom.bottomSheet.BottomSheetModel

class AppLanguage(id: Int, name:String):BottomSheetModel() {

    init {
        selectionID = id
        text = name
    }
}