package com.blueMarketing.capsula.ui.search

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.blueMarketing.base.BaseActivity
import com.blueMarketing.base.BaseRecyclerAdapter
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.custom.bottomSheet.BottomSelectionFragment
import com.blueMarketing.capsula.data.FilterType
import com.blueMarketing.capsula.data.MessageEvent
import com.blueMarketing.capsula.data.Product
import com.blueMarketing.capsula.data.source.local.UserDataSource
import com.blueMarketing.capsula.databinding.ActivitySearchBinding
import com.blueMarketing.capsula.ui.checkout.CheckoutActivity
import com.blueMarketing.capsula.ui.productDetails.ProductDetailsActivity
import com.blueMarketing.capsula.ui.products.adapters.ProductsAdapter
import com.blueMarketing.capsula.utils.Constants
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_search.*
import org.greenrobot.eventbus.EventBus
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named
import rx.functions.Action1
import rx.functions.Action2
import io.reactivex.functions.Action

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
        viewDataBinding?.noDataText = getString(R.string.no_results)
        paginateWithScrollView(search_scroll_view, Action {
            mViewModel.pageNo++
            mViewModel.getSearchResults()
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initRecyclerView()
        subscribeToLiveData()
        setUpSearchToolbar(Action1 {
            mViewModel.pageNo = 1
            mViewModel.searchText = it
            mViewModel.searchResultList = ArrayList()
            mViewModel.isLastPage = false
            mViewModel.getSearchResults()
        })
    }

    private fun subscribeToLiveData() {
        mViewModel.searchResultsResponse.observe(this, Observer {
            if (it.data != null) {

                if (it.data!!.count > 0)
                    mViewModel.hasData.set(true)
                else
                    mViewModel.hasData.set(false)

                if (it.data!!.productsList != null) {
                    mViewModel.searchResultList.addAll(it.data!!.productsList!!)
                    if (mViewModel.searchResultList.size == it.data!!.count)
                        mViewModel.isLastPage = true
                    mAdapter.setData(mViewModel.searchResultList)
                }
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
            .newInstance(
                getString(R.string.sort_by), mFilterTypeList, Action2 { pos, item ->
                    run {
                        mViewModel.selectedFilterTypePos = pos
                        mViewModel.filterType = item!!.selectionID!!
                        mViewModel.pageNo = 1
                        mViewModel.searchResultList = ArrayList()
                        mViewModel.isLastPage = false
                        mViewModel.getSearchResults()
                    }
                }, mViewModel.selectedFilterTypePos, showClearText = true, showIconImage = false,
                showAddNewAddressText = false
            )

        fragment.show(supportFragmentManager, fragment.tag)
    }

    override fun onPlusClick(product: Product) {
        mViewModel.mProduct = product
        val storeName = UserDataSource.checkCartFromTheSameStore(product)
        if (UserDataSource.getUser() == null) {
            if (storeName.contentEquals("")) {
                UserDataSource.addProductToCart(product)
                mViewModel.updateCartNumber()
            } else {
                val message = getString(R.string.your_cart_contains) + " " + storeName + " " +
                        getString(R.string.do_u_wish)
                showPopUp("", message, R.string.new_order,
                    DialogInterface.OnClickListener { _, _ ->
                        UserDataSource.deleteCart()
                        UserDataSource.addProductToCart(product)
                    }
                    , getString(android.R.string.cancel), false)
            }
        } else {
            if (UserDataSource.checkProductExistInCart(
                    UserDataSource.getUser()?.cartContent!!,
                    product
                )
            ) {
                openCheckout()
            } else {
                if (storeName.contentEquals(""))
                    mViewModel.addProductToCart()
                else {
                    val message = getString(R.string.your_cart_contains) + " " + storeName + " " +
                            getString(R.string.do_u_wish)
                    showPopUp("", message, R.string.new_order,
                        DialogInterface.OnClickListener { _, _ ->
                            mViewModel.addProductToCart()
                        }
                        , getString(android.R.string.cancel), false)
                }
            }
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
