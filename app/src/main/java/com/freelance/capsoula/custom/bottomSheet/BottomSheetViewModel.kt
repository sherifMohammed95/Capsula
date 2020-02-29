package com.freelance.capsoula.custom.bottomSheet

import com.freelance.base.BaseViewModel
import com.freelance.capsoula.custom.bottomSheet.BottomSheetNavigator

class BottomSheetViewModel : BaseViewModel<BottomSheetNavigator>(){

    var mSelectedPos = -1
    var mBottomSheetModel = BottomSheetModel()
}