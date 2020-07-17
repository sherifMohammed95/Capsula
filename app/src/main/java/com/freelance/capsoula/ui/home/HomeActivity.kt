package com.freelance.capsoula.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.freelance.base.BaseActivity
import com.freelance.base.BaseRecyclerAdapter
import com.freelance.capsoula.R
import com.freelance.capsoula.data.Store
import com.freelance.capsoula.data.source.local.UserDataSource
import com.freelance.capsoula.databinding.ActivityHomeBinding
import com.freelance.capsoula.ui.authentication.AuthenticationActivity
import com.freelance.capsoula.ui.brands.BrandsActivity
import com.freelance.capsoula.ui.categories.CategoriesActivity
import com.freelance.capsoula.ui.home.adapters.HomeBrandsAdapter
import com.freelance.capsoula.ui.home.adapters.HomeCategoriesAdapter
import com.freelance.capsoula.ui.home.adapters.HomeStoresAdapter
import com.freelance.capsoula.ui.products.ProductsActivity
import com.freelance.capsoula.ui.search.SearchActivity
import com.freelance.capsoula.ui.stores.StoresActivity
import com.freelance.capsoula.utils.AnimationUtils
import com.freelance.capsoula.utils.Constants
import kotlinx.android.synthetic.main.activity_home.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import android.os.Handler
import android.view.View
import com.freelance.capsoula.data.MessageEvent
import com.freelance.capsoula.ui.checkout.CheckoutActivity
import com.freelance.capsoula.ui.more.MoreActivity
import io.reactivex.functions.Action
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


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
    }

    override fun onResume() {
        super.onResume()
        mViewModel.updateToolbarData()
        if (UserDataSource.getUser() != null)
            mViewModel.loadUpdatedCart()
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
//        mViewModel.homeDataResponse.observe(this, Observer {
//            if (it != null) {
//                homeCategoriesAdapter.setData(it.categoriesData)
//                homeBrandsAdapter.setData(it.brandsData)
//                homeStoresAdapter.setData(it.storesData)
//            }
//        })

        mViewModel.storesResponse.observe(this, Observer {
            if (it.data?.storesList != null) {
                homeStoresAdapter.setData(it.data?.storesList!!)
            }
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
    }

    override fun openMore() {
        startActivity(Intent(this, MoreActivity::class.java))

    }

    override fun openCheckout() {
        startActivity(Intent(this, CheckoutActivity::class.java))
    }

}
