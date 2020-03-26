package com.freelance.capsoula.ui.subCategories

import androidx.lifecycle.viewModelScope
import com.freelance.base.BaseResponse
import com.freelance.base.BaseViewModel
import com.freelance.capsoula.data.Category
import com.freelance.capsoula.data.repository.CategoriesRepository
import com.freelance.capsoula.data.responses.CategoriesResponse
import com.freelance.capsoula.utils.SingleLiveEvent
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