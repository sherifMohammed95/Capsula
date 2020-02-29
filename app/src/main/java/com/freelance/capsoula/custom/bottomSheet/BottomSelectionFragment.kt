package com.freelance.capsoula.custom.bottomSheet

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.freelance.base.BaseBottomSheetFragment
import com.freelance.base.BaseRecyclerAdapter
import com.freelance.capsoula.R
import com.freelance.capsoula.databinding.FragmentBottomSelectionBinding
import com.freelance.capsoula.utils.Constants.EXTRA_SELECTION_LIST
import com.freelance.capsoula.utils.Constants.EXTRA_SELECTION_TITLE
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

    }


    override fun subscibeToLiveData() {

        apply_btn.setOnClickListener {
            clickAction.call(mViewModel.mSelectedPos, mViewModel.mBottomSheetModel)
            dismiss()
        }

        clear_textView.setOnClickListener{
            mViewModel.mSelectedPos = -1
            mViewModel.mBottomSheetModel = BottomSheetModel()
            clickAction.call(mViewModel.mSelectedPos, mViewModel.mBottomSheetModel)
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
        mAdapter.setData(list)
        mAdapter.selectedPos = this.selectedPos
        mAdapter.onITemClickListener = this
    }

    companion object {
        fun newInstance(
            selectionTitle: String, selectionList: List<BottomSheetModel>,
            clickAction: Action2<Int, BottomSheetModel>, selectedPos: Int
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