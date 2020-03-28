package com.freelance.base

import android.app.ProgressDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.freelance.capsoula.R

abstract class BaseFragment<T : ViewDataBinding, V : BaseViewModel<*>> : Fragment() {

    var baseActivity: BaseActivity<*, *>? = null
        private set
    private var mRootView: View? = null
    var viewDataBinding: T? = null
        private set
    protected var viewModel: V? = null
    var loadingLayout: View? = null

    protected abstract fun getMyViewModel(): V

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    protected abstract fun init(): Unit


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getMyViewModel()
        setHasOptionsMenu(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = DataBindingUtil.inflate(
            inflater, getLayoutId(), container,
            false
        )
        mRootView = viewDataBinding!!.root
        return mRootView
        //        return super.onCreateView(inflater, container, savedInstanceState);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewDataBinding!!.setVariable(getBindingVariable(), mViewModel)
        init()
        viewDataBinding!!.executePendingBindings()
        handleError()
        handleProgressLoading()
    }


    private fun handleProgressLoading() {
        viewModel!!.showLoadingLayout.observe(viewLifecycleOwner, Observer { showLoading ->
            if(showLoading)
                showProgressLayout()
            else
                hideProgressLayout()
        })

        viewModel!!.progressLoading.observe(viewLifecycleOwner, Observer { showLoading ->
            if (viewModel != null) {
                if (showLoading)
                    activity?.window?.setFlags(
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    )
                else
                    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                viewModel!!.isLoading.set(showLoading)
            }
        })
    }

    private fun handleError() {
        viewModel!!.errorMessage.observe(viewLifecycleOwner, Observer {
            (activity as BaseActivity<*, *>).showPopUp(
                "",
                it,
                getString(android.R.string.ok),
                false
            )
        })
        viewModel!!.networkErrorMessage.observe(viewLifecycleOwner, Observer {
            (activity as BaseActivity<*, *>).showPopUp("", it, R.string.try_again,
                DialogInterface.OnClickListener { _, _ ->
                    if (viewModel!!.getRepoAction() != null) {
                        viewModel!!.getRepoAction()!!.run()
                    }

                }
                , getString(android.R.string.cancel), false)
        })
    }


    private fun showProgressLayout() {
        if (loadingLayout == null)
            loadingLayout = view?.findViewById(R.id.progress_bar_layout)

        loadingLayout?.visibility = View.VISIBLE
    }

    private fun hideProgressLayout() {
        if (loadingLayout == null)
            loadingLayout = view?.findViewById(R.id.progress_bar_layout)

        if (loadingLayout == null) return

        loadingLayout?.visibility = View.GONE
    }


    fun hideKeyboard() {
        if (baseActivity != null) {
            baseActivity!!.hideKeyboard()
        }
    }


    fun showPopUp(
        @StringRes messageId: Int,
        @StringRes posActionName: Int,
        positiveAction: DialogInterface.OnClickListener,
        @StringRes negAction: Int,
        isCancelable: Boolean
    ): AlertDialog {
        return baseActivity!!.showPopUp(
            messageId,
            posActionName,
            positiveAction,
            negAction,
            isCancelable
        )
    }

    fun showPopUp(
        @StringRes messageId: Int,
        @StringRes posAction: Int,
        positiveAction: DialogInterface.OnClickListener,
        @StringRes negActioname: Int,
        negAction: DialogInterface.OnClickListener,
        isCancelable: Boolean
    ): AlertDialog {
        return showPopUp(
            messageId,
            posAction,
            positiveAction,
            negActioname,
            negAction,
            isCancelable
        )
    }

    fun showPopUp(
        @StringRes messageId: Int,
        posActionName: String,
        positiveAction: DialogInterface.OnClickListener,
        negActionName: String,
        negAction: DialogInterface.OnClickListener,
        isCancelable: Boolean
    ): AlertDialog {
        return baseActivity!!.showPopUp(
            messageId,
            posActionName,
            positiveAction,
            negActionName,
            negAction,
            isCancelable
        )
    }

    fun showPopUp(
        @StringRes messageId: Int,
        @StringRes posAction: Int,
        positiveAction: DialogInterface.OnClickListener,
        isCancelable: Boolean
    ): AlertDialog {
        return baseActivity!!.showPopUp(messageId, posAction, positiveAction, isCancelable)
    }


    fun showPopUp(
        message: String,
        @StringRes posActionName: Int,
        positiveAction: DialogInterface.OnClickListener,
        @StringRes negAction: Int,
        isCancelable: Boolean
    ): AlertDialog {
        return baseActivity!!.showPopUp(
            message,
            posActionName,
            positiveAction,
            negAction,
            isCancelable
        )
    }

    fun showPopUp(
        @StringRes messageId: Int,
        @StringRes posAction: Int,
        isCancelable: Boolean
    ): AlertDialog {
        return baseActivity!!.showPopUp(messageId, posAction, isCancelable)
    }

    fun showPopUp(
        title: String, message: String,
        posAction: Int,
        positiveAction: DialogInterface.OnClickListener,
        negAction: String,
        isCancelable: Boolean
    ): AlertDialog {
        return baseActivity!!.showPopUp(
            title,
            message,
            posAction,
            positiveAction,
            negAction,
            isCancelable
        )
    }

    fun showPopUp(
        title: String,
        message: String,
        posAction: String,
        isCancelable: Boolean
    ): AlertDialog {
        return baseActivity!!.showPopUp(title, message, posAction, isCancelable)
    }

    fun showPopUp(
        message: String,
        @StringRes posAction: Int,
        positiveAction: DialogInterface.OnClickListener,
        isCancelable: Boolean
    ): AlertDialog {
        return baseActivity!!.showPopUp(message, posAction, positiveAction, isCancelable)
    }

    fun showPopUp(
        message: String,
        @StringRes posAction: Int,
        isCancelable: Boolean
    ): AlertDialog {
        return baseActivity!!.showPopUp(message, posAction, isCancelable)
    }


    interface Callback {

        fun onFragmentAttached()

        fun onFragmentDetached(tag: String)

    }

}
