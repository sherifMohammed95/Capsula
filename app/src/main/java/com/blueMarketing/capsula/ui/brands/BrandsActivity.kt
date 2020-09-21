package com.blueMarketing.capsula.ui.brands

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.blueMarketing.base.BaseActivity
import com.blueMarketing.base.BaseRecyclerAdapter
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.data.Brand
import com.blueMarketing.capsula.data.repository.BrandsRepository
import com.blueMarketing.capsula.databinding.ActivityBrandsBinding
import com.blueMarketing.capsula.ui.brands.adapters.BrandsAdapter
import com.blueMarketing.capsula.ui.products.ProductsActivity
import com.blueMarketing.capsula.utils.Constants
import kotlinx.android.synthetic.main.activity_brands.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module

val brandsModule = module {
    factory { BrandsAdapter() }
    factory { BrandsRepository() }
}

class BrandsActivity : BaseActivity<ActivityBrandsBinding, BrandsViewModel>(),
    BaseRecyclerAdapter.OnITemClickListener<Brand> {

    private val mViewModel: BrandsViewModel by viewModel()
    private val mAdapter: BrandsAdapter by inject()

    override fun getMyViewModel(): BrandsViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_brands
    }

    override fun init() {
        viewModel = mViewModel
        viewDataBinding?.vm = mViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolbar(getString(R.string.brands))
        initRecyclerView()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        mViewModel.brandsResponse.observe(this, Observer {
            if (it.data != null) {
                if (!it.data!!.brandsList.isNullOrEmpty())
                    mAdapter.setData(it.data!!.brandsList!!)
            }
        })
    }

    private fun initRecyclerView() {
        brands_recyclerView.adapter = mAdapter
        mAdapter.onITemClickListener = this
    }

    override fun onItemClick(pos: Int, item: Brand) {
        val intent = Intent(this, ProductsActivity::class.java)
        intent.putExtra(Constants.EXTRA_BRAND_ID, item.brandId)
        intent.putExtra(Constants.FROM_WHERE, Constants.FROM_BRANDS)
        startActivity(intent)
    }
}
