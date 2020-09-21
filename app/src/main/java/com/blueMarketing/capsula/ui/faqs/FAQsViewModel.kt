package com.blueMarketing.capsula.ui.faqs

import androidx.lifecycle.viewModelScope
import com.blueMarketing.base.BaseViewModel
import com.blueMarketing.capsula.data.FAQ
import com.blueMarketing.capsula.data.repository.GeneralRepository
import com.blueMarketing.capsula.utils.SingleLiveEvent
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