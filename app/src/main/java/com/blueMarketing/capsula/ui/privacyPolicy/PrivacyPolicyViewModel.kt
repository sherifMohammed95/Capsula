package com.blueMarketing.capsula.ui.privacyPolicy

import androidx.lifecycle.viewModelScope
import com.blueMarketing.base.BaseViewModel
import com.blueMarketing.capsula.data.FAQ
import com.blueMarketing.capsula.data.Policy
import com.blueMarketing.capsula.data.repository.GeneralRepository
import com.blueMarketing.capsula.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PrivacyPolicyViewModel(private val repo: GeneralRepository) :
    BaseViewModel<PrivacyPolicyNavigator>() {

    var policyResponse = SingleLiveEvent<ArrayList<Policy>>()

    init {
        initRepository(repo)
        this.policyResponse = repo.policyResponse
        getPrivacyPolicy()
    }

    private fun getPrivacyPolicy() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getPrivacyPolicy()
        }
    }
}