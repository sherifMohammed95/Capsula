package com.freelance.capsoula.ui.checkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.freelance.base.BaseActivity
import com.freelance.capsoula.R
import com.freelance.capsoula.databinding.ActivityCheckoutBinding
import com.freelance.capsoula.ui.checkout.fragment.cart.CartFragment
import com.freelance.capsoula.ui.checkout.fragment.details.DetailsFragment
import com.freelance.capsoula.ui.checkout.fragment.done.DoneFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.fragment.app.Fragment
import com.freelance.capsoula.data.Order
import com.freelance.capsoula.utils.Constants
import com.google.gson.Gson

class CheckoutActivity : BaseActivity<ActivityCheckoutBinding, CheckoutViewModel>(),
    CheckoutNavigator {

    private val mViewModel: CheckoutViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getIntentsData()

        viewDataBinding?.toolbar?.backToolbarImageView?.setOnClickListener {
            finish()
        }

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

    private fun getIntentsData() {
        mViewModel.fromWhere = intent.getIntExtra(Constants.FROM_WHERE,-1)
        if(mViewModel.fromWhere == Constants.FROM_ORDER_DETAILS)
            mViewModel.mOrder = Gson().fromJson(intent.getStringExtra(Constants.EXTRA_ORDER),
                Order::class.java)

        openCartFragment()
    }

    override fun openCartFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, CartFragment.newInstance(mViewModel.mOrder), "Cart fragment")
            .commit()
    }

    override fun openDetailsFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, DetailsFragment(), "Details fragment")
            .addToBackStack(null)
            .commit()
    }

    override fun openDoneFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, DoneFragment(), "Done fragment")
            .addToBackStack(null)
            .commit()
    }

    override fun onBackPressed() {
        val doneFragment = supportFragmentManager.findFragmentByTag("Done fragment")
        val cartFragment = supportFragmentManager.findFragmentByTag("Cart fragment")
        val detailsFragment = supportFragmentManager.findFragmentByTag("Details fragment")

        if ((doneFragment != null && doneFragment.isVisible) ||
            (cartFragment != null && cartFragment.isVisible)
        )
            finish()
        else if (detailsFragment != null && detailsFragment.isVisible)
            supportFragmentManager.popBackStack()
    }

}
