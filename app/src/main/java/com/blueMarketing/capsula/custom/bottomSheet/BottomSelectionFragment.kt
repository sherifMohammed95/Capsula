package com.blueMarketing.capsula.custom.bottomSheet

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blueMarketing.base.BaseBottomSheetFragment
import com.blueMarketing.base.BaseRecyclerAdapter
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.databinding.FragmentBottomSelectionBinding
import com.blueMarketing.capsula.ui.addAddress.AddAddressActivity
import com.blueMarketing.capsula.utils.Constants
import com.blueMarketing.capsula.utils.Constants.EXTRA_SELECTION_LIST
import com.blueMarketing.capsula.utils.Constants.EXTRA_SELECTION_TITLE
import com.blueMarketing.capsula.utils.Constants.FROM_CHECKOUT_DETAILS
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_bottom_selection.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import rx.functions.Action2

class BottomSelectionFragment :
    BaseBottomSheetFragment<FragmentBottomSelectionBinding, BottomSheetViewModel>(),
    BaseRecyclerAdapter.OnITemClickListener<BottomSheetModel>, BottomSheetNavigator {

    val mViewModel: BottomSheetViewModel by viewModel()
    private var title = ""
    private var list: List<BottomSheetModel> = ArrayList()
    private val mAdapter = BottomSheetAdapter()
    private var selectedPos = -1
    private var showClearText = true
    private var showAddNewAddressText = true
    private var showIconImage = true
    private lateinit var clickAction: Action2<Int, BottomSheetModel>

    override fun getViewModel(): BottomSheetViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_bottom_selection
    }

    override fun init() {
        mViewModel.navigator = this
        viewDataBinding!!.vm = mViewModel
        getBundles()
        initRecyclerView()
        title_textView.text = title
        if (this.showClearText)
            clear_textView.visibility = View.VISIBLE
        else
            clear_textView.visibility = View.INVISIBLE

        if (this.showAddNewAddressText)
            add_new_address_textView.visibility = View.VISIBLE
        else
            add_new_address_textView.visibility = View.INVISIBLE
    }


    override fun subscibeToLiveData() {

        apply_btn.setOnClickListener {
            if (mViewModel.mSelectedPos != -1) {
                clickAction.call(mViewModel.mSelectedPos, mViewModel.mBottomSheetModel)
                dismiss()
            }
        }

        clear_textView.setOnClickListener {
            mViewModel.mSelectedPos = -1
            mViewModel.mBottomSheetModel = BottomSheetModel()
            clickAction.call(mViewModel.mSelectedPos, mViewModel.mBottomSheetModel)
            dismiss()
        }

        add_new_address_textView.setOnClickListener {
            val intent  = Intent(activity,AddAddressActivity::class.java)
            intent.putExtra(Constants.FROM_WHERE, FROM_CHECKOUT_DETAILS)
            startActivity(intent)
            dismiss()
        }
    }

    private fun getBundles() {
        if (arguments != null) {
            title = arguments!!.getString(EXTRA_SELECTION_TITLE)!!
            list = Gson().fromJson(
                arguments!!.getString(EXTRA_SELECTION_LIST),
                object : TypeToken<List<BottomSheetModel>>() {}.type
            )
        }
    }

    private fun initRecyclerView() {
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = mAdapter
        mAdapter.selectedPos = this.selectedPos
        mAdapter.showIconImage = this.showIconImage
        mAdapter.setData(list)
        mAdapter.onITemClickListener = this
    }

    companion object {
        fun newInstance(
            selectionTitle: String, selectionList: List<BottomSheetModel>,
            clickAction: Action2<Int, BottomSheetModel>, selectedPos: Int, showClearText: Boolean,
            showIconImage: Boolean,showAddNewAddressText: Boolean
        ) =
            BottomSelectionFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_SELECTION_TITLE, selectionTitle)
                    putString(
                        EXTRA_SELECTION_LIST, Gson().toJson(
                            selectionList,
                            object : TypeToken<List<BottomSheetModel>>() {}.type
                        )
                    )
                }
                this.clickAction = clickAction
                this.selectedPos = selectedPos
                this.showClearText = showClearText
                this.showIconImage = showIconImage
                this.showAddNewAddressText = showAddNewAddressText
            }
    }

    override fun onItemClick(pos: Int, item: BottomSheetModel) {
        mViewModel.mSelectedPos = pos
        mViewModel.mBottomSheetModel = item
    }


    override fun finish() {
        dismiss()
    }

}