package com.blueMarketing.capsula.ui.deliveryMan.wallet

import androidx.lifecycle.viewModelScope
import com.blueMarketing.base.BaseViewModel
import com.blueMarketing.capsula.data.Wallet
import com.blueMarketing.capsula.data.repository.DeliveryRepository
import com.blueMarketing.capsula.utils.SingleLiveEvent
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