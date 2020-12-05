package com.blueMarketing.capsula.ui.products

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.blueMarketing.base.BaseActivity
import com.blueMarketing.base.BaseRecyclerAdapter
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.data.Category
import com.blueMarketing.capsula.data.MessageEvent
import com.blueMarketing.capsula.data.Product
import com.blueMarketing.capsula.data.repository.ProductsRepository
import com.blueMarketing.capsula.data.source.local.UserDataSource
import com.blueMarketing.capsula.databinding.ActivityProductsBinding
import com.blueMarketing.capsula.ui.checkout.CheckoutActivity
import com.blueMarketing.capsula.ui.productDetails.ProductDetailsActivity
import com.blueMarketing.capsula.ui.products.adapters.ProductsAdapter
import com.blueMarketing.capsula.utils.Constants
import com.google.gson.Gson
import io.reactivex.functions.Action
import kotlinx.android.synthetic.main.activity_categories.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_products.*
import org.greenrobot.eventbus.EventBus
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module

val productsModule = module {
    factory { ProductsRepository() }
    factory { ProductsAdapter() }
}

class ProductsActivity : BaseActivity<ActivityProductsBinding, ProductsViewModel>(),
    ProductsNavigator, ProductsAdapter.OnPlusClickListener,
    BaseRecyclerAdapter.OnITemClickListener<Product> {

    private val mViewModel: ProductsViewModel by viewModel()
    private val mAdapter: ProductsAdapter by inject()
    private var toolbarTitle = ""


    override fun getMyViewModel(): ProductsViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_products
    }

    private fun getIntentsData() {
        mViewModel.mCategory = Gson().fromJson(
            intent.getStringExtra(Constants.EXTRA_CATEGORY),
            Category::class.java
        )

        mViewModel.storeId = intent.getIntExtra(Constants.EXTRA_STORE_ID, -1)
        toolbarTitle = mViewModel.mCategory.categoryName

        mViewModel.getCategoryProducts(true)
//        mViewModel.fromWhere = intent.getIntExtra(Constants.FROM_WHERE, 1000)

//        when {
//            mViewModel.fromWhere == Constants.FROM_BRANDS -> {
//                mViewModel.brandId = intent.getIntExtra(Constants.EXTRA_BRAND_ID, -1)
//                toolbarTitle = getString(R.string.brands)
//                mViewModel.getBrandProducts()
//            }
//            mViewModel.fromWhere == Constants.FROM_SUB_CATEGORIES -> {
//                mViewModel.subCategory = Gson().fromJson(
//                    intent.getStringExtra(Constants.EXTRA_SUB_CATEGORY),
//                    Category::class.java
//                )
//                mViewModel.storeId = intent.getIntExtra(Constants.EXTRA_STORE_ID, -1)
//                toolbarTitle = mViewModel.subCategory.categoryName
//
//                if (mViewModel.storeId == -1)
//                    mViewModel.getSubCategoryProducts()
//                else
//                    mViewModel.getStoreProducts()
//            }
//            mViewModel.fromWhere == Constants.FROM_TOP_RATED -> {
//                toolbarTitle = getString(R.string.top_rated)
//                mViewModel.getTopRated()
//            }
//            mViewModel.fromWhere == Constants.FROM_BEST_SELLER -> {
//                toolbarTitle = getString(R.string.best_seller)
//                mViewModel.getBestSellers()
//            }
//
//            mViewModel.fromWhere == Constants.FROM_FREE_DELIVERY -> {
//                toolbarTitle = getString(R.string.free_delivery)
//                mViewModel.getFreeDelivery()
//            }
//        }
    }

    override fun init() {
        viewModel = mViewModel
        viewDataBinding?.vm = mViewModel
        viewDataBinding?.noDataText = getString(R.string.no_products)
        mViewModel.navigator = this
        paginateWithScrollView(products_scroll_layout, Action {
            mViewModel.pageNo++
            mViewModel.getCategoryProducts(false)
        })
        swipeToRefresh(products_refresh_layout, Action {
            mViewModel.isLastPage = false
            mViewModel.pageNo = 1
            mViewModel.productsList = ArrayList()
            mViewModel.getCategoryProducts(false)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getIntentsData()
        setUpToolbar(toolbarTitle)
        initRecyclerView()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        mViewModel.productsResponse.observe(this, Observer {
            viewModel?.isPagingLoadingEvent?.value = false
            products_refresh_layout.isRefreshing = false

            if (it.data != null) {

                if (it.data!!.count > 0)
                    mViewModel.hasData.set(true)
                else
                    mViewModel.hasData.set(false)

                if (!it.data!!.productsList.isNullOrEmpty()) {
                    if (mViewModel.pageNo == 1)
                        mViewModel.productsList = it.data!!.productsList!!
                    else
                        mViewModel.productsList.addAll(it.data!!.productsList!!)

                    if (mViewModel.productsList.size == it.data!!.count)
                        mViewModel.isLastPage = true
                }
                mAdapter.setData(mViewModel.productsList)
            }
        })

        mViewModel.cartResponse.observe(this, Observer {
            val user = UserDataSource.getUser()
            user?.cartContent = it
            UserDataSource.saveUser(user)
            EventBus.getDefault().postSticky(MessageEvent(Constants.UPDATE_CART_NUMBER))
        })
    }

    private fun initRecyclerView() {
        products_recyclerView.adapter = mAdapter
        mAdapter.onPlusClickListener = this
        mAdapter.onITemClickListener = this
    }

    override fun onPlusClick(product: Product) {
        mViewModel.mProduct = product
        val storeName = UserDataSource.checkCartFromTheSameStore(product)
        if (UserDataSource.getUser() == null) {
            if (storeName.contentEquals(""))
                UserDataSource.addProductToCart(product)
            else {
                val message = getString(R.string.your_cart_contains) + " " + storeName + " " +
                        getString(R.string.do_u_wish)
                showPopUp("", message, R.string.new_order,
                    DialogInterface.OnClickListener { _, _ ->
                        UserDataSource.deleteCart()
                        UserDataSource.addProductToCart(product)
                    }
                    , getString(android.R.string.cancel), false)
            }
        } else {
            if (UserDataSource.checkProductExistInCart(
                    UserDataSource.getUser()?.cartContent!!,
                    product
                )
            ) {
                openCheckout()
            } else {
                if (storeName.contentEquals(""))
                    mViewModel.addProductToCart()
                else {
                    val message = getString(R.string.your_cart_contains) + " " + storeName + " " +
                            getString(R.string.do_u_wish)
                    showPopUp("", message, R.string.new_order,
                        DialogInterface.OnClickListener { _, _ ->
                            mViewModel.addProductToCart()
                        }
                        , getString(android.R.string.cancel), false)
                }
            }
        }
    }

    override fun onItemClick(pos: Int, item: Product) {
        val intent = Intent(this, ProductDetailsActivity::class.java)
        intent.putExtra(Constants.EXTRA_PRODUCT, Gson().toJson(item))
        startActivity(intent)
    }

    override fun openCheckout() {
        startActivity(Intent(this, CheckoutActivity::class.java))
    }
}
