package com.blueMarketing.capsula.ui.categories

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.blueMarketing.base.BaseActivity
import com.blueMarketing.base.BaseRecyclerAdapter
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.data.Category
import com.blueMarketing.capsula.data.repository.CategoriesRepository
import com.blueMarketing.capsula.databinding.ActivityCategoriesBinding
import com.blueMarketing.capsula.ui.categories.adapters.CategoriesAdapter
import com.blueMarketing.capsula.ui.products.ProductsActivity
import com.blueMarketing.capsula.utils.Constants
import com.google.gson.Gson
import io.reactivex.functions.Action
import kotlinx.android.synthetic.main.activity_categories.*
import kotlinx.android.synthetic.main.activity_products.*
import kotlinx.android.synthetic.main.activity_search.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module

val categoriesModule = module {
    factory { CategoriesRepository() }
    factory { CategoriesAdapter() }
}

class CategoriesActivity : BaseActivity<ActivityCategoriesBinding, CategoriesViewModel>(),
    CategoriesNavigator, BaseRecyclerAdapter.OnITemClickListener<Category> {

    private val mViewModel: CategoriesViewModel by viewModel()
    private val mAdapter: CategoriesAdapter by inject()

    override fun getMyViewModel(): CategoriesViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_categories
    }

    override fun init() {
        viewModel = mViewModel
        viewDataBinding?.vm = mViewModel
        mViewModel.navigator = this
        viewDataBinding?.noDataText = getString(R.string.no_cat_found)
        paginateWithScrollView(categories_scroll_layout, Action {
            mViewModel.pageNo++
            mViewModel.getStoreCategories(false)
        })
        swipeToRefresh(categories_refresh_layout, Action {
            mViewModel.isLastPage = false
            mViewModel.pageNo = 1
            mViewModel.storeCategoriesList = ArrayList()
            mViewModel.getStoreCategories(false)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolbar(getString(R.string.categories))
        getIntentsData()
        initRecyclerView()
        subscribeToLiveData()

        mViewModel.getStoreCategories(true)
    }

    private fun getIntentsData() {
        mViewModel.storeId = intent.getIntExtra(Constants.EXTRA_STORE_ID, -1)
    }


    private fun subscribeToLiveData() {

        mViewModel.storeCategoriesResponse.observe(this, Observer {
            viewModel?.isPagingLoadingEvent?.value = false
            categories_refresh_layout.isRefreshing = false
            if (it.data != null) {

                if (it.data!!.count > 0)
                    mViewModel.hasData.set(true)
                else
                    mViewModel.hasData.set(false)

                if (!it.data!!.categoriesList.isNullOrEmpty()) {
                    if (mViewModel.pageNo == 1)
                        mViewModel.storeCategoriesList = it.data!!.categoriesList!!
                    else
                        mViewModel.storeCategoriesList.addAll(it.data!!.categoriesList!!)

                    if (mViewModel.storeCategoriesList.size == it.data!!.count)
                        mViewModel.isLastPage = true
                    mAdapter.setData(mViewModel.storeCategoriesList)
                }
            }
        })
    }

    private fun initRecyclerView() {
        categories_recyclerView.adapter = mAdapter
        mAdapter.onITemClickListener = this
    }

    override fun onItemClick(pos: Int, item: Category) {
        val intent = Intent(this, ProductsActivity::class.java)
        intent.putExtra(Constants.EXTRA_CATEGORY, Gson().toJson(item))
        intent.putExtra(Constants.EXTRA_STORE_ID, mViewModel.storeId)
        startActivity(intent)
    }
}
