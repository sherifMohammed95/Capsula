package com.freelance.capsoula.ui.more

import androidx.lifecycle.viewModelScope
import com.freelance.base.BaseViewModel
import com.freelance.capsoula.R
import com.freelance.capsoula.data.MoreItem
import com.freelance.capsoula.data.repository.CartRepository
import com.freelance.capsoula.data.repository.UserRepository
import com.freelance.capsoula.utils.Domain
import com.freelance.capsoula.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MoreViewModel(val repo: UserRepository) : BaseViewModel<MoreNavigator>() {

    var checkoutIdResponse = SingleLiveEvent<String>()
    var saveCardResponse = SingleLiveEvent<String>()
    var selectedPaymentMethodValue = -1
    var resoursePath:String? = ""

    init {
        initRepository(repo)
        this.checkoutIdResponse = repo.checkoutIdResponse
        this.saveCardResponse = repo.saveCardResponse
    }

    fun navigate(item: MoreItem) {

        when (item.text) {
            Domain.application.getString(R.string.my_orders) -> navigator?.openMyOrders()

            Domain.application.getString(R.string.personal_details) -> navigator?.openPersonalDetails()

            Domain.application.getString(R.string.login) -> navigator?.openLogin()

            Domain.application.getString(R.string.logout) -> navigator?.logout()

            Domain.application.getString(R.string.history) -> navigator?.openHistory()

            Domain.application.getString(R.string.my_wallet) -> navigator?.openMyWallet()

            Domain.application.getString(R.string.manage_payment_methods) -> navigator?.addPaymentCard()

            Domain.application.getString(R.string.change_lang) -> navigator?.changeLanguage()
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
}