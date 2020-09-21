package com.blueMarketing.capsula.ui.userProfile

import android.content.Intent
import android.os.Bundle
import com.blueMarketing.base.BaseActivity
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.data.source.local.UserDataSource
import com.blueMarketing.capsula.databinding.ActivityUserProfileBinding
import com.blueMarketing.capsula.ui.editProfile.EditProfileActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserProfileActivity : BaseActivity<ActivityUserProfileBinding, UserProfileViewModel>(),
    UserProfileNavigator {

    private val mViewModel: UserProfileViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        val user = UserDataSource.getUser()
        viewDataBinding?.user = user
    }

    override fun getMyViewModel(): UserProfileViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_user_profile
    }

    override fun init() {
        viewModel = mViewModel
        viewDataBinding?.vm = mViewModel
        viewDataBinding?.navigator = this
        mViewModel.navigator = this
    }

    override fun backAction() {
        finish()
    }

    override fun openEditMode() {
        startActivity(Intent(this, EditProfileActivity::class.java))
    }
}