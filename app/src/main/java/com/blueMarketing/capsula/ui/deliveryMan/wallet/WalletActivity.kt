package com.blueMarketing.capsula.ui.deliveryMan.wallet

import android.os.Bundle
import androidx.lifecycle.Observer
import com.blueMarketing.base.BaseActivity
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.data.repository.DeliveryRepository
import com.blueMarketing.capsula.databinding.ActivityWalletBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module

val walletModule = module {
    factory { DeliveryRepository() }
}

class WalletActivity : BaseActivity<ActivityWalletBinding, WalletViewModel>(), WalletNavigator {

    private val mViewModel: WalletViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeToLiveData()
    }

    override fun getMyViewModel(): WalletViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_wallet
    }

    override fun init() {
        viewModel = mViewModel
        viewDataBinding?.vm = mViewModel
        viewDataBinding?.navigator = this
        mViewModel.navigator = this
    }

    private fun subscribeToLiveData() {
        mViewModel.walletResponse.observe(this, Observer {
            if (it != null)
                viewDataBinding?.wallet = it
        })
    }

    override fun backAction() {
        finish()
    }
}