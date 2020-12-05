package com.blueMarketing.capsula

import android.content.ContentResolver
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import com.blueMarketing.capsula.di.viewModelModule
import com.blueMarketing.capsula.di.networkModule
import com.blueMarketing.capsula.ui.addAddress.addAddressModule
import com.blueMarketing.capsula.ui.authentication.authModule
import com.blueMarketing.capsula.ui.brands.brandsModule
import com.blueMarketing.capsula.ui.categories.categoriesModule
import com.blueMarketing.capsula.ui.checkout.fragment.cart.cartModule
import com.blueMarketing.capsula.ui.checkout.fragment.details.checkoutDetailsModule
import com.blueMarketing.capsula.ui.deliveryMan.deliveryHome.deliveryHomeModule
import com.blueMarketing.capsula.ui.deliveryMan.viewProfile.viewDeliveryProfileModule
import com.blueMarketing.capsula.ui.deliveryMan.wallet.walletModule
import com.blueMarketing.capsula.ui.faqs.FAQsModule
import com.blueMarketing.capsula.ui.home.homeModule
import com.blueMarketing.capsula.ui.more.moreModule
import com.blueMarketing.capsula.ui.myOrders.ordersModule
import com.blueMarketing.capsula.ui.notifications.notificationsModule
import com.blueMarketing.capsula.ui.orderDetails.orderDetailsModule
import com.blueMarketing.capsula.ui.orderTracking.orderTrackingModule
import com.blueMarketing.capsula.ui.products.productsModule
import com.blueMarketing.capsula.ui.search.searchModule
import com.blueMarketing.capsula.ui.stores.storesModule
import com.blueMarketing.capsula.ui.subCategories.subCategoriesModule
import com.blueMarketing.capsula.utils.Constants
import com.blueMarketing.capsula.utils.Domain
import com.ninenox.kotlinlocalemanager.ApplicationLocale
import io.intercom.android.sdk.Intercom
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import timber.log.Timber

class App : ApplicationLocale() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Domain.integrateWith(this)
        startDI()
        instance = this
        initIntercom()
        initRingTone()
    }

    private fun initRingTone() {
        alarmSound = Uri.parse(
            ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                    this.packageName + "/raw/ringtone"
        )
        ringtone = RingtoneManager.getRingtone(this, alarmSound)
    }

     fun startDI() {
        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    networkModule, viewModelModule, authModule, addAddressModule, homeModule,
                    brandsModule, categoriesModule, storesModule, subCategoriesModule,
                    productsModule, searchModule, cartModule, checkoutDetailsModule, moreModule,
                    ordersModule, orderDetailsModule, orderTrackingModule, deliveryHomeModule,
                    walletModule,viewDeliveryProfileModule, FAQsModule, notificationsModule
                )
            )
        }
    }

    fun stopDI() {
        stopKoin()
    }

    private fun initIntercom() {
        Intercom.initialize(
            this, Constants.INTERCOM_API_KEY,
            Constants.INTERCOM_APP_ID
        )
    }

    companion object {
        lateinit var alarmSound: Uri
        lateinit var ringtone: Ringtone
        lateinit var instance: App
    }
}