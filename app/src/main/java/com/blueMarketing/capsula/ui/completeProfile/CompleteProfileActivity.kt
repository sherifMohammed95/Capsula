package com.blueMarketing.capsula.ui.completeProfile

import android.content.Intent
import android.os.Bundle
import com.blueMarketing.base.BaseActivity
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.data.source.local.UserDataSource
import com.blueMarketing.capsula.databinding.ActivityCompleteProfileBinding
import com.blueMarketing.capsula.ui.terms.TermsActivity
import com.blueMarketing.capsula.ui.userTypes.UserTypesActivity
import com.blueMarketing.capsula.ui.verification.VerificationActivity
import com.blueMarketing.capsula.utils.Constants
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class CompleteProfileActivity :
    BaseActivity<ActivityCompleteProfileBinding, CompleteProfileViewModel>(),
    CompleteProfileNavigator {

    private val mViewModel: CompleteProfileViewModel by viewModel()

    override fun getMyViewModel(): CompleteProfileViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_complete_profile
    }

    override fun init() {
        viewModel = mViewModel
        mViewModel.navigator = this
        viewDataBinding?.vm = mViewModel
        viewDataBinding?.backImageView!!.setOnClickListener {
            openUserTypes()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun openVerification() {
        val intent = Intent(this, VerificationActivity::class.java)
        intent.putExtra(Constants.EXTRA_COMPLETE_PROFILE_REQUEST, Gson().toJson(mViewModel.request))
        intent.putExtra(Constants.EXTRA_REGISTER_REQUEST, Gson().toJson(null))
        intent.putExtra(Constants.EXTRA_PHONE, "")
        intent.putExtra(Constants.FROM_WHERE, Constants.COMPLETE_PROFILE_SCREEN)
        startActivity(intent)
    }

    override fun openTerms() {
        startActivity(Intent(this, TermsActivity::class.java))
    }

    override fun openUserTypes() {
        UserDataSource.saveUser(null)
        val intent = Intent(this, UserTypesActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    override fun onBackPressed() {
        openUserTypes()
    }
}
