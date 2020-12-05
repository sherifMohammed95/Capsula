package com.blueMarketing.base

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
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
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.custom.PaginationScrollListener
import com.blueMarketing.capsula.data.MessageEvent
import com.blueMarketing.capsula.data.source.local.UserDataSource
import com.blueMarketing.capsula.ui.addAddress.AddAddressActivity
import com.blueMarketing.capsula.ui.authentication.AuthenticationActivity
import com.blueMarketing.capsula.ui.brands.BrandsActivity
import com.blueMarketing.capsula.ui.categories.CategoriesActivity
import com.blueMarketing.capsula.ui.checkout.CheckoutActivity
import com.blueMarketing.capsula.ui.completeProfile.CompleteProfileActivity
import com.blueMarketing.capsula.ui.deliveryMan.deliveryAuthentication.DeliveryAuthenticationActivity
import com.blueMarketing.capsula.ui.deliveryMan.deliveryHome.DeliveryHomeActivity
import com.blueMarketing.capsula.ui.deliveryMan.deliveryOrderDetails.DeliveryOrderDetailsActivity
import com.blueMarketing.capsula.ui.deliveryMan.history.HistoryActivity
import com.blueMarketing.capsula.ui.more.MoreActivity
import com.blueMarketing.capsula.ui.myOrders.MyOrdersActivity
import com.blueMarketing.capsula.ui.products.ProductsActivity
import com.blueMarketing.capsula.ui.search.SearchActivity
import com.blueMarketing.capsula.ui.stores.StoresActivity
import com.blueMarketing.capsula.ui.subCategories.SubCategoriesActivity
import com.blueMarketing.capsula.utils.Constants.OPEN_CHECKOUT
import com.blueMarketing.capsula.utils.Constants.UPDATE_CART_NUMBER
import com.blueMarketing.capsula.utils.Domain
import com.blueMarketing.capsula.utils.Utils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ninenox.kotlinlocalemanager.AppCompatActivityBase
import io.reactivex.functions.Action
import kotlinx.android.synthetic.main.loading_layout.*
import kotlinx.android.synthetic.main.search_toolbar_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.search_toolbar_layout
import kotlinx.android.synthetic.main.toolbar_layout.title_toolbar_textView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import rx.functions.Action1
import java.util.*

abstract class BaseActivity<T : ViewDataBinding, V : BaseViewModel<*>> :
    AppCompatActivityBase(), BaseFragment.Callback {

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

    fun updateConfig(newLang: String) {
        val loc = Locale(newLang)
        Locale.setDefault(loc)
        val cfg = Configuration()
        cfg.locale = loc
        Domain.application.resources.updateConfiguration(cfg, null)
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

        viewModel!!.isPagingLoadingEvent.observe(this, Observer { showLoading ->
            if (viewModel != null) {
                viewModel?.isPagingLoading?.set(showLoading)!!
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

    fun swipeToRefresh(layout: SwipeRefreshLayout?, action: Action?) {
        layout?.setOnRefreshListener { action?.run() }
    }

    fun paginate(recyclerView: RecyclerView?, layoutManager: LinearLayoutManager, action: Action?) {
        recyclerView?.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun isLastPage(): Boolean {
                return viewModel?.isLastPage!!
            }

            override fun isLoading(): Boolean {
                return viewModel?.isPagingLoading?.get()!!
            }

            override fun loadMoreItems() {
                viewModel?.isPagingLoading?.set(true)
                action?.run()
            }
        })
    }

    fun paginateWithScrollView(scrollView: NestedScrollView?, action: Action?) {
        scrollView?.setOnScrollChangeListener { v, scrollX: Int, scrollY: Int,
                                                oldScrollX: Int, oldScrollY: Int ->


            val lastChild = scrollView.getChildAt(scrollView.childCount - 1)

            if (lastChild != null) {
                if ((scrollY >=
                            (lastChild.measuredHeight - v.measuredHeight))
                    && scrollY > oldScrollY && !viewModel?.isPagingLoading?.get()!!
                    && !viewModel?.isLastPage!!
                ) {
                    viewModel?.isPagingLoadingEvent?.value = true
                    action?.run()
                }
            }
        }
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

        cart_imageView_layout_toolbar?.setOnClickListener {
            if (UserDataSource.getUser() == null) {
                if (UserDataSource.getUserCartSize() > 0)
                    startActivity(Intent(this, CheckoutActivity::class.java))
                else
                    showPopUp(
                        getString(R.string.cart), getString(R.string.empty_cart_msg),
                        getString(android.R.string.ok), false
                    )
            } else {
                when {
                    UserDataSource.getUserCartSize() > 0 ->
                        startActivity(Intent(this, CheckoutActivity::class.java))
                    UserDataSource.getUser()?.cartContent?.size!! > 0 ->
                        startActivity(Intent(this, CheckoutActivity::class.java))
                    else -> showPopUp(
                        getString(R.string.cart), getString(R.string.empty_cart_msg),
                        getString(android.R.string.ok), false
                    )
                }
            }
        }

        search_toolbar_layout.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

        updateCartNumber()
    }

    override fun onResume() {
        super.onResume()

        if (this is ProductsActivity || this is StoresActivity || this is CategoriesActivity ||
            this is SubCategoriesActivity || this is BrandsActivity || this is MyOrdersActivity
        )
            updateCartNumber()

        if (this is AuthenticationActivity || this is CompleteProfileActivity ||
            this is AddAddressActivity || this is DeliveryAuthenticationActivity
        ) {
            Utils.hideIntercom()
        } else {
            if (UserDataSource.getUser() != null || UserDataSource.getDeliveryUser() != null)
                Utils.showIntercom()
            else
                Utils.hideIntercom()
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
        val builder = MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_Rounded)
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
        val builder = MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_Rounded)
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
        val builder = MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_Rounded)
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
        super.attachBaseContext(newBase)
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

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) {
        if (event.message.contentEquals(UPDATE_CART_NUMBER)) {
            updateCartNumber()
        } else if (event.message.contentEquals(OPEN_CHECKOUT)) {
            startActivity(Intent(this, CheckoutActivity::class.java))
        }
    }

    private fun updateCartNumber() {

        if (UserDataSource.getUser() == null) {
            if (UserDataSource.getUserCartSize() > 0) {
                cart_number_textView.visibility = View.VISIBLE
                cart_number_textView.text = UserDataSource.getUserCartSize().toString()
            } else
                cart_number_textView.visibility = View.INVISIBLE
        } else {
            when {
                UserDataSource.getUser()?.cartContent?.size!! > 0 -> {
                    cart_number_textView.visibility = View.VISIBLE
                    cart_number_textView.text =
                        UserDataSource.getUser()?.cartContent?.size!!.toString()
                    UserDataSource.deleteCart()
                }
                UserDataSource.getUserCartSize() > 0 -> {
                    cart_number_textView.visibility = View.VISIBLE
                    cart_number_textView.text = UserDataSource.getUserCartSize().toString()
                }
                else -> cart_number_textView.visibility = View.INVISIBLE
            }
        }
    }
}
