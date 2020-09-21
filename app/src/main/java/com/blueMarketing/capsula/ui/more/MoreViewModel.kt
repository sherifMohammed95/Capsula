package com.blueMarketing.capsula.ui.more

import androidx.lifecycle.viewModelScope
import com.blueMarketing.base.BaseViewModel
import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.data.MoreItem
import com.blueMarketing.capsula.data.repository.UserRepository
import com.blueMarketing.capsula.utils.Domain
import com.blueMarketing.capsula.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MoreViewModel(val repo: UserRepository) : BaseViewModel<MoreNavigator>() {

    var checkoutIdResponse = SingleLiveEvent<String>()
    var saveCardResponse = SingleLiveEvent<String>()
    var logoutResponse = SingleLiveEvent<Void>()
    var selectedPaymentMethodValue = -1
    var resoursePath:String? = ""

    init {
        initRepository(repo)
        this.checkoutIdResponse = repo.checkoutIdResponse
        this.saveCardResponse = repo.saveCardResponse
        this.logoutResponse = repo.logoutEvent
    }

    fun navigate(item: MoreItem) {

        when (item.text) {
            Domain.application.getString(R.string.my_orders) -> navigator?.openMyOrders()

            Domain.application.getString(R.string.personal_details) -> navigator?.openPersonalDetails()

            Domain.application.getString(R.string.login) -> navigator?.openLogin()

            Domain.application.getString(R.string.logout) -> logout()

            Domain.application.getString(R.string.history) -> navigator?.openHistory()

            Domain.application.getString(R.string.my_wallet) -> navigator?.openMyWallet()

            Domain.application.getString(R.string.manage_payment_methods) -> navigator?.addPaymentCard()

            Domain.application.getString(R.string.change_lang) -> navigator?.changeLanguage()

            Domain.application.getString(R.string.about) -> navigator?.openAbout()

            Domain.application.getString(R.string.faqs) -> navigator?.openFAQs()
        }
    }

    fun prepareRegistration() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.prepareRegistration(selectedPaymentMethodValue)
        }
    }

    fun saveCard() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.saveCard(selectedPaymentMethodValue,resoursePath!!)
        }
    }

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.logout()
        }
    }
}