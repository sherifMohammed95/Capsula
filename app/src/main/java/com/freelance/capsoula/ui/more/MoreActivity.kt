package com.freelance.capsoula.ui.more

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.freelance.base.BaseActivity
import com.freelance.capsoula.R
import com.freelance.capsoula.databinding.ActivityMoreBinding
import com.freelance.capsoula.ui.home.HomeViewModel
import com.freelance.capsoula.utils.AnimationUtils
import io.reactivex.functions.Action
import kotlinx.android.synthetic.main.activity_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MoreActivity : BaseActivity<ActivityMoreBinding, MoreViewModel>(), MoreNavigator {

    private val mViewModel: MoreViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Handler().postDelayed({
            AnimationUtils
                .circularTransition(viewDataBinding?.parent!!)
        }, 50)
    }

    override fun onBackPressed() {
        Handler().postDelayed({
            AnimationUtils
                .circularReverseTransition(viewDataBinding?.parent!!, Action {
                    super.onBackPressed()
                })
        }, 50)
    }


    override fun getMyViewModel(): MoreViewModel {
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_more
    }

    override fun init() {
        viewModel = mViewModel
        viewDataBinding?.vm = mViewModel
        mViewModel.navigator = this
    }
}
