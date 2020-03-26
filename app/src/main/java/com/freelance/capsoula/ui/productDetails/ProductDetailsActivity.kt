package com.freelance.capsoula.ui.productDetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.freelance.base.BaseActivity
import com.freelance.capsoula.R
import com.freelance.capsoula.data.Product
import com.freelance.capsoula.databinding.ActivityProductDetailsBinding
import com.freelance.capsoula.utils.Constants
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductDetailsActivity :
    BaseActivity<ActivityProductDetailsBinding, ProductDetailsViewModel>(),
    ProductDetailsNavigator {

    private val mViewModel: ProductDetailsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getIntentsData()

    }

    override fun getMyViewModel(): ProductDetailsViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_product_details
    }

    override fun init() {
        viewDataBinding?.vm = mViewModel
        viewModel = mViewModel
        mViewModel.navigator = this
    }

    private fun getIntentsData() {
        viewDataBinding?.product =
            Gson().fromJson(intent.getStringExtra(Constants.EXTRA_PRODUCT), Product::class.java)
    }
}
