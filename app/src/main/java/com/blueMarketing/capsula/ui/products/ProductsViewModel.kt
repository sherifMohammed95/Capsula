package com.blueMarketing.capsula.ui.products

import androidx.lifecycle.viewModelScope
import com.blueMarketing.base.BaseResponse
import com.blueMarketing.base.BaseViewModel
import com.blueMarketing.capsula.data.requests.CartRequest
import com.blueMarketing.capsula.data.Category
import com.blueMarketing.capsula.data.Product
import com.blueMarketing.capsula.data.repository.ProductsRepository
import com.blueMarketing.capsula.data.responses.ProductsResponse
import com.blueMarketing.capsula.utils.SingleLiveEvent
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
    var productsList = ArrayList<Product>()

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