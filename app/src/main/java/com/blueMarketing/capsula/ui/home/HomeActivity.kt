package com.blueMarketing.capsula.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.blueMarketing.base.BaseActivity
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.data.source.local.UserDataSource
import com.blueMarketing.capsula.databinding.ActivityHomeBinding
import com.blueMarketing.capsula.ui.brands.BrandsActivity
import com.blueMarketing.capsula.ui.categories.CategoriesActivity
import com.blueMarketing.capsula.ui.checkout.CustomerCheckoutActivity
import com.blueMarketing.capsula.ui.home.adapters.HomeBrandsAdapter
import com.blueMarketing.capsula.ui.home.adapters.HomeCategoriesAdapter
import com.blueMarketing.capsula.ui.home.adapters.HomeStoresAdapter
import com.blueMarketing.capsula.ui.more.MoreActivity
import com.blueMarketing.capsula.ui.notifications.NotificationsActivity
import com.blueMarketing.capsula.ui.products.ProductsActivity
import com.blueMarketing.capsula.ui.search.SearchActivity
import com.blueMarketing.capsula.ui.stores.StoresActivity
import com.blueMarketing.capsula.utils.Constants
import io.reactivex.functions.Action
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.categories_recyclerView
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class HomeActivity : BaseActivity<ActivityHomeBinding, HomeViewModel>(), HomeNavigator {

    private val mViewModel: HomeViewModel by viewModel()
    private val homeStoresAdapter: HomeStoresAdapter by inject { parametersOf(this) }
    private val homeCategoriesAdapter: HomeCategoriesAdapter by inject { parametersOf(this) }
    private val homeBrandsAdapter: HomeBrandsAdapter by inject { parametersOf(this) }

    override fun getMyViewModel(): HomeViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_home
    }

    override fun init() {
        viewModel = mViewModel
        viewDataBinding?.vm = mViewModel
        mViewModel.navigator = this
        viewDataBinding?.navigator = this
        viewDataBinding?.noDataText = getString(R.string.no_stores_found)
        paginateWithScrollView(home_scroll_view, Action {
            mViewModel.pageNo++
            mViewModel.loadStores(false)
        })
        swipeToRefresh(home_refresh_layout, Action {
            mViewModel.isLastPage = false
            mViewModel.pageNo = 1
            mViewModel.storesList = ArrayList()
            mViewModel.loadStores(false)
        })
    }

    override fun onResume() {
        super.onResume()
        mViewModel.updateToolbarData()
        if (UserDataSource.getUser() != null) {
            mViewModel.notificationsIconVisibility.set(true)
            mViewModel.loadUserData()
        }

//        if (Constants.REFRESH_HOME) {
//            Constants.REFRESH_HOME = false
//            mViewModel.pageNo = 1
//            mViewModel.storesList = ArrayList()
//            if (mViewModel.storesList.size > 0)
//                mViewModel.loadStores(false)
//            else
//                mViewModel.loadStores(true)
//        }
    }

    private fun initRecyclerViews() {
        categories_recyclerView.adapter = homeCategoriesAdapter
        brands_recyclerView.adapter = homeBrandsAdapter
        stores_recyclerView.adapter = homeStoresAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initRecyclerViews()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {

        mViewModel.storesResponse.observe(this, Observer {
            viewModel?.isPagingLoadingEvent?.value = false
            home_refresh_layout.isRefreshing = false

            if (it.data!!.count > 0)
                mViewModel.hasData.set(true)
            else
                mViewModel.hasData.set(false)

            if (it.data != null && !it.data?.storesList.isNullOrEmpty()) {
                if (mViewModel.pageNo == 1)
                    mViewModel.storesList = it.data!!.storesList!!
                else
                    mViewModel.storesList.addAll(it.data!!.storesList!!)

                if (mViewModel.storesList.size == it.data!!.count)
                    mViewModel.isLastPage = true
            }
            homeStoresAdapter.setData(mViewModel.storesList)
        })

        mViewModel.emptyCartMessage.observe(this, Observer {
            showPopUp(
                getString(R.string.cart), getString(R.string.empty_cart_msg),
                getString(android.R.string.ok), false
            )
        })
    }

    override fun openAllCategories() {
        startActivity(Intent(this, CategoriesActivity::class.java))
    }

    override fun openAllBrands() {
        startActivity(Intent(this, BrandsActivity::class.java))
    }

    override fun openAllStores() {
        startActivity(Intent(this, StoresActivity::class.java))
    }

    override fun openBestSeller() {
        val intent = Intent(this, ProductsActivity::class.java)
        intent.putExtra(Constants.FROM_WHERE, Constants.FROM_BEST_SELLER)
        startActivity(intent)
    }

    override fun openTopRated() {
        val intent = Intent(this, ProductsActivity::class.java)
        intent.putExtra(Constants.FROM_WHERE, Constants.FROM_TOP_RATED)
        startActivity(intent)
    }

    override fun openFreeDelivery() {
        val intent = Intent(this, ProductsActivity::class.java)
        intent.putExtra(Constants.FROM_WHERE, Constants.FROM_FREE_DELIVERY)
        startActivity(intent)
    }

    override fun openSearch() {
        startActivity(Intent(this, SearchActivity::class.java))
    }

    override fun openNotifications() {
        startActivity(Intent(this, NotificationsActivity::class.java))
    }

    override fun openMore() {
        startActivity(Intent(this, MoreActivity::class.java))

    }

    override fun openCheckout() {
        startActivity(Intent(this, CustomerCheckoutActivity::class.java))
    }

}
