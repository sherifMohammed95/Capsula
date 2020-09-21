package com.blueMarketing.capsula.ui.terms

import androidx.lifecycle.viewModelScope
import com.blueMarketing.base.BaseViewModel
import com.blueMarketing.capsula.data.repository.GeneralRepository
import com.blueMarketing.capsula.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class TermsViewModel(private val repo: GeneralRepository) : BaseViewModel<TermsNavigator>() {
    var termsResponse = SingleLiveEvent<String>()

    init {
        initRepository(repo)
        this.termsResponse = repo.termsResponse
        getTerms()
    }

    private fun getTerms() {
        viewModelScope.launch(IO) {
            repo.getTerms()
        }
    }
}