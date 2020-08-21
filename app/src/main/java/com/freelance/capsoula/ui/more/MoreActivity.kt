package com.freelance.capsoula.ui.more

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.freelance.base.BaseActivity
import com.freelance.base.BaseRecyclerAdapter
import com.freelance.capsoula.R
import com.freelance.capsoula.custom.bottomSheet.BottomSelectionFragment
import com.freelance.capsoula.data.MoreItem
import com.freelance.capsoula.data.PaymentMethod
import com.freelance.capsoula.data.source.local.UserDataSource
import com.freelance.capsoula.databinding.ActivityMoreBinding
import com.freelance.capsoula.ui.authentication.AuthenticationActivity
import com.freelance.capsoula.ui.checkout.fragment.details.PAYMENT_METHOD_LIST
import com.freelance.capsoula.ui.deliveryMan.history.HistoryActivity
import com.freelance.capsoula.ui.home.HomeViewModel
import com.freelance.capsoula.ui.more.adapters.MoreAdapter
import com.freelance.capsoula.ui.myOrders.MyOrdersActivity
import com.freelance.capsoula.ui.userProfile.UserProfileActivity
import com.freelance.capsoula.ui.userTypes.UserTypesActivity
import com.freelance.capsoula.utils.AnimationUtils
import com.freelance.capsoula.utils.Constants
import com.freelance.capsoula.utils.ImageUtil
import com.oppwa.mobile.connect.checkout.meta.CheckoutSettings
import com.oppwa.mobile.connect.checkout.meta.CheckoutStorePaymentDetailsMode
import com.oppwa.mobile.connect.exception.PaymentError
import com.oppwa.mobile.connect.provider.Connect
import com.oppwa.mobile.connect.provider.Transaction
import io.reactivex.functions.Action
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_more.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named
import rx.functions.Action2

