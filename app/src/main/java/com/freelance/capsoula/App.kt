package com.freelance.capsoula

import android.app.Application
import com.freelance.capsoula.di.viewModelModule
import com.freelance.capsoula.di.networkModule
import com.freelance.capsoula.ui.addAddress.addAddressModule
import com.freelance.capsoula.ui.authentication.authModule
import com.freelance.capsoula.ui.brands.brandsModule
import com.freelance.capsoula.ui.categories.categoriesModule
import com.freelance.capsoula.ui.checkout.fragment.cart.cartModule
import com.freelance.capsoula.ui.checkout.fragment.details.checkoutDetailsModule
import com.freelance.capsoula.ui.deliveryMan.deliveryHome.deliveryHomeModule
import com.freelance.capsoula.ui.deliveryMan.viewProfile.viewDeliveryProfileModule
import com.freelance.capsoula.ui.deliveryMan.wallet.walletModule
import com.freelance.capsoula.ui.home.homeModule
import com.freelance.capsoula.ui.more.moreModule
import com.freelance.capsoula.ui.myOrders.ordersModule
import com.freelance.capsoula.ui.orderDetails.orderDetailsModule
import com.freelance.capsoula.ui.orderTracking.orderTrackingModule
import com.freelance.capsoula.ui.products.productsModule
import com.freelance.capsoula.ui.search.searchModule
import com.freelance.capsoula.ui.stores.storesModule
import com.freelance.capsoula.ui.subCategories.subCategoriesModule
import com.freelance.capsoula.utils.Constants
import com.freelance.capsoula.utils.Domain
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
                    walletModule,viewDeliveryProfileModule
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
        lateinit var instance: App
    }
}