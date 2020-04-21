package com.freelance.capsoula.ui.checkout.fragment.details

import com.freelance.capsoula.R
import com.freelance.capsoula.data.ImagePickerOption
import com.freelance.capsoula.data.PaymentMethod
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val checkoutDetailsModule = module {

    single(named(IMAGE_PICKER_OPTIONS_LIST)) {
        arrayListOf(
            ImagePickerOption(1, androidContext().getString(R.string.gallery)),
            ImagePickerOption(2, androidContext().getString(R.string.camera))
        )
    }

    single(named(PAYMENT_METHOD_LIST)) {
        arrayListOf(
            PaymentMethod(1,R.drawable.ic_cash,androidContext()
                .getString(R.string.cash_on_delivery)),
            PaymentMethod(2,R.drawable.ic_credit,androidContext()
                .getString(R.string.credit_card)),
            PaymentMethod(3,R.drawable.ic_google_pay,androidContext()
                .getString(R.string.google_pay)),
            PaymentMethod(4,R.drawable.ic_stc_pay,androidContext()
                .getString(R.string.stc_pay)),
            PaymentMethod(3,R.drawable.ic_mada,androidContext()
                .getString(R.string.mada))

        )
    }
}

const val IMAGE_PICKER_OPTIONS_LIST = "imagePickerOptionList"
const val PAYMENT_METHOD_LIST = "paymentMethodList"