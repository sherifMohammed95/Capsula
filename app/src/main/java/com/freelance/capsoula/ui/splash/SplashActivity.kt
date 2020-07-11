package com.freelance.capsoula.ui.splash

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.freelance.capsoula.R
import com.freelance.capsoula.data.source.local.UserDataSource
import com.freelance.capsoula.ui.addAddress.AddAddressActivity
import com.freelance.capsoula.ui.authentication.AuthenticationActivity
import com.freelance.capsoula.ui.completeProfile.CompleteProfileActivity
import com.freelance.capsoula.ui.home.HomeActivity
import com.freelance.capsoula.utils.Utils
import com.paytabs.paytabs_sdk.payment.ui.activities.PayTabActivity
import com.paytabs.paytabs_sdk.utils.PaymentParams
import androidx.core.app.ActivityCompat.startActivityForResult
import cards.pay.paycardsrecognizer.sdk.ScanCardIntent

import cards.pay.paycardsrecognizer.sdk.Card
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
//                        testFun()
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

    private fun testFun() {

        val intent = Intent(this, PayTabActivity::class.java)
        intent.putExtra(PaymentParams.MERCHANT_EMAIL, "sherif.mohammed2016@gmail.com")
        intent.putExtra(
            PaymentParams.SECRET_KEY,
            "UD4BnpUbtqOiADaSiIXxWltFgiLhtd6nQaYdzTp3jelTSwLafJIb4cQluCCXQJlGUkUtdjxwyTqqpcBFVkQ0wHl0kYYaHjQBDK5Q"
        )//Add your Secret Key Here
        intent.putExtra(PaymentParams.LANGUAGE, PaymentParams.ARABIC)
        intent.putExtra(PaymentParams.TRANSACTION_TITLE, "Test Paytabs android library")
        intent.putExtra(PaymentParams.AMOUNT, 5.0)

        intent.putExtra(PaymentParams.CURRENCY_CODE, "BHD")
        intent.putExtra(PaymentParams.CUSTOMER_PHONE_NUMBER, "009733")
        intent.putExtra(PaymentParams.CUSTOMER_EMAIL, "customer-email@example.com")
        intent.putExtra(PaymentParams.ORDER_ID, "123456")
        intent.putExtra(PaymentParams.PRODUCT_NAME, "Product 1, Product 2")

//Billing Address
        intent.putExtra(PaymentParams.ADDRESS_BILLING, "Flat 1,Building 123, Road 2345")
        intent.putExtra(PaymentParams.CITY_BILLING, "Manama")
        intent.putExtra(PaymentParams.STATE_BILLING, "Manama")
        intent.putExtra(PaymentParams.COUNTRY_BILLING, "BHR")
        intent.putExtra(
            PaymentParams.POSTAL_CODE_BILLING,
            "00973"
        ) //Put Country Phone code if Postal code not available '00973'

//Shipping Address
        intent.putExtra(PaymentParams.ADDRESS_SHIPPING, "Flat 1,Building 123, Road 2345")
        intent.putExtra(PaymentParams.CITY_SHIPPING, "Manama")
        intent.putExtra(PaymentParams.STATE_SHIPPING, "Manama")
        intent.putExtra(PaymentParams.COUNTRY_SHIPPING, "USD")
        intent.putExtra(
            PaymentParams.POSTAL_CODE_SHIPPING,
            "00973"
        ) //Put Country Phone code if Postal code not available '00973'

//Payment Page Style
        intent.putExtra(PaymentParams.PAY_BUTTON_COLOR, "#2474bc")

//Tokenization
        intent.putExtra(PaymentParams.IS_TOKENIZATION, true)
        startActivityForResult(intent, PaymentParams.PAYMENT_REQUEST_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PaymentParams.PAYMENT_REQUEST_CODE) {
            Log.e("Tag", data!!.getStringExtra(PaymentParams.RESPONSE_CODE))
            Log.e("Tag", data.getStringExtra(PaymentParams.TRANSACTION_ID))
            if (data.hasExtra(PaymentParams.TOKEN) && data.getStringExtra(PaymentParams.TOKEN)!!
                    .isNotEmpty()
            ) {
                Log.e("Tag", data.getStringExtra(PaymentParams.TOKEN))
                Log.e("Tag", data.getStringExtra(PaymentParams.CUSTOMER_EMAIL))
                Log.e("Tag", data.getStringExtra(PaymentParams.CUSTOMER_PASSWORD))
            }
        }
    }
}
