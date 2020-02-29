package com.freelance.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment

abstract class BaseDialogFragment<T : ViewDataBinding, V : BaseViewModel<*>> : DialogFragment() {

    var baseActivity: BaseActivity<*, *>? = null
        private set
    private var mRootView: View? = null
    var viewDataBinding: T? = null
        private set
    private var mViewModel: V? = null

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

    protected abstract fun subscibeToLiveData() : Unit



//    protected abstract fun getBindingVariable(): Int

//    override fun onAttach(context: Context?) {
//        super.onAttach(context)
//        if (context is BaseActivity<*, *>) {
//            val activity = context as BaseActivity<*, *>?
//            this.baseActivity = activity
//            activity!!.onFragmentAttached()
//        }
//    }
//
//    override fun onDetach() {
//        baseActivity = null
//        super.onDetach()
//    }

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
        mRootView = viewDataBinding!!.getRoot()
        return mRootView
        //        return super.onCreateView(inflater, container, savedInstanceState);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewDataBinding!!.setVariable(getBindingVariable(), mViewModel)
        viewDataBinding!!.executePendingBindings()
        init()
        subscibeToLiveData()
    }

    fun hideKeyboard() {
        if (baseActivity != null) {
            baseActivity!!.hideKeyboard()
        }
    }

}
