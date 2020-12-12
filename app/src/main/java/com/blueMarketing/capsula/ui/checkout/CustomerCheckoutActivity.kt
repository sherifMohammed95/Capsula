package com.blueMarketing.capsula.ui.checkout

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.blueMarketing.base.BaseActivity
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.data.Order
import com.blueMarketing.capsula.databinding.ActivityCustomerCheckoutBinding
import com.blueMarketing.capsula.ui.checkout.fragment.cart.CartFragment
import com.blueMarketing.capsula.ui.checkout.fragment.details.DetailsFragment
import com.blueMarketing.capsula.ui.checkout.fragment.done.DoneFragment
import com.blueMarketing.capsula.utils.Constants
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class CustomerCheckoutActivity : BaseActivity<ActivityCustomerCheckoutBinding, CheckoutViewModel>(),
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
        return R.layout.activity_customer_checkout
    }

    override fun init() {
        viewDataBinding?.vm = mViewModel
        viewModel = mViewModel
        mViewModel.navigator = this
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        if (intent!!.scheme.equals("capsula")) {
            val checkoutId = intent.data!!.getQueryParameter("id")
            Toast.makeText(this, "new intent", Toast.LENGTH_LONG).show()
        }
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
