package com.freelance.capsoula.ui.about

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.freelance.base.BaseActivity
import com.freelance.capsoula.R
import com.freelance.capsoula.databinding.ActivityAboutBinding
import com.freelance.capsoula.ui.terms.TermsViewModel
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.activity_terms.*
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