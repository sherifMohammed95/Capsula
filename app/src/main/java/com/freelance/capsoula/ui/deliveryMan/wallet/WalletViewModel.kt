package com.freelance.capsoula.ui.deliveryMan.wallet

import androidx.lifecycle.viewModelScope
import com.freelance.base.BaseViewModel
import com.freelance.capsoula.data.Wallet
import com.freelance.capsoula.data.repository.DeliveryRepository
import com.freelance.capsoula.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WalletViewModel(private val repo: DeliveryRepository) : BaseViewModel<WalletNavigator>() {

    var walletResponse = SingleLiveEvent<Wallet>()

    init {
        initRepository(repo)
        this.walletResponse = repo.walletResponse
        loadWallet()
    }

    private fun loadWallet() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getWallet()
        }
    }
}