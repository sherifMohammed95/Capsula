package com.freelance.capsoula.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.freelance.capsoula.R
import com.freelance.capsoula.data.source.local.UserDataSource
import com.freelance.capsoula.ui.addAddress.AddAddressActivity
import com.freelance.capsoula.ui.completeProfile.CompleteProfileActivity
import com.freelance.capsoula.ui.home.HomeActivity
import com.freelance.capsoula.utils.Utils
import com.freelance.capsoula.ui.deliveryMan.deliveryHome.DeliveryHomeActivity
import com.freelance.capsoula.ui.userTypes.UserTypesActivity


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Utils.hideIntercom()
        openDesiredScreen()
    }

    private fun openDesiredScreen() {

        Handler().postDelayed({
            if (UserDataSource.getUser() == null && UserDataSource.getDeliveryUser() == null)
                openUserTypes()
            else {
                when {
                    UserDataSource.getDeliveryUser() != null -> openDeliveryHome()
                    UserDataSource.getUser()!!.phone.isNullOrEmpty() -> openCompleteProfile()
                    UserDataSource.getUser()!!.userAddresses.isNullOrEmpty() -> openAddAddress()
                    else -> openHome()
                }
            }

        }, 2000)
    }

    private fun openUserTypes() {
        startActivity(Intent(this, UserTypesActivity::class.java))
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

    private fun openDeliveryHome() {
        startActivity(Intent(this, DeliveryHomeActivity::class.java))
        finish()
    }
}
