package com.freelance.capsoula.ui.search

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.freelance.base.BaseActivity
import com.freelance.base.BaseRecyclerAdapter
import com.freelance.capsoula.R
import com.freelance.capsoula.custom.bottomSheet.BottomSelectionFragment
import com.freelance.capsoula.data.FilterType
import com.freelance.capsoula.data.MessageEvent
import com.freelance.capsoula.data.Product
import com.freelance.capsoula.data.source.local.UserDataSource
import com.freelance.capsoula.databinding.ActivitySearchBinding
import com.freelance.capsoula.ui.checkout.CheckoutActivity
import com.freelance.capsoula.ui.productDetails.ProductDetailsActivity
import com.freelance.capsoula.ui.products.adapters.ProductsAdapter
import com.freelance.capsoula.utils.Constants
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_search.*
import org.greenrobot.eventbus.EventBus
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import rx.functions.Action1
import rx.functions.Action2
import java.util.zip.CheckedOutputStream

class SearchActivity : BaseActivity<ActivitySearchBinding, SearchViewModel>(), SearchNavigator,
    ProductsAdapter.OnPlusClickListener, BaseRecyclerAdapter.OnITemClickListener<Product> {

    private val mViewModel: SearchViewModel by viewModel()
    private val mAdapter: ProductsAdapter by inject()
    private val mFilterTypeList: ArrayList<FilterType> by inject(named(FILTER_TYPE_LIST))

    override fun getMyViewModel(): SearchViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_search
    }

    override fun onResume() {
        super.onResume()
        mViewModel.updateCartNumber()
    }

    override fun init() {
        viewModel = mViewModel
        viewDataBinding?.vm = mViewModel
        mViewModel.navigator = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initRecyclerView()
        subscribeToLiveData()
        setUpSearchToolbar(Action1 {
            mViewModel.pageNo = 1
            mViewModel.searchText = it
            mViewModel.getSearchResults()
        })
    }

    private fun subscribeToLiveData() {
        mViewModel.searchResultsResponse.observe(this, Observer {
            if (it.data != null) {
                if (it.data!!.productsList != null)
                    mAdapter.setData(it.data!!.productsList!!)
            }
        })

        mViewModel.cartResponse.observe(this, Observer {
            val user = UserDataSource.getUser()
            user?.cartContent = it
            UserDataSource.saveUser(user)
            mViewModel.updateCartNumber()
            EventBus.getDefault().postSticky(MessageEvent(Constants.UPDATE_CART_NUMBER))
        })

        mViewModel.emptyCartMessage.observe(this, Observer {
            showPopUp(
                getString(R.string.cart), getString(R.string.empty_cart_msg),
                getString(android.R.string.ok), false
            )
        })
    }

    private fun initRecyclerView() {
        results_recyclerView.adapter = mAdapter
        mAdapter.onPlusClickListener = this
        mAdapter.onITemClickListener = this
    }

    override fun openFilterList() {
        val fragment: BottomSelectionFragment = BottomSelectionFragment
            .newInstance(getString(R.string.sort_by), mFilterTypeList, Action2 { pos, item ->
                run {
                    mViewModel.selectedFilterTypePos = pos
                    mViewModel.filterType = item!!.selectionID!!
                    mViewModel.pageNo = 1
                    mViewModel.getSearchResults()
                }
            }, mViewModel.selectedFilterTypePos, showClearText = true, showIconImage = false)

        fragment.show(supportFragmentManager, fragment.tag)
    }

    override fun onPlusClick(product: Product) {
        mViewModel.mProduct = product
        if (UserDataSource.getUser() == null) {
            UserDataSource.addProductToCart(product)
            mViewModel.updateCartNumber()
        } else {
            if(UserDataSource.checkProductExistInCart(UserDataSource.getUser()?.cartContent!!,
                    product)) {
                openCheckout()
            } else
                mViewModel.addProductToCart()
        }
    }

    override fun onItemClick(pos: Int, item: Product) {
        val intent = Intent(this, ProductDetailsActivity::class.java)
        intent.putExtra(Constants.EXTRA_PRODUCT, Gson().toJson(item))
        startActivity(intent)
    }

    override fun openCheckout() {
        startActivity(Intent(this, CheckoutActivity::class.java))
    }
}
