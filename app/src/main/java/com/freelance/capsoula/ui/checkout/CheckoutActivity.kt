package com.freelance.capsoula.ui.checkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.freelance.base.BaseActivity
import com.freelance.capsoula.R
import com.freelance.capsoula.databinding.ActivityCheckoutBinding
import com.freelance.capsoula.ui.checkout.fragment.cart.CartFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class CheckoutActivity : BaseActivity<ActivityCheckoutBinding, CheckoutViewModel>(),
    CheckoutNavigator {

    private val mViewModel: CheckoutViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        openCartFragment()

    }

    override fun getMyViewModel(): CheckoutViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_checkout
    }

    override fun init() {
        viewDataBinding?.vm = mViewModel
        viewModel = mViewModel
        mViewModel.navigator = this
    }

    override fun openCartFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, CartFragment())
            .commit()
    }

    override fun openDetailsFragment() {
    }

    override fun openDoneFragment() {
    }
}
