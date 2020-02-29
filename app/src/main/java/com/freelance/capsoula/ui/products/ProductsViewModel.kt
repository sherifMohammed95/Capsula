package com.freelance.capsoula.ui.products

import androidx.lifecycle.viewModelScope
import com.freelance.base.BaseResponse
import com.freelance.base.BaseViewModel
import com.freelance.capsoula.data.Category
import com.freelance.capsoula.data.repository.ProductsRepository
import com.freelance.capsoula.data.responses.ProductsResponse
import com.freelance.capsoula.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductsViewModel(val mRepository: ProductsRepository) : BaseViewModel<ProductsNavigator>() {

    var productsResponse = SingleLiveEvent<BaseResponse<ProductsResponse>>()
    var pageNo = 1
    var brandId = -1
    var subCategory = Category()
    var fromWhere = 0

    init {
        initRepository(mRepository)
        this.productsResponse = mRepository.productsResponse
    }

    fun getBrandProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getBrandProducts(pageNo, brandId)
        }
    }

    fun getSubCategoryProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getSubCategoryProducts(pageNo, subCategory.categoryId)
        }
    }

    fun getTopRated() {
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getTopRated(pageNo)
        }
    }

    fun getBestSellers() {
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getBestSellers(pageNo)
        }
    }
}