package com.freelance.base

import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.freelance.capsoula.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetFragment<T : ViewDataBinding, V : BaseViewModel<*>> :
    BottomSheetDialogFragment() {

    var baseActivity: BaseActivity<*, *>? = null
        private set
    private var mRootView: View? = null
    var viewDataBinding: T? = null
        private set
    private var mViewModel: V? = null
    private var progressDialog: ProgressDialog? = null

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
//    abstract val bindingVariable: Int

    /**
     * @return layout resource id
     */
//    @get:LayoutRes
//    abstract val layoutId: Int

    /**
     * Override for set view model
     *
     * @return view model instance
     */
//    abstract val viewModel: V


    protected abstract fun getViewModel(): V

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    protected abstract fun init(): Unit

    protected abstract fun subscibeToLiveData(): Unit


//    protected abstract fun getBindingVariable(): Int

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*, *>) {
            this.baseActivity = context
            context.onFragmentAttached()
        }
    }

    override fun onDetach() {
        baseActivity = null
        super.onDetach()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = getViewModel()
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
        viewDataBinding!!.executePendingBindings()
        init()
        subscibeToLiveData()
        handleError()
    }


    private fun handleError() {
        mViewModel!!.errorMessage.observe(this, Observer {
            showPopUp("", it, getString(android.R.string.ok), false)
        })
        mViewModel!!.networkErrorMessage.observe(this, Observer {
            showPopUp("", it, R.string.try_again,
                DialogInterface.OnClickListener { _, _ ->
                    if (mViewModel!!.getRepoAction() != null) {
                        mViewModel!!.getRepoAction()!!.run()
                    }

                }
                , getString(android.R.string.cancel), false)
        })
    }

    fun hideKeyboard() {
        if (baseActivity != null) {
            baseActivity!!.hideKeyboard()
        }
    }

    protected fun showProgressDialog(staringId: Int) {
        progressDialog = ProgressDialog(getActivity())
        progressDialog!!.setCancelable(true)
        progressDialog!!.setMessage(getString(staringId))
        progressDialog!!.show()
    }

    protected fun hideProgressDialog() {
        progressDialog!!.dismiss()
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

}