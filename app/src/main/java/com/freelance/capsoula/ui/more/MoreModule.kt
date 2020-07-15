package com.freelance.capsoula.ui.more

import com.freelance.capsoula.R
import com.freelance.capsoula.data.MoreItem
import com.freelance.capsoula.ui.more.adapters.MoreAdapter
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val moreModule = module {

    factory { MoreAdapter() }

    single(named(GUEST_MORE_LIST)) {
        arrayListOf(
            MoreItem(R.drawable.ic_help, androidContext().getString(R.string.help)),
            MoreItem(R.drawable.ic_earn_share, androidContext().getString(R.string.share_earn)),
            MoreItem(
                R.drawable.ic_enable_notifications,
                androidContext().getString(R.string.enable_notifications)
            ),
            MoreItem(R.drawable.ic_faqs, androidContext().getString(R.string.faqs)),
            MoreItem(R.drawable.ic_logout, androidContext().getString(R.string.login))
        )
    }

    single(named(LOGGED_MORE_LIST)) {
        arrayListOf(
            MoreItem(R.drawable.ic_personal, androidContext().getString(R.string.personal_details)),
            MoreItem(R.drawable.ic_my_orders, androidContext().getString(R.string.my_orders)),
            MoreItem(
                R.drawable.ic_manage_payment,
                androidContext().getString(R.string.manage_payment_methods)
            ),
            MoreItem(R.drawable.ic_help, androidContext().getString(R.string.help)),
            MoreItem(R.drawable.ic_earn_share, androidContext().getString(R.string.share_earn)),
            MoreItem(
                R.drawable.ic_enable_notifications,
                androidContext().getString(R.string.enable_notifications)
            ),
            MoreItem(R.drawable.ic_faqs, androidContext().getString(R.string.faqs)),
            MoreItem(R.drawable.ic_logout, androidContext().getString(R.string.logout))
        )
    }

    single(named(DELIVERY_MORE_LIST)) {
        arrayListOf(
            MoreItem(R.drawable.ic_personal, androidContext().getString(R.string.personal_details)),
            MoreItem(R.drawable.ic_history, androidContext().getString(R.string.history)),
            MoreItem(
                R.drawable.ic_wallet,
                androidContext().getString(R.string.my_wallet)
            ),
            MoreItem(R.drawable.ic_language, androidContext().getString(R.string.arabic)),

            MoreItem(R.drawable.ic_logout, androidContext().getString(R.string.logout))
        )
    }
}

const val GUEST_MORE_LIST = "guest_more_list"
const val LOGGED_MORE_LIST = "logged_more_list"
const val DELIVERY_MORE_LIST = "delivery_more_list"