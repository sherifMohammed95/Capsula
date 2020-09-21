package com.blueMarketing.capsula.ui.deliveryMan.viewProfile.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.blueMarketing.capsula.ui.deliveryMan.viewProfile.fragments.ViewCarDetailsFragment
import com.blueMarketing.capsula.ui.deliveryMan.viewProfile.fragments.ViewPersonalDetailsFragment
import com.blueMarketing.capsula.ui.deliveryMan.viewProfile.fragments.ViewRequiredDocumentsFragment

class ViewPagerAdapter(fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {


    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        return when(position){
            0-> ViewPersonalDetailsFragment()
            1-> ViewCarDetailsFragment()
            else-> ViewRequiredDocumentsFragment()
        }
    }

}