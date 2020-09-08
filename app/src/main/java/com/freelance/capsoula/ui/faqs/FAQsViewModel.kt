package com.freelance.capsoula.ui.faqs

import androidx.lifecycle.viewModelScope
import com.freelance.base.BaseViewModel
import com.freelance.capsoula.data.FAQ
import com.freelance.capsoula.data.repository.GeneralRepository
import com.freelance.capsoula.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FAQsViewModel(private val repo: GeneralRepository) : BaseViewModel<FAQsNavigator>() {

    var faqsResponse = SingleLiveEvent<ArrayList<FAQ>>()

    init {
        initRepository(repo)
        this.faqsResponse = repo.faqsResponse
        getFAQs()
    }

    private fun getFAQs() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getFAQs()
        }
    }
}