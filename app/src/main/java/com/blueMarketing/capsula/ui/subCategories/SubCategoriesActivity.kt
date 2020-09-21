package com.blueMarketing.capsula.ui.subCategories

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.blueMarketing.base.BaseActivity
import com.blueMarketing.base.BaseRecyclerAdapter
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.data.Category
import com.blueMarketing.capsula.databinding.ActivitySubCategoriesBinding
import com.blueMarketing.capsula.ui.products.ProductsActivity
import com.blueMarketing.capsula.ui.subCategories.adapters.SubCategoriesAdapter
import com.blueMarketing.capsula.utils.Constants
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_sub_categories.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module

val subCategoriesModule = module {
    factory { SubCategoriesAdapter() }
}

class SubCategoriesActivity : BaseActivity<ActivitySubCategoriesBinding, SubCategoriesViewModel>(),
    SubCategoriesNavigator, BaseRecyclerAdapter.OnITemClickListener<Category> {

    private val mViewModel: SubCategoriesViewModel by viewModel()
    private val mAdapter: SubCategoriesAdapter by inject()

    override fun getMyViewModel(): SubCategoriesViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_sub_categories
    }

    override fun init() {
        viewModel = mViewModel
        viewDataBinding?.vm = mViewModel
        mViewModel.navigator = this
    }

    private fun getIntentsData() {
        mViewModel.mCategory = Gson().fromJson(
            intent.getStringExtra(Constants.EXTRA_CATEGORY),
            Category::class.java
        )

        mViewModel.storeId = intent.getIntExtra(Constants.EXTRA_STORE_ID, -1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getIntentsData()
        initRecyclerView()
        setUpToolbar(mViewModel.mCategory.categoryName)
        subscribeToLiveData()

        if (mViewModel.storeId == -1)
            mViewModel.getSubCategories()
        else
            mViewModel.getStoreSubCategories()
    }

    private fun initRecyclerView() {
        sub_categories_recyclerView.adapter = mAdapter
        mAdapter.onITemClickListener = this
    }

    private fun subscribeToLiveData() {
        mViewModel.subCategoriesResponse.observe(this, Observer {
            if (it.data != null) {
                if (!it.data!!.categoriesList.isNullOrEmpty())
                    mAdapter.setData(it.data!!.categoriesList!!)
            }
        })
    }

    override fun onItemClick(pos: Int, item: Category) {
        val intent = Intent(this, ProductsActivity::class.java)
        intent.putExtra(Constants.EXTRA_SUB_CATEGORY, Gson().toJson(item))
        intent.putExtra(Constants.FROM_WHERE, Constants.FROM_SUB_CATEGORIES)
        intent.putExtra(Constants.EXTRA_STORE_ID, mViewModel.storeId)
        startActivity(intent)
    }
}
