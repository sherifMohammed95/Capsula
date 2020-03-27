package com.freelance.capsoula.ui.checkout.fragment.cart

import android.os.Bundle
import android.view.View
import com.freelance.base.BaseFragment
import com.freelance.capsoula.R
import com.freelance.capsoula.data.Product
import com.freelance.capsoula.data.source.local.UserDataSource
import com.freelance.capsoula.databinding.FragmentCartBinding
import com.freelance.capsoula.ui.checkout.fragment.cart.adapters.CartAdapter
import kotlinx.android.synthetic.main.fragment_cart.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module

val cartModule = module {
    factory { CartAdapter() }
}

class CartFragment : BaseFragment<FragmentCartBinding, CartViewModel>(), CartNavigator {

    private val mViewModel: CartViewModel by viewModel()
    private val mAdapter: CartAdapter by inject()
    private var cartList = ArrayList<Product>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cart_recyclerView.adapter = mAdapter
        fetchCartList()
        mViewModel.itemsNumber.set(cartList.size.toString() + " " + getString(R.string.items))
        mAdapter.setData(cartList)
    }

    override fun getMyViewModel(): CartViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_cart
    }

    override fun init() {
        viewDataBinding?.vm = mViewModel
        viewModel = mViewModel
        mViewModel.navigator = this

    }

    private fun fetchCartList() {
        if (UserDataSource.getUser() == null) {
            cartList = UserDataSource.getUserCart()
        } else {
            if (UserDataSource.getUserCartSize() > 0) {
                // todo call add products to cart api
            } else
                cartList = UserDataSource.getUser()?.cartContent!!

        }
    }
}