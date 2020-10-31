package com.blueMarketing.capsula.ui.deliveryMan.history

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import androidx.lifecycle.Observer
import com.blueMarketing.base.BaseActivity
import com.blueMarketing.base.BaseRecyclerAdapter
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.data.DeliveryOrder
import com.blueMarketing.capsula.databinding.ActivityHistoryBinding
import com.blueMarketing.capsula.ui.deliveryMan.deliveryHome.adapters.DeliveryOrdersAdapter
import com.blueMarketing.capsula.ui.deliveryMan.deliveryOrderDetails.DeliveryOrderDetailsActivity
import com.blueMarketing.capsula.utils.Constants
import com.blueMarketing.capsula.utils.MonthYearPickerDialog
import com.google.gson.Gson
import io.reactivex.functions.Action
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.activity_my_orders.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class HistoryActivity : BaseActivity<ActivityHistoryBinding, HistoryViewModel>(), HistoryNavigator,
    BaseRecyclerAdapter.OnITemClickListener<DeliveryOrder>, DatePickerDialog.OnDateSetListener {

    private val mViewModel: HistoryViewModel by viewModel()
    private val mAdapter: DeliveryOrdersAdapter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initRecyclerView()
        subscribeToLiveData()

    }

    override fun getMyViewModel(): HistoryViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_history
    }

    override fun init() {
        viewModel = mViewModel
        viewDataBinding?.vm = mViewModel
        viewDataBinding?.navigator = this
        mViewModel.navigator = this
        viewDataBinding?.noDataText = getString(R.string.no_history_found)
        paginateWithScrollView(history_scroll_layout, Action {
            mViewModel.pageNo++
            mViewModel.loadHistory()
        })
    }

    private fun subscribeToLiveData() {
        mViewModel.historyResponse.observe(this, Observer {
            if (it != null) {
                if (it.count > 0)
                    mViewModel.hasData.set(true)
                else
                    mViewModel.hasData.set(false)

                if (!it.ordersList.isNullOrEmpty())
                    mViewModel.ordersList.addAll(it.ordersList!!)
                if (mViewModel.ordersList.size == it.count)
                    mViewModel.isLastPage = true
                mAdapter.setData(mViewModel.ordersList)
            }
        })
    }

    private fun initRecyclerView() {
        history_recyclerView.adapter = mAdapter
        mAdapter.onITemClickListener = this
    }


    override fun backAction() {
        finish()
    }

    override fun openFilter() {
        val pd = MonthYearPickerDialog(this, mViewModel.currentMonth, mViewModel.currentYear)
        pd.setListener(this)
        pd.show(supportFragmentManager, "MonthYearPickerDialog")
    }

    override fun onItemClick(pos: Int, item: DeliveryOrder) {
        val intent = Intent(this, DeliveryOrderDetailsActivity::class.java)
        intent.putExtra(Constants.EXTRA_ORDER, Gson().toJson(item))
        intent.putExtra(Constants.FROM_HISTORY, true)
        startActivity(intent)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        mViewModel.currentMonth = month
        mViewModel.currentYear = year
        mViewModel.ordersList = ArrayList()
        mViewModel.pageNo = 1
        mAdapter.setData(ArrayList())
        mViewModel.loadHistory()
    }
}