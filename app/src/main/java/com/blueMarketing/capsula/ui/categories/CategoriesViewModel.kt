package com.blueMarketing.capsula.ui.categories

import androidx.lifecycle.viewModelScope
import com.blueMarketing.base.BaseResponse
import com.blueMarketing.base.BaseViewModel
import com.blueMarketing.capsula.data.Category
import com.blueMarketing.capsula.data.repository.CategoriesRepository
import com.blueMarketing.capsula.data.responses.CategoriesResponse
import com.blueMarketing.capsula.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class CategoriesViewModel(private val mRepository: CategoriesRepository) :
    BaseViewModel<CategoriesNavigator>() {

    var categoriesResponse = SingleLiveEvent<BaseResponse<CategoriesResponse>>()
    var storeCategoriesResponse = SingleLiveEvent<BaseResponse<CategoriesResponse>>()
    var storeId = -1
    var pageNo = 1

    var storeCategoriesList = ArrayList<Category>()

    init {
        initRepository(mRepository)
        this.categoriesResponse = mRepository.categoriesResponse
        this.storeCategoriesResponse = mRepository.storeCategoriesResponse
    }

    fun getCategories() {
        viewModelScope.launch(IO) {
            mRepository.getCategories(pageNo)
        }
    }

    fun getStoreCategories(showLoading:Boolean) {
        viewModelScope.launch(IO) {
            mRepository.getStoreCategories(showLoading,pageNo, storeId)
        }
    }

}