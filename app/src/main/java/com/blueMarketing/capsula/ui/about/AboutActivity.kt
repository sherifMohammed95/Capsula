package com.blueMarketing.capsula.ui.about

import android.os.Bundle
import androidx.lifecycle.Observer
import com.blueMarketing.base.BaseActivity
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.databinding.ActivityAboutBinding
import kotlinx.android.synthetic.main.activity_about.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AboutActivity : BaseActivity<ActivityAboutBinding, AboutViewModel>(), AboutNavigator {

    private val mViewModel: AboutViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        mViewModel.aboutResponse.observe(this, Observer {
            if (!it.isNullOrEmpty())
                about_textView.text = it
        })
    }

    override fun getMyViewModel(): AboutViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_about
    }

    override fun init() {
        viewModel = mViewModel
        viewDataBinding?.vm = mViewModel
        viewDataBinding?.navigator = this
        mViewModel.navigator = this
    }

    override fun backAction() {
        finish()
    }
}