package com.freelance.capsoula.ui.products

import androidx.lifecycle.viewModelScope
import com.freelance.base.BaseResponse
import com.freelance.base.BaseViewModel
import com.freelance.capsoula.data.requests.CartRequest
import com.freelance.capsoula.data.Category
import com.freelance.capsoula.data.Product
import com.freelance.capsoula.data.repository.ProductsRepository
import com.freelance.capsoula.data.responses.ProductsResponse
import com.freelance.capsoula.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductsViewModel(val mRepository: ProductsRepository) : BaseViewModel<ProductsNavigator>() {

    var productsResponse = SingleLiveEvent<BaseResponse<ProductsResponse>>()
    var pageNo = 1
    var brandId = -1
    var storeId = -1
    var subCategory = Category()
    var mCategory = Category()
    var fromWhere = 0
    var mProduct = Product()
    var cartResponse = SingleLiveEvent<ArrayList<Product>>()

    init {
        initRepository(mRepository)
        this.productsResponse = mRepository.productsResponse
        this.cartResponse = mRepository.cartResponse
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

    fun getCategoryProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getCategoryProducts(pageNo, mCategory.categoryId, storeId)
        }
    }

    fun getStoreProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getStoreProducts(pageNo, subCategory.categoryId, storeId)
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

    fun getFreeDelivery() {
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.getFreeDelivery(pageNo)
        }
    }

    fun addProductToCart() {
        val cart = CartRequest()
        cart.mainId = mProduct.mainId
        cart.quantity = 1
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.updateCart(cart)
        }
    }
}