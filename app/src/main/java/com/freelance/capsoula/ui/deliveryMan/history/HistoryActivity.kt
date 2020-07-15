package com.freelance.capsoula.ui.deliveryMan.history

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import androidx.lifecycle.Observer
import com.freelance.base.BaseActivity
import com.freelance.base.BaseRecyclerAdapter
import com.freelance.capsoula.R
import com.freelance.capsoula.data.DeliveryOrder
import com.freelance.capsoula.databinding.ActivityHistoryBinding
import com.freelance.capsoula.ui.deliveryMan.deliveryHome.adapters.DeliveryOrdersAdapter
import com.freelance.capsoula.ui.deliveryMan.deliveryOrderDetails.DeliveryOrderDetailsActivity
import com.freelance.capsoula.utils.Constants
import com.freelance.capsoula.utils.MonthYearPickerDialog
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_history.*
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
    }

    private fun subscribeToLiveData() {
        mViewModel.historyResponse.observe(this, Observer {
            if (it.ordersList != null)
                mAdapter.setData(it.ordersList!!)
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
        mViewModel.loadHistory()
    }
}