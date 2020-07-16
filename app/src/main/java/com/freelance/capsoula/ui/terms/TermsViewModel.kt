package com.freelance.capsoula.ui.terms

import androidx.lifecycle.viewModelScope
import com.freelance.base.BaseViewModel
import com.freelance.capsoula.data.repository.GeneralRepository
import com.freelance.capsoula.utils.SingleLiveEvent
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