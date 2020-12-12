package com.blueMarketing.capsula.ui.more

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.blueMarketing.base.BaseActivity
import com.blueMarketing.base.BaseRecyclerAdapter
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.custom.bottomSheet.BottomSelectionFragment
import com.blueMarketing.capsula.data.AppLanguage
import com.blueMarketing.capsula.data.MoreItem
import com.blueMarketing.capsula.data.OrderStatus
import com.blueMarketing.capsula.data.PaymentMethod
import com.blueMarketing.capsula.data.source.local.UserDataSource
import com.blueMarketing.capsula.databinding.ActivityMoreBinding
import com.blueMarketing.capsula.ui.about.AboutActivity
import com.blueMarketing.capsula.ui.authentication.AuthenticationActivity
import com.blueMarketing.capsula.ui.deliveryMan.history.HistoryActivity
import com.blueMarketing.capsula.ui.deliveryMan.viewProfile.ViewProfileActivity
import com.blueMarketing.capsula.ui.deliveryMan.wallet.WalletActivity
import com.blueMarketing.capsula.ui.faqs.FaqsActivity
import com.blueMarketing.capsula.ui.more.adapters.MoreAdapter
import com.blueMarketing.capsula.ui.myOrders.MyOrdersActivity
import com.blueMarketing.capsula.ui.privacyPolicy.PrivacyPolicyActivity
import com.blueMarketing.capsula.ui.splash.SplashActivity
import com.blueMarketing.capsula.ui.userProfile.UserProfileActivity
import com.blueMarketing.capsula.ui.userTypes.UserTypesActivity
import com.blueMarketing.capsula.utils.AnimationUtils
import com.blueMarketing.capsula.utils.Constants
import com.blueMarketing.capsula.utils.Utils
import com.blueMarketing.capsula.utils.preferencesGateway
import com.oppwa.mobile.connect.checkout.meta.CheckoutSettings
import com.oppwa.mobile.connect.exception.PaymentError
import com.oppwa.mobile.connect.provider.Connect
import io.intercom.android.sdk.Intercom
import io.reactivex.functions.Action
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
    private val appLangList: ArrayList<AppLanguage> by inject(named(LANGUAGES))

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
            showPopUp(
                R.string.deduction_message, getString(android.R.string.cancel),
                DialogInterface.OnClickListener { dialog, _ ->
                    dialog.dismiss()
                }, getString(android.R.string.ok),
                DialogInterface.OnClickListener { _, _ ->
                    openHyperPay(it)
                }, false
            )

        })

        mViewModel.saveCardResponse.observe(this, Observer {
//            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            showPopUp("", it, getString(android.R.string.ok), false)
        })

        mViewModel.logoutResponse.observe(this, Observer {
            logout()
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
        else if (UserDataSource.getDeliveryUser() != null)
            startActivity(Intent(this, ViewProfileActivity::class.java))
    }

    override fun openLogin() {
        val intent = Intent(this, AuthenticationActivity::class.java)
        intent.putExtra(Constants.FROM_WHERE, Constants.FROM_MORE)
        startActivity(intent)
    }

    override fun logout() {
        UserDataSource.saveUser(null)
        UserDataSource.saveDeliveryUser(null)
        Intercom.client().logout()
        UserDataSource.saveUserToken("")
        val intent = Intent(this, UserTypesActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    override fun openHistory() {
        startActivity(Intent(this, HistoryActivity::class.java))
    }

    override fun openMyWallet() {
        startActivity(Intent(this, WalletActivity::class.java))
    }

    override fun addPaymentCard() {
        showPaymentMethodSheet()
//        Utils.openLink(this, "https://capsula.cc/swagger/index.html")
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

    override fun changeLanguage() {
        val selectedPos = if (preferencesGateway.load(Constants.LANGUAGE, "en")
                .contentEquals("en")
        )
            0
        else
            1
        val fragment = BottomSelectionFragment.newInstance(
            getString(R.string.change_lang),
            appLangList,
            Action2 { pos, item ->
                when (pos) {
                    0 -> {
                        val newLang = "en"
                        preferencesGateway.save(Constants.LANGUAGE, newLang)
                        updateConfig(newLang)
                        setNewLocale(newLang.toUpperCase())
                        openSplash()
                    }
                    1 -> {
                        val newLang = "ar"
                        preferencesGateway.save(Constants.LANGUAGE, newLang)
                        updateConfig(newLang)
                        setNewLocale(newLang.toUpperCase())
                        openSplash()
                    }
                }
            },
            selectedPos,
            showClearText = false,
            showIconImage = false,
            showAddNewAddressText = false
        )
        fragment.show(supportFragmentManager, fragment.tag)
    }

    override fun openAbout() {
        startActivity(Intent(this, AboutActivity::class.java))
    }

    override fun openFAQs() {
        startActivity(Intent(this, FaqsActivity::class.java))
    }

    override fun openPrivacyPolicy() {
        startActivity(Intent(this, PrivacyPolicyActivity::class.java))
    }

    private fun openSplash() {
        val intent = Intent(this, SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun openHyperPay(checkoutId: String) {
        val paymentBrands: MutableSet<String> = LinkedHashSet()
        if (mViewModel.selectedPaymentMethodValue == Constants.CREDIT_CARD) {
            paymentBrands.add("VISA")
            paymentBrands.add("MASTER")
        } else if (mViewModel.selectedPaymentMethodValue == Constants.MADA)
            paymentBrands.add("MADA")

        val checkoutSettings =
            CheckoutSettings(checkoutId, paymentBrands, Connect.ProviderMode.LIVE);

        val currentLang: String = preferencesGateway.load(Constants.LANGUAGE, "en")
        if (currentLang.contentEquals("en"))
            checkoutSettings.locale = "en_US";
        else
            checkoutSettings.locale = "ar_AR";

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
