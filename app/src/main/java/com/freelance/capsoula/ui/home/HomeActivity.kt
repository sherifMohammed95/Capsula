package com.freelance.capsoula.ui.home

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
import com.freelance.capsoula.utils.Constants
import kotlinx.android.synthetic.main.activity_home.*
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
    }

    override fun onResume() {
        super.onResume()
        if(UserDataSource.getUser()!=null)
        viewDataBinding?.toolbar?.userNameTextView?.text = UserDataSource.getUser()?.name
        else
            viewDataBinding?.toolbar?.userNameTextView?.text = "User"
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

//        viewDataBinding?.toolbar?.cartImageView?.setOnClickListener {
//            UserDataSource.saveUser(null)
//            val intent = Intent(this,AuthenticationActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//            startActivity(intent)
//        }
    }

    private fun subscribeToLiveData() {
        mViewModel.homeDataResponse.observe(this, Observer {
            if (it != null) {
                homeCategoriesAdapter.setData(it.categoriesData)
                homeBrandsAdapter.setData(it.brandsData)
                homeStoresAdapter.setData(it.storesData)
            }
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
    }

    override fun openSearch() {
        startActivity(Intent(this, SearchActivity::class.java))
    }

    override fun openNotifications() {
    }

    override fun openMore() {
    }

}
