package com.blueMarketing.capsula.ui.subCategories

import androidx.lifecycle.viewModelScope
import com.blueMarketing.base.BaseResponse
import com.blueMarketing.base.BaseViewModel
import com.blueMarketing.capsula.data.Category
import com.blueMarketing.capsula.data.repository.CategoriesRepository
import com.blueMarketing.capsula.data.responses.CategoriesResponse
import com.blueMarketing.capsula.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class SubCategoriesViewModel(private val mRepository: CategoriesRepository) :
    BaseViewModel<SubCategoriesNavigator>() {

    var subCategoriesResponse = SingleLiveEvent<BaseResponse<CategoriesResponse>>()
    var mCategory = Category()
    var pageNo = 1
    var storeId = -1

    init {
        initRepository(mRepository)
        this.subCategoriesResponse = mRepository.subCategoriesResponse
    }

    fun getSubCategories() {
        viewModelScope.launch(IO) {
            mRepository.getSubCategories(pageNo, mCategory.categoryId)
        }
    }

    fun getStoreSubCategories() {
        viewModelScope.launch(IO) {
            mRepository.getStoreSubCategories(pageNo, mCategory.categoryId, storeId)
        }
    }
}