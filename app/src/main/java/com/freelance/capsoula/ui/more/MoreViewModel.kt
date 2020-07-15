package com.freelance.capsoula.ui.more

import androidx.lifecycle.viewModelScope
import com.freelance.base.BaseViewModel
import com.freelance.capsoula.R
import com.freelance.capsoula.data.MoreItem
import com.freelance.capsoula.data.repository.CartRepository
import com.freelance.capsoula.utils.Domain
import com.freelance.capsoula.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MoreViewModel(val repo: CartRepository) : BaseViewModel<MoreNavigator>() {
    var deleteCartResponse = SingleLiveEvent<Void>()

    init {
        initRepository(repo)
        this.deleteCartResponse = repo.deleteCartResponse
    }

    fun navigate(item: MoreItem) {

        when (item.text) {
            Domain.application.getString(R.string.my_orders) -> navigator?.openMyOrders()

            Domain.application.getString(R.string.personal_details) -> navigator?.openPersonalDetails()

            Domain.application.getString(R.string.login) -> navigator?.openLogin()

            Domain.application.getString(R.string.logout) -> navigator?.logout()

            Domain.application.getString(R.string.history) -> navigator?.openHistory()

            Domain.application.getString(R.string.my_wallet) -> navigator?.openMyWallet()
        }
    }

    private fun deleteCart() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteCart()
        }
    }
}