class MoreActivity : BaseActivity<ActivityMoreBinding, MoreViewModel>(), MoreNavigator,
    BaseRecyclerAdapter.OnITemClickListener<MoreItem> {

    private val mViewModel: MoreViewModel by viewModel()
    private val guestList: ArrayList<MoreItem> by inject(named(GUEST_MORE_LIST))
    private val loggedList: ArrayList<MoreItem> by inject(named(LOGGED_MORE_LIST))
    private val deliveryList: ArrayList<MoreItem> by inject(named(DELIVERY_MORE_LIST))
    private val paymentMethodList: ArrayList<PaymentMethod> by inject(named(ADD_PAYMENT_METHOD))

    private val mAdapter: MoreAdapter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Handler().postDelayed({
            AnimationUtils
                .circularTransition(viewDataBinding?.parent!!)
        }, 50)

        initRecyclerView()
        subscribeToLiveData()

    }

    private fun subscribeToLiveData() {
        mViewModel.checkoutIdResponse.observe(this, Observer {
            openHyperPay(it)
        })

        mViewModel.saveCardResponse.observe(this, Observer {
            showPopUp("", it, getString(android.R.string.ok), false)
        })
    }

    override fun onResume() {
        super.onResume()
        when {
            UserDataSource.getDeliveryUser() != null -> mAdapter.setData(deliveryList)
            UserDataSource.getUser() == null -> mAdapter.setData(guestList)
            else -> mAdapter.setData(loggedList)
        }
    }

    private fun initRecyclerView() {
        more_recyclerView.adapter = mAdapter
        mAdapter.onITemClickListener = this
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
        viewDataBinding?.navigator = this
        mViewModel.navigator = this
    }

    override fun onBackPressed() {
        closeMore()
    }

    override fun onItemClick(pos: Int, item: MoreItem) {
        mViewModel.navigate(item)
    }

    override fun closeMore() {
        Handler().postDelayed({
            AnimationUtils
                .circularReverseTransition(viewDataBinding?.parent!!, Action {
                    more_recyclerView.visibility = View.INVISIBLE
                    finish()
                })
        }, 50)
    }

    override fun openMyOrders() {
        startActivity(Intent(this, MyOrdersActivity::class.java))
    }

    override fun openPersonalDetails() {
        if (UserDataSource.getUser() != null)
            startActivity(Intent(this, UserProfileActivity::class.java))
    }

    override fun openLogin() {
        val intent = Intent(this, AuthenticationActivity::class.java)
        intent.putExtra(Constants.FROM_WHERE, Constants.FROM_MORE)
        startActivity(intent)
    }

    override fun logout() {
        UserDataSource.saveUser(null)
        UserDataSource.saveDeliveryUser(null)
        UserDataSource.saveUserToken("")
        val intent = Intent(this, UserTypesActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    override fun openHistory() {
        startActivity(Intent(this, HistoryActivity::class.java))
    }

    override fun openMyWallet() {
    }

    override fun addPaymentCard() {
        showPaymentMethodSheet()

    }

    override fun showPaymentMethodSheet() {
        val fragment = BottomSelectionFragment.newInstance(
            getString(R.string.payment_method),
            paymentMethodList,
            Action2 { pos, item ->
                when (pos) {
                    0 -> {
                        mViewModel.selectedPaymentMethodValue = Constants.CREDIT_CARD
                        mViewModel.prepareRegistration()
                    }
                    1 -> {
                        mViewModel.selectedPaymentMethodValue = Constants.MADA
                        mViewModel.prepareRegistration()
                    }
                }
            },
            -1,
            showClearText = false,
            showIconImage = true,
            showAddNewAddressText = false
        )
        fragment.show(supportFragmentManager, fragment.tag)
    }

    private fun openHyperPay(checkoutId: String) {
        val paymentBrands: MutableSet<String> = LinkedHashSet()
        if (mViewModel.selectedPaymentMethodValue == Constants.CREDIT_CARD) {
            paymentBrands.add("VISA")
            paymentBrands.add("MASTER")
        } else if (mViewModel.selectedPaymentMethodValue == Constants.MADA)
            paymentBrands.add("MADA")

        val checkoutSettings =
            CheckoutSettings(checkoutId, paymentBrands, Connect.ProviderMode.TEST);
        checkoutSettings.locale = "en_US";
//        checkoutSettings.storePaymentDetailsMode = CheckoutStorePaymentDetailsMode.ALWAYS
        checkoutSettings.shopperResultUrl = "capsula://result";

        val intent =
            Intent(this, com.oppwa.mobile.connect.checkout.dialog.CheckoutActivity::class.java)
        intent.putExtra(
            com.oppwa.mobile.connect.checkout.dialog.CheckoutActivity.CHECKOUT_SETTINGS,
            checkoutSettings
        )

        startActivityForResult(
            intent,
            com.oppwa.mobile.connect.checkout.dialog.CheckoutActivity.REQUEST_CODE_CHECKOUT
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {

            com.oppwa.mobile.connect.checkout.dialog.CheckoutActivity.RESULT_OK -> {

                /* resource path if needed */
                mViewModel.resoursePath =
                    data?.getStringExtra(
                        com.oppwa.mobile.connect.checkout.dialog
                            .CheckoutActivity.CHECKOUT_RESULT_RESOURCE_PATH
                    )

                mViewModel.saveCard()

//                Toast.makeText(this, mViewModel.resoursePath, Toast.LENGTH_LONG).show()

            }

            com.oppwa.mobile.connect.checkout.dialog.CheckoutActivity.RESULT_ERROR -> {
                val error: PaymentError? =
                    data?.getParcelableExtra(
                        com.oppwa.mobile.connect.checkout
                            .dialog.CheckoutActivity.CHECKOUT_RESULT_ERROR
                    )

                Toast.makeText(this, error?.errorInfo, Toast.LENGTH_LONG).show()

            }

        }
    }

}
