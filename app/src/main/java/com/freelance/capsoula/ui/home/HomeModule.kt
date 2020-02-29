package com.freelance.capsoula.ui.home

import com.freelance.capsoula.data.repository.GeneralRepository
import com.freelance.capsoula.ui.home.adapters.HomeBrandsAdapter
import com.freelance.capsoula.ui.home.adapters.HomeCategoriesAdapter
import com.freelance.capsoula.ui.home.adapters.HomeStoresAdapter
import org.koin.dsl.module

val homeModule = module {

    factory { (mContext:HomeActivity)->HomeStoresAdapter(mContext) }
    factory { (mContext:HomeActivity)->HomeCategoriesAdapter(mContext) }
    factory { (mContext:HomeActivity)->HomeBrandsAdapter(mContext) }
    factory { GeneralRepository() }
}