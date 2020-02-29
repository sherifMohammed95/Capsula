package com.freelance.capsoula.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.freelance.capsoula.R
import com.freelance.capsoula.data.source.local.UserDataSource
import com.freelance.capsoula.ui.addAddress.AddAddressActivity
import com.freelance.capsoula.ui.authentication.AuthenticationActivity
import com.freelance.capsoula.ui.completeProfile.CompleteProfileActivity
import com.freelance.capsoula.ui.home.HomeActivity
import com.freelance.capsoula.utils.Utils

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        openDesiredScreen()
        Utils.getHashKey(this)
    }

    private fun openDesiredScreen() {

        Handler().postDelayed({
            if (UserDataSource.getUser() == null)
                openAuthentication()
            else {
                when {
                    UserDataSource.getUser()!!.phone.isNullOrEmpty() -> openCompleteProfile()
                    UserDataSource.getUser()!!.userAddresses.isNullOrEmpty() -> openAddAddress()
                    else -> openHome()
                }
            }

        }, 2000)
    }

    private fun openAuthentication() {
        startActivity(Intent(this, AuthenticationActivity::class.java))
        finish()
    }

    private fun openCompleteProfile() {
        startActivity(Intent(this, CompleteProfileActivity::class.java))
        finish()
    }

    private fun openAddAddress() {
        startActivity(Intent(this, AddAddressActivity::class.java))
        finish()
    }

    private fun openHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}
