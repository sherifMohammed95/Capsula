package com.freelance.base

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.freelance.capsoula.R
import com.freelance.capsoula.databinding.ActivityBrandsBinding
import com.freelance.capsoula.databinding.ActivityCategoriesBinding
import com.freelance.capsoula.ui.search.SearchActivity
import com.freelance.capsoula.utils.MyContextWrapper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_brands.view.*
import kotlinx.android.synthetic.main.loading_layout.*
import kotlinx.android.synthetic.main.search_toolbar_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.search_toolbar_layout
import kotlinx.android.synthetic.main.toolbar_layout.title_toolbar_textView
import rx.functions.Action1

abstract class BaseActivity<T : ViewDataBinding, V : BaseViewModel<*>> :
    AppCompatActivity(), BaseFragment.Callback {

    var viewDataBinding: T? = null
        private set
    protected var viewModel: V? = null
    private var dialog: AlertDialog? = null
    private var loadingLayout: View? = null
    private var toolbarLayout: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performDataBinding()
        init()
        viewDataBinding?.executePendingBindings()
        handleError()
        handleProgressLoading()
    }

    private fun handleProgressLoading() {
        viewModel!!.progressLoading.observe(this, Observer { showLoading ->
            if (viewModel != null) {
                if (showLoading)
                    window.setFlags(
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    )
                else
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                viewModel!!.isLoading.set(showLoading)
            }
        })

        viewModel!!.showLoadingLayout.observe(this, Observer { showLoading ->
            if (showLoading)
                showProgressLayout()
            else
                hideProgressLayout()
        })
    }

    private fun handleError() {
        viewModel!!.errorMessage.observe(this, Observer {
            showPopUp("", it, getString(android.R.string.ok), false)
        })
        viewModel!!.networkErrorMessage.observe(this, Observer {
            showPopUp("", it, R.string.try_again,
                DialogInterface.OnClickListener { _, _ ->
                    if (viewModel!!.getRepoAction() != null) {
                        viewModel!!.getRepoAction()!!.run()
                    }
                }
                , getString(android.R.string.cancel), false)
        })
    }

    fun setUpSearchToolbar(searchTextCallback: Action1<String>) {

        back_search_toolbar_imageView?.setOnClickListener {
            finish()
        }

        search_toolbar_editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.toString().isEmpty())
                    searchTextCallback.call("")
            }

            override fun afterTextChanged(editable: Editable) {

            }
        })


        search_toolbar_editText?.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                actionId == EditorInfo.IME_ACTION_DONE
            ) {
                if (v.text.toString().trim().isNotEmpty()) {
                    hideKeyboard()
                    searchTextCallback.call(v.text.toString())
                }
            }
            true
        }
    }

    fun setUpToolbar(toolbarTitle: String) {

        title_toolbar_textView?.text = toolbarTitle

        back_toolbar_imageView?.setOnClickListener {
            finish()
        }

        search_toolbar_layout.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }
    }

    private fun showProgressLayout() {
        progress_bar_layout?.visibility = View.VISIBLE
    }

    private fun hideProgressLayout() {
        if (loadingLayout == null)
            loadingLayout = findViewById(R.id.progress_bar_layout)

        if (loadingLayout == null) return

        loadingLayout?.visibility = View.GONE
    }


    override fun onFragmentAttached() {

    }

    override fun onFragmentDetached(tag: String) {

    }

    protected abstract fun getMyViewModel(): V

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    protected abstract fun init(): Unit


    @TargetApi(Build.VERSION_CODES.M)
    fun hasPermission(permission: String): Boolean {
        return (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || checkSelfPermission(permission)
                == PackageManager.PERMISSION_GRANTED)
    }

    private fun performDataBinding() {
        viewDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
        this.viewModel = if (viewModel == null) getMyViewModel() else viewModel
        viewDataBinding!!.executePendingBindings()
    }


    fun showPopUp(
        @StringRes messageId: Int,
        @StringRes posActionName: Int,
        positiveAction: DialogInterface.OnClickListener,
        @StringRes negAction: Int,
        isCancelable: Boolean
    ): AlertDialog {
        val builder = MaterialAlertDialogBuilder(this,R.style.MaterialAlertDialog_Rounded)
        return builder.setCancelable(isCancelable)
            .setMessage(getString(messageId))
            .setPositiveButton(getString(posActionName), positiveAction)
            .setNegativeButton(negAction) { dialog, which -> dialog.dismiss() }.show()
    }


    fun showPopUp(
        message: String,
        @StringRes posActionName: Int,
        positiveAction: DialogInterface.OnClickListener,
        @StringRes negAction: Int,
        isCancelable: Boolean
    ): AlertDialog {
        val builder = MaterialAlertDialogBuilder(this,R.style.MaterialAlertDialog_Rounded)
        return builder.setCancelable(isCancelable)
            .setMessage(message)
            .setPositiveButton(getString(posActionName), positiveAction)
            .setNegativeButton(negAction) { dialog, which -> dialog.dismiss() }.show()
    }

    fun showPopUp(
        @StringRes messageId: Int,
        @StringRes posAction: Int,
        positiveAction: DialogInterface.OnClickListener,
        @StringRes negActioname: Int,
        negAction: DialogInterface.OnClickListener,
        isCancelable: Boolean
    ): AlertDialog {
        val builder = MaterialAlertDialogBuilder(this,R.style.MaterialAlertDialog_Rounded)
        return builder.setCancelable(isCancelable)
            .setMessage(getString(messageId))
            .setPositiveButton(getString(posAction), positiveAction)
            .setNegativeButton(getString(negActioname), negAction).show()
    }

    fun showPopUp(
        @StringRes messageId: Int,
        posActionName: String,
        positiveAction: DialogInterface.OnClickListener,
        negActionName: String,
        negAction: DialogInterface.OnClickListener,
        isCancelable: Boolean
    ): AlertDialog {
        val builder =
            MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_Rounded)
        return builder.setCancelable(isCancelable)
            .setMessage(getString(messageId))
            .setPositiveButton(posActionName, positiveAction)
            .setNegativeButton(negActionName, negAction).show()
    }

    fun showPopUp(
        @StringRes messageId: Int,
        @StringRes posAction: Int,
        positiveAction: DialogInterface.OnClickListener,
        isCancelable: Boolean
    ): AlertDialog {
        val builder = MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_Rounded)
        return builder.setCancelable(isCancelable)
            .setMessage(getString(messageId))
            .setPositiveButton(getString(posAction), positiveAction)
            .show()
    }

    fun showPopUp(
        @StringRes messageId: Int,
        @StringRes posAction: Int,
        isCancelable: Boolean
    ): AlertDialog {
        val builder = MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_Rounded)
        return builder.setCancelable(isCancelable)
            .setMessage(getString(messageId))
            .setPositiveButton(getString(posAction), null)
            .show()
    }

    fun showPopUp(
        title: String, message: String,
        posAction: Int,
        positiveAction: DialogInterface.OnClickListener,
        negAction: String,
        isCancelable: Boolean
    ): AlertDialog {
        val builder = MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_Rounded)
        return builder.setCancelable(isCancelable)
            .setMessage(message)
            .setPositiveButton(getString(posAction), positiveAction)
            .setNegativeButton(negAction) { dialog, which -> dialog.dismiss() }.show()
    }

    fun showPopUp(
        title: String,
        message: String,
        posAction: String,
        isCancelable: Boolean
    ): AlertDialog {
        val builder = MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_Rounded)
        return builder.setCancelable(isCancelable)
            .setMessage(message)
            .setPositiveButton(posAction) { dialog, which -> dialog.dismiss() }
            .show()
    }

    fun showPopUp(
        title: String,
        message: String,
        posAction: String,
        positiveAction: DialogInterface.OnClickListener,
        isCancelable: Boolean
    ): AlertDialog {
        val builder = MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_Rounded)
        return builder.setCancelable(isCancelable)
            .setMessage(message)
            .setPositiveButton(posAction, positiveAction)
            .show()
    }

    fun showPopUp(
        message: String,
        @StringRes posAction: Int,
        positiveAction: DialogInterface.OnClickListener,
        isCancelable: Boolean
    ): AlertDialog {
        val builder = MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_Rounded)
        return builder.setCancelable(isCancelable)
            .setMessage(message)
            .setPositiveButton(getString(posAction), positiveAction)
            .show()
    }

    fun showPopUp(
        message: String,
        @StringRes posAction: Int,
        isCancelable: Boolean
    ): AlertDialog {
        val builder = MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_Rounded)
        return builder.setCancelable(isCancelable)
            .setMessage(message)
            .setPositiveButton(getString(posAction), null)
            .show()
    }

    fun showChoicesDialog(
        list: Array<String>, isCancelable: Boolean,
        action1: Action1<Int>
    ) {

        MaterialAlertDialogBuilder(this)
            .setCancelable(isCancelable)
            .setItems(
                list
            ) { dialog, which ->
                when (which) {
                    0 -> action1.call(0)
                    1 -> action1.call(1)
                }
            }
            .show()
    }

    override fun attachBaseContext(newBase: Context) {
        //super.attachBaseContext(newBase)
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
            val newLocale = "en"
            val context: Context = MyContextWrapper.wrap(newBase, newLocale)
            super.attachBaseContext(context)
        } else {
            super.attachBaseContext(newBase)
        }
    }

    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    fun hideKeypadWhenClickingOut(view: View) {

        // Set up touch listener for non-text box views endDate hide keyboard.
        if (view !is EditText) {
            view.setOnTouchListener { v, event ->
                hideSoftKeyboard(this@BaseActivity)
                false
            }
        }

        //If a layout container, iterate over children and seed recursion.
        if (view is ViewGroup) {
            for (i in 0 until (view as ViewGroup).childCount) {
                val innerView = (view as ViewGroup).getChildAt(i)
                hideKeypadWhenClickingOut(innerView)
            }
        }
    }

    companion object {
        fun hideSoftKeyboard(activity: Activity) {
            val inputMethodManager = activity.getSystemService(
                Activity.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            if ((activity.currentFocus != null && activity.currentFocus!!.windowToken != null)) {
                inputMethodManager.hideSoftInputFromWindow(
                    activity.currentFocus!!.windowToken, 0
                )
            }

        }
    }

}