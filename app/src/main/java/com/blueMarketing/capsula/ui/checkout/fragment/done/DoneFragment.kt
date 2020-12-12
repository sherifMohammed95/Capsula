package com.blueMarketing.capsula.ui.checkout.fragment.done

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.blueMarketing.base.BaseFragment
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.data.source.local.UserDataSource
import com.blueMarketing.capsula.databinding.FragmentDoneBinding
import com.blueMarketing.capsula.ui.checkout.CustomerCheckoutActivity
import com.blueMarketing.capsula.ui.myOrders.MyOrdersActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class DoneFragment : BaseFragment<FragmentDoneBinding, DoneViewModel>(), DoneNavigator {

    private val mViewModel: DoneViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = UserDataSource.getUser()
        user?.cartContent = ArrayList()
        UserDataSource.saveUser(user)
    }

    override fun getMyViewModel(): DoneViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_done
    }

    override fun init() {
        viewDataBinding?.vm = mViewModel
        viewDataBinding?.navigator = this
        viewModel = mViewModel
        mViewModel.navigator = this
    }

    override fun onResume() {
        super.onResume()
        (activity as CustomerCheckoutActivity).viewDataBinding?.toolbar?.progressBarImageView
            ?.setImageResource(R.drawable.done_progress_bar)
    }

    override fun openMyOrders() {
        startActivity(Intent(activity, MyOrdersActivity::class.java))
        activity?.finish()
    }
}