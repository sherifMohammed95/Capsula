package com.freelance.capsoula.ui.products

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.freelance.base.BaseActivity
import com.freelance.base.BaseRecyclerAdapter
import com.freelance.capsoula.R
import com.freelance.capsoula.data.Category
import com.freelance.capsoula.data.Product
import com.freelance.capsoula.data.repository.ProductsRepository
import com.freelance.capsoula.data.source.local.UserDataSource
import com.freelance.capsoula.databinding.ActivityBrandsBinding
import com.freelance.capsoula.databinding.ActivityProductsBinding
import com.freelance.capsoula.ui.productDetails.ProductDetailsActivity
import com.freelance.capsoula.ui.products.adapters.ProductsAdapter
import com.freelance.capsoula.utils.Constants
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_products.*
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
        mViewModel.fromWhere = intent.getIntExtra(Constants.FROM_WHERE, 1000)

        when {
            mViewModel.fromWhere == Constants.FROM_BRANDS -> {
                mViewModel.brandId = intent.getIntExtra(Constants.EXTRA_BRAND_ID, -1)
                toolbarTitle = getString(R.string.brands)
                mViewModel.getBrandProducts()
            }
            mViewModel.fromWhere == Constants.FROM_SUB_CATEGORIES -> {
                mViewModel.subCategory = Gson().fromJson(
                    intent.getStringExtra(Constants.EXTRA_SUB_CATEGORY),
                    Category::class.java
                )
                mViewModel.storeId = intent.getIntExtra(Constants.EXTRA_STORE_ID, -1)
                toolbarTitle = mViewModel.subCategory.categoryName

                if (mViewModel.storeId == -1)
                    mViewModel.getSubCategoryProducts()
                else
                    mViewModel.getStoreProducts()
            }
            mViewModel.fromWhere == Constants.FROM_TOP_RATED -> {
                toolbarTitle = getString(R.string.top_rated)
                mViewModel.getTopRated()
            }
            mViewModel.fromWhere == Constants.FROM_BEST_SELLER -> {
                toolbarTitle = getString(R.string.best_seller)
                mViewModel.getBestSellers()
            }

            mViewModel.fromWhere == Constants.FROM_FREE_DELIVERY -> {
                toolbarTitle = getString(R.string.free_delivery)
                mViewModel.getFreeDelivery()
            }
        }
    }

    override fun init() {
        viewModel = mViewModel
        viewDataBinding?.vm = mViewModel
        mViewModel.navigator = this
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
            if (it.data != null) {
                if (!it.data!!.productsList.isNullOrEmpty())
                    mAdapter.setData(it.data!!.productsList!!)
            }
        })
    }

    private fun initRecyclerView() {
        products_recyclerView.adapter = mAdapter
        mAdapter.onPlusClickListener = this
        mAdapter.onITemClickListener = this
    }

    override fun onPlusClick(product: Product) {
        if (UserDataSource.getUser() == null)
            UserDataSource.addProductToCart(product)
        else {

        }
    }

    override fun onItemClick(pos: Int, item: Product) {
        val intent = Intent(this, ProductDetailsActivity::class.java)
        intent.putExtra(Constants.EXTRA_PRODUCT, Gson().toJson(item))
        startActivity(intent)
    }
}
