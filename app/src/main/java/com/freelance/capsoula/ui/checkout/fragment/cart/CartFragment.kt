package com.freelance.capsoula.ui.checkout.fragment.cart

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.freelance.base.BaseFragment
import com.freelance.base.BaseRecyclerSwipeAdapter
import com.freelance.capsoula.R
import com.freelance.capsoula.data.MessageEvent
import com.freelance.capsoula.data.Order
import com.freelance.capsoula.data.Product
import com.freelance.capsoula.data.repository.CartRepository
import com.freelance.capsoula.data.source.local.UserDataSource
import com.freelance.capsoula.databinding.FragmentCartBinding
import com.freelance.capsoula.ui.authentication.AuthenticationActivity
import com.freelance.capsoula.ui.checkout.CheckoutActivity
import com.freelance.capsoula.ui.checkout.fragment.cart.adapters.CartAdapter
import com.freelance.capsoula.ui.productDetails.ProductDetailsActivity
import com.freelance.capsoula.utils.Constants
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_cart.*
import org.greenrobot.eventbus.EventBus
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module

val cartModule = module {
    factory { CartAdapter() }
    factory { CartRepository() }
}

class CartFragment : BaseFragment<FragmentCartBinding, CartViewModel>(), CartNavigator,
    CartAdapter.OnIconsClickListener, BaseRecyclerSwipeAdapter.OnITemClickListener<Product> {

    private val mViewModel: CartViewModel by viewModel()
    private val mAdapter: CartAdapter by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBundles()
        initRecyclerView()
//        mViewModel.fetchCartList()
        updateDataView()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {

        mViewModel.emptyCartEvent.observe(viewLifecycleOwner, Observer {
            showPopUp(
                getString(R.string.cart), getString(R.string.empty_cart_msg),
                getString(android.R.string.ok), false
            )
        })

        mViewModel.cartResponse.observe(viewLifecycleOwner, Observer {
            mViewModel.cartList = it!!
            val user = UserDataSource.getUser()
            user?.cartContent = mViewModel.cartList
            UserDataSource.saveUser(user)
            UserDataSource.deleteCart()
            updateDataView()
            EventBus.getDefault().postSticky(MessageEvent(Constants.UPDATE_CART_NUMBER))
        })

        mViewModel.deleteCartResponse.observe(viewLifecycleOwner, Observer {
            if (UserDataSource.getUser() == null) {
                UserDataSource.deleteCart()
            } else {
                val user = UserDataSource.getUser()
                user?.cartContent = ArrayList()
                UserDataSource.saveUser(user)
            }
            updateDataView()
        })

        mViewModel.deleteCartEvent.observe(viewLifecycleOwner, Observer {
            if (UserDataSource.getUser() == null) {
                UserDataSource.deleteCart()
                updateDataView()
                EventBus.getDefault().postSticky(MessageEvent(Constants.UPDATE_CART_NUMBER))
            } else {
                mViewModel.deleteCart()
            }
        })

        mViewModel.validCart.observe(viewLifecycleOwner, Observer {
            openDetailsStep()
        })

        mViewModel.differentProductMsg.observe(viewLifecycleOwner, Observer {
            val message = getString(R.string.your_cart_contains) + " " + it + " " +
                    getString(R.string.do_u_wish)
            showPopUp("", message, R.string.new_order,
                DialogInterface.OnClickListener { _, _ ->
                    run {
                       mViewModel.addProductsToCart()
                    }
                }
                , getString(android.R.string.cancel), false)
        })

    }

    override fun getMyViewModel(): CartViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_cart
    }

    private fun initRecyclerView() {
        cart_recyclerView.adapter = mAdapter
        mAdapter.onIconsClickListener = this
        mAdapter.onITemClickListener = this
    }

    override fun init() {
        viewDataBinding?.vm = mViewModel
        viewModel = mViewModel
        mViewModel.navigator = this
    }

    override fun onPlusClick(product: Product) {
        mViewModel.mProduct = product
        if (UserDataSource.getUser() == null) {
            UserDataSource.increaseProductQuantity(product)
            updateDataView()
        } else
            mViewModel.updateCart(1)
    }

    override fun onMinusClick(product: Product) {
        mViewModel.mProduct = product
        if (UserDataSource.getUser() == null) {
            UserDataSource.decreaseProductQuantity(product)
            updateDataView()
        } else
            mViewModel.updateCart(2)
    }

    override fun onDeleteClick(product: Product) {
        mViewModel.mProduct = product
        if (UserDataSource.getUser() == null) {
            UserDataSource.deleteProduct(product)
            updateDataView()
        } else
            mViewModel.deleteProductFromCart()
    }

    override fun onItemClick(pos: Int, item: Product) {
        val intent = Intent(activity, ProductDetailsActivity::class.java)
        intent.putExtra(Constants.EXTRA_PRODUCT, Gson().toJson(item))
        intent.putExtra(Constants.FROM_WHERE, Constants.FROM_CART)
        startActivity(intent)
    }

    override fun openAuthentication() {
        startActivity(Intent(activity, AuthenticationActivity::class.java))
    }

    override fun openDetailsStep() {
        (activity as CheckoutActivity).openDetailsFragment()
    }

    private fun updateDataView() {
        if (UserDataSource.getUser() == null)
            mViewModel.cartList = UserDataSource.getUserCart()
        else
            mViewModel.cartList = UserDataSource.getUser()?.cartContent!!

        mViewModel.itemsNumber.set(mViewModel.cartList.size.toString() + " " + getString(R.string.items))
        mAdapter.setData(mViewModel.cartList)
        mViewModel.calcTotalPrice()
    }

    override fun onResume() {
        super.onResume()
        (activity as CheckoutActivity).viewDataBinding?.toolbar?.progressBarImageView
            ?.setImageResource(R.drawable.cart_progress_bar)
    }


    companion object {
        fun newInstance(order: Order?) =
            CartFragment().apply {
                arguments = Bundle().apply {
                    putString(Constants.EXTRA_ORDER, Gson().toJson(order))
                }
            }
    }

    private fun getBundles() {
        if (arguments != null) {
            mViewModel.mOrder = Gson().fromJson(arguments?.getString(Constants.EXTRA_ORDER),
                Order::class.java)

            mViewModel.fetchCartList()
        }
    }
}