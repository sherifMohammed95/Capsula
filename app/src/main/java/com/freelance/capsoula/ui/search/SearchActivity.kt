package com.freelance.capsoula.ui.search

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.freelance.base.BaseActivity
import com.freelance.base.BaseRecyclerAdapter
import com.freelance.capsoula.R
import com.freelance.capsoula.custom.bottomSheet.BottomSelectionFragment
import com.freelance.capsoula.data.FilterType
import com.freelance.capsoula.data.Product
import com.freelance.capsoula.data.source.local.UserDataSource
import com.freelance.capsoula.databinding.ActivitySearchBinding
import com.freelance.capsoula.ui.productDetails.ProductDetailsActivity
import com.freelance.capsoula.ui.products.adapters.ProductsAdapter
import com.freelance.capsoula.utils.Constants
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_search.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import rx.functions.Action1
import rx.functions.Action2

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
                    mViewModel.filterType = item!!.id
                    mViewModel.pageNo = 1
                    mViewModel.getSearchResults()
                }
            }, mViewModel.selectedFilterTypePos)

        fragment.show(supportFragmentManager, fragment.tag)
    }

    override fun onPlusClick(product: Product) {
        if (UserDataSource.getUser() == null) {
            UserDataSource.addProductToCart(product)
            mViewModel.updateCartNumber()
        }
    }

    override fun onItemClick(pos: Int, item: Product) {
        val intent = Intent(this, ProductDetailsActivity::class.java)
        intent.putExtra(Constants.EXTRA_PRODUCT, Gson().toJson(item))
        startActivity(intent)
    }
}
