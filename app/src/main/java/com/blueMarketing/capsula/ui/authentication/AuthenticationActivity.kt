package com.blueMarketing.capsula.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.blueMarketing.base.BaseActivity
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.data.repository.AuthenticationRepository
import com.blueMarketing.capsula.data.source.local.UserDataSource
import com.blueMarketing.capsula.databinding.ActivityAuthenticationBinding
import com.blueMarketing.capsula.ui.home.HomeActivity
import com.blueMarketing.capsula.ui.addAddress.AddAddressActivity
import com.blueMarketing.capsula.ui.authentication.fragments.LoginFragment
import com.blueMarketing.capsula.ui.authentication.fragments.RegisterFragment
import com.blueMarketing.capsula.ui.completeProfile.CompleteProfileActivity
import com.blueMarketing.capsula.ui.forgetPassword.ForgetPasswordActivity
import com.blueMarketing.capsula.ui.terms.TermsActivity
import com.blueMarketing.capsula.ui.verification.VerificationActivity
import com.blueMarketing.capsula.utils.Constants
import com.blueMarketing.capsula.utils.SocialNetworkUtils
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_authentication.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module
import rx.functions.Action1
import timber.log.Timber


val authModule = module {
    factory { AuthenticationRepository() }
}

class AuthenticationActivity :
    BaseActivity<ActivityAuthenticationBinding, AuthenticationViewModel>(),
    AuthenticationNavigator {

    private val RC_SIGN_IN = 7
    private val mViewModel: AuthenticationViewModel by viewModel()
    private val socialUtils = SocialNetworkUtils()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getIntentsData()
        openRegister()
        subscribeToLiveData()

        if (mViewModel.fromWhere == Constants.FROM_MORE)
            skip_textView.visibility = View.INVISIBLE
        else
            skip_textView.visibility = View.VISIBLE
    }

    private fun getIntentsData() {
        mViewModel.fromWhere = intent.getIntExtra(Constants.FROM_WHERE, -1)
    }

    override fun getMyViewModel(): AuthenticationViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_authentication
    }

    override fun init() {
        viewModel = mViewModel
        viewDataBinding?.vm = mViewModel
        mViewModel.navigator = this
    }

    override fun signInWithGoogle() {
        val signInIntent = socialUtils.initGoogleSignInClient(this).signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun signInWithFaceBook() {
        socialUtils.loginWithFacebook(this, Action1 { fbToken ->
            mViewModel.fbToken = fbToken
            mViewModel.loginWithFaceBook()
        }, Action1 { error ->
            Toast.makeText(this, error, Toast.LENGTH_LONG).show()
        })
    }

    override fun signInWithTwitter() {
        Toast.makeText(this, "Under Development", Toast.LENGTH_LONG).show()
    }

    override fun openLogin() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, LoginFragment())
            .commit()
    }

    override fun openRegister() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, RegisterFragment())
            .commit()
    }

    override fun openForgetPassword() {
        val intent = Intent(this, ForgetPasswordActivity::class.java)
        intent.putExtra(Constants.IS_DELIVERY, false)
        startActivity(intent)
    }

    override fun openTerms() {
        startActivity(Intent(this, TermsActivity::class.java))
    }

    override fun openVerification() {
        val intent = Intent(this, VerificationActivity::class.java)
        intent.putExtra(Constants.EXTRA_REGISTER_REQUEST, Gson().toJson(mViewModel.registerRequest))
        intent.putExtra(Constants.EXTRA_COMPLETE_PROFILE_REQUEST, Gson().toJson(null))
        intent.putExtra(Constants.EXTRA_PHONE, "")
        intent.putExtra(Constants.FROM_WHERE, Constants.REGISTER_SCREEN)
        startActivity(intent)
    }

    override fun openHome() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    override fun openAddAddress() {
        startActivity(Intent(this, AddAddressActivity::class.java))
        finish()
    }

    override fun openCompleteProfile() {
        startActivity(Intent(this, CompleteProfileActivity::class.java))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleGoogleSignInResult(task)
        } else {
            socialUtils.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            mViewModel.googleSignInAccount = account
            mViewModel.loginWithGoogle()
        } catch (e: ApiException) {
            Timber.e("signInResult:failed code=%s", e.statusCode)
        }
    }

    private fun subscribeToLiveData() {
        mViewModel.socialMediaResponse.observe(this, Observer {
            when {
                UserDataSource.getUser()!!.phone.isNullOrEmpty() -> openCompleteProfile()
                UserDataSource.getUser()!!.userAddresses.isNullOrEmpty() -> openAddAddress()
                else -> openHome()
            }
        })

        mViewModel.loginResponse.observe(this, Observer {
            if (UserDataSource.getUser()!!.userAddresses.isNullOrEmpty())
                openAddAddress()
            else
                openHome()
        })

        mViewModel.userIsNotExistResponse.observe(this, Observer {
            mViewModel.buildRegisterRequest()
            openVerification()
        })
        mViewModel.userIsExistResponse.observe(this, Observer {
            showPopUp("", it, getString(android.R.string.ok), false)
        })

    }
}
