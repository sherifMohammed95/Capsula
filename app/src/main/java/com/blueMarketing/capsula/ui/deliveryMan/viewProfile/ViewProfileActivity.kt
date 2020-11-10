package com.blueMarketing.capsula.ui.deliveryMan.viewProfile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.blueMarketing.base.BaseActivity
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.data.source.local.UserDataSource
import com.blueMarketing.capsula.databinding.ActivityViewProfileBinding
import com.blueMarketing.capsula.ui.deliveryMan.editDeliveryProfile.EditDeliveryProfileActivity
import com.blueMarketing.capsula.ui.deliveryMan.viewProfile.adapters.ViewPagerAdapter
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val viewDeliveryProfileModule = module {
    factory { (fm: FragmentManager) -> ViewPagerAdapter(fm) }
}

class ViewProfileActivity : BaseActivity<ActivityViewProfileBinding, ViewProfileViewModel>(),
    ViewProfileNavigator {

    private val mViewModel: ViewProfileViewModel by viewModel()
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
        viewDataBinding?.user = UserDataSource.getDeliveryUser()
    }

    override fun backAction() {
        finish()
    }

    override fun openEditMode() {
        startActivity(Intent(this, EditDeliveryProfileActivity::class.java))
    }
}