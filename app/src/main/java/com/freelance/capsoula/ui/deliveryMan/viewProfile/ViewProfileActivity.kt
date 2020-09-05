package com.freelance.capsoula.ui.deliveryMan.viewProfile

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.freelance.base.BaseActivity
import com.freelance.capsoula.R
import com.freelance.capsoula.databinding.ActivityViewProfileBinding
import com.freelance.capsoula.ui.deliveryMan.viewProfile.adapters.ViewPagerAdapter
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val viewDeliveryProfileModule = module {
    factory { (fm: FragmentManager) -> ViewPagerAdapter(fm) }
}

class ViewProfileActivity : BaseActivity<ActivityViewProfileBinding,ViewProfileViewModel>(),
    ViewProfileNavigator {

    private val mViewModel:ViewProfileViewModel by viewModel()
    private val mAdapter: ViewPagerAdapter by inject { parametersOf(supportFragmentManager) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun getMyViewModel(): ViewProfileViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_view_profile
    }

    override fun init() {
        viewModel = mViewModel
        viewDataBinding?.vm = mViewModel
        viewDataBinding?.navigator = this
        mViewModel.navigator = this
        viewDataBinding?.viewPager?.adapter = mAdapter
    }

    override fun backAction() {
        finish()
    }

    override fun openEditMode() {

    }
}