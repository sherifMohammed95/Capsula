package com.blueMarketing.capsula.ui.productDetails

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.blueMarketing.base.BaseActivity
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.data.MessageEvent
import com.blueMarketing.capsula.data.Product
import com.blueMarketing.capsula.data.source.local.UserDataSource
import com.blueMarketing.capsula.databinding.ActivityProductDetailsBinding
import com.blueMarketing.capsula.ui.checkout.CustomerCheckoutActivity
import com.blueMarketing.capsula.utils.Constants
import com.google.gson.Gson
import io.reactivex.functions.Action
import org.greenrobot.eventbus.EventBus
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductDetailsActivity :
    BaseActivity<ActivityProductDetailsBinding, ProductDetailsViewModel>(),
    ProductDetailsNavigator {

    private val mViewModel: ProductDetailsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getIntentsData()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        mViewModel.cartResponse.observe(this, Observer {
            val user = UserDataSource.getUser()
            user?.cartContent = it
            UserDataSource.saveUser(user)
            EventBus.getDefault().postSticky(MessageEvent(Constants.UPDATE_CART_NUMBER))
            showPopUp(
                getString(R.string.cart), getString(R.string.product_added_to_cart),
                getString(android.R.string.ok), false
            )
        })

        mViewModel.productaddedMsg.observe(this, Observer {
            showPopUp(
                getString(R.string.cart), getString(R.string.product_added_to_cart),
                getString(android.R.string.ok), false
            )
        })

        mViewModel.differentProductMsg.observe(this, Observer {
            val message = getString(R.string.your_cart_contains) + " " + it + " " +
                    getString(R.string.do_u_wish)
            showPopUp("", message, R.string.new_order,
                DialogInterface.OnClickListener { _, _ ->
                    run {
                        if (UserDataSource.getUser() == null) {
                            UserDataSource.deleteCart()
                            UserDataSource.addProductToCart(mViewModel.product, Action {
                                openCheckout()
                            })
                        } else {
                            mViewModel.addProductToCart()
                        }
                    }
                }
                , getString(android.R.string.cancel), false)
        })
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
        mViewModel.product = Gson().fromJson(
            intent.getStringExtra(Constants.EXTRA_PRODUCT),
            Product::class.java
        )
        viewDataBinding?.product = mViewModel.product

        mViewModel.fromCart.set(
            intent.getIntExtra(Constants.FROM_WHERE, -1)
                    == Constants.FROM_CART
        )
    }

    override fun openCheckout() {
        startActivity(Intent(this, CustomerCheckoutActivity::class.java))
    }
}
