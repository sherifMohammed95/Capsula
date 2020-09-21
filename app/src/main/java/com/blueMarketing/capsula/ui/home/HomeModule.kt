package com.blueMarketing.capsula.ui.home

import com.blueMarketing.capsula.data.repository.GeneralRepository
import com.blueMarketing.capsula.ui.home.adapters.HomeBrandsAdapter
import com.blueMarketing.capsula.ui.home.adapters.HomeCategoriesAdapter
import com.blueMarketing.capsula.ui.home.adapters.HomeStoresAdapter
import org.koin.dsl.module

val homeModule = module {

    factory { (mContext:HomeActivity)->HomeStoresAdapter(mContext) }
    factory { (mContext:HomeActivity)->HomeCategoriesAdapter(mContext) }
    factory { (mContext:HomeActivity)->HomeBrandsAdapter(mContext) }
    factory { GeneralRepository() }
}