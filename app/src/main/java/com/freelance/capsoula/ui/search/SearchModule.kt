package com.freelance.capsoula.ui.search

import com.freelance.capsoula.R
import com.freelance.capsoula.data.FilterType
import com.freelance.capsoula.data.repository.SearchRepository
import com.freelance.capsoula.utils.Domain
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val searchModule = module {
    factory { SearchRepository() }

    single(named(FILTER_TYPE_LIST)) {
        arrayListOf(
            FilterType(1, Domain.application.getString(R.string.highest_price)),

            FilterType(2, Domain.application.getString(R.string.lowest_price))

//            FilterType(3, Domain.application.getString(R.string.nearby_location))
        )
    }
}

const val FILTER_TYPE_LIST = "filter_type_list"