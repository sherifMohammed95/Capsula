package com.blueMarketing.capsula.ui.stores

import android.os.Bundle
import androidx.lifecycle.Observer
import com.blueMarketing.base.BaseActivity
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.data.repository.StoresRepository
import com.blueMarketing.capsula.databinding.ActivityStoresBinding
import com.blueMarketing.capsula.ui.stores.adapters.StoresAdapter
import kotlinx.android.synthetic.main.activity_stores.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val storesModule = module {
    factory { (mContext:StoresActivity)->StoresAdapter(mContext) }
    factory { StoresRepository() }
}

class StoresActivity : BaseActivity<ActivityStoresBinding, StoresViewModel>(), StoresNavigator {

    private val mViewModel: StoresViewModel by viewModel()
    private val mAdapter: StoresAdapter by inject { parametersOf(this) }

    override fun getMyViewModel(): StoresViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_stores
    }

    override fun init() {
        viewModel = mViewModel
        viewDataBinding?.vm = mViewModel
        mViewModel.navigator = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolbar(getString(R.string.stores))
        initRecyclerView()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        mViewModel.storesResponse.observe(this, Observer {
            if (it.data != null) {
                if (!it.data!!.storesList.isNullOrEmpty())
                    mAdapter.setData(it.data!!.storesList!!)
            }
        })
    }

    private fun initRecyclerView() {
        stores_recyclerView.adapter = mAdapter
    }
}
