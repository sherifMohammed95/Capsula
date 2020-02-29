package com.freelance.capsoula.ui.categories

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.freelance.base.BaseActivity
import com.freelance.base.BaseRecyclerAdapter
import com.freelance.capsoula.R
import com.freelance.capsoula.data.Category
import com.freelance.capsoula.data.repository.CategoriesRepository
import com.freelance.capsoula.databinding.ActivityCategoriesBinding
import com.freelance.capsoula.ui.categories.adapters.CategoriesAdapter
import com.freelance.capsoula.ui.subCategories.SubCategoriesActivity
import com.freelance.capsoula.utils.Constants
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_categories.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module
import rx.functions.Action1

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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolbar(getString(R.string.categories))
        getIntentsData()
        initRecyclerView()
        subscribeToLiveData()

        if (mViewModel.storeId == -1)
            mViewModel.getCategories()
        else
            mViewModel.getStoreCategories()
    }

    private fun getIntentsData(){
        mViewModel.storeId = intent.getIntExtra(Constants.EXTRA_STORE_ID,-1)
    }


    private fun subscribeToLiveData() {
        mViewModel.categoriesResponse.observe(this, Observer {
            if (it.data != null) {
                if (!it.data!!.categoriesList.isNullOrEmpty())
                    mAdapter.setData(it.data!!.categoriesList!!)
            }
        })

        mViewModel.storeCategoriesResponse.observe(this, Observer {
            if (it.data != null) {
                if (!it.data!!.categoriesList.isNullOrEmpty())
                    mAdapter.setData(it.data!!.categoriesList!!)
            }
        })
    }

    private fun initRecyclerView() {
        categories_recyclerView.adapter = mAdapter
        mAdapter.onITemClickListener = this
    }

    override fun onItemClick(pos: Int, item: Category) {
        val intent  = Intent(this,SubCategoriesActivity::class.java)
        intent.putExtra(Constants.EXTRA_CATEGORY,Gson().toJson(item))
        startActivity(intent)
    }
}
