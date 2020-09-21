package com.blueMarketing.capsula.ui.terms

import android.os.Bundle
import androidx.lifecycle.Observer
import com.blueMarketing.base.BaseActivity
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.databinding.ActivityTermsBinding
import kotlinx.android.synthetic.main.activity_terms.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class TermsActivity : BaseActivity<ActivityTermsBinding, TermsViewModel>(), TermsNavigator {

    private val mViewModel: TermsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        mViewModel.termsResponse.observe(this, Observer {
            if (!it.isNullOrEmpty())
                terms_textView.text = it
        })
    }

    override fun getMyViewModel(): TermsViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_terms
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