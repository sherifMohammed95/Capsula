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
import com.freelance.capsoula.ui.home.homeModule
import com.freelance.capsoula.ui.more.moreModule
import com.freelance.capsoula.ui.myOrders.ordersModule
import com.freelance.capsoula.ui.orderDetails.orderDetailsModule
import com.freelance.capsoula.ui.orderTracking.orderTrackingModule
import com.freelance.capsoula.ui.products.productsModule
import com.freelance.capsoula.ui.search.searchModule
import com.freelance.capsoula.ui.stores.storesModule
import com.freelance.capsoula.ui.subCategories.subCategoriesModule
import com.freelance.capsoula.utils.Domain
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Domain.integrateWith(this)
        startDI()
    }

    private fun startDI() {
        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    networkModule, viewModelModule, authModule, addAddressModule, homeModule,
                    brandsModule, categoriesModule, storesModule, subCategoriesModule,
                    productsModule, searchModule, cartModule, checkoutDetailsModule, moreModule,
                    ordersModule, orderDetailsModule, orderTrackingModule
                )
            )
        }
    }
}