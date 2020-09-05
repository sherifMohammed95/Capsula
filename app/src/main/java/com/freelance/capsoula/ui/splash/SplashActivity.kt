package com.freelance.capsoula.ui.splash

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.freelance.capsoula.App
import com.freelance.capsoula.R
import com.freelance.capsoula.data.source.local.UserDataSource
import com.freelance.capsoula.ui.addAddress.AddAddressActivity
import com.freelance.capsoula.ui.completeProfile.CompleteProfileActivity
import com.freelance.capsoula.ui.home.HomeActivity
import com.freelance.capsoula.utils.Utils
import com.freelance.capsoula.ui.deliveryMan.deliveryHome.DeliveryHomeActivity
import com.freelance.capsoula.ui.userTypes.UserTypesActivity
import com.freelance.capsoula.utils.Constants
import com.freelance.capsoula.utils.Domain
import com.freelance.capsoula.utils.preferencesGateway
import com.ninenox.kotlinlocalemanager.ApplicationLocale
import java.util.*


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (preferencesGateway.load( Constants.LANGUAGE, "")
                .contentEquals("")
        ) {
            preferencesGateway.save(Constants.LANGUAGE, "en")
            updateConfig("en")
        } else {
            updateConfig(preferencesGateway.load(Constants.LANGUAGE, "en"))
        }

        App.instance.stopDI()
        App.instance.startDI()

        ApplicationLocale.localeManager!!.setNewLocale(this,
            preferencesGateway.load(Constants.LANGUAGE, "en").toUpperCase())

        Utils.hideIntercom()
        openDesiredScreen()
    }

    private fun updateConfig(newLang: String) {
        val loc = Locale(newLang)
        Locale.setDefault(loc)
        val cfg = Configuration()
        cfg.locale = loc
        Domain.application.resources.updateConfiguration(cfg, null)
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
