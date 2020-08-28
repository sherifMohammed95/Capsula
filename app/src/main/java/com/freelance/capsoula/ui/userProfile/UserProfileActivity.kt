package com.freelance.capsoula.ui.userProfile

import android.content.Intent
import android.os.Bundle
import com.freelance.base.BaseActivity
import com.freelance.capsoula.R
import com.freelance.capsoula.data.source.local.UserDataSource
import com.freelance.capsoula.databinding.ActivityUserProfileBinding
import com.freelance.capsoula.ui.editProfile.EditProfileActivity
import com.freelance.capsoula.utils.preferencesGateway
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