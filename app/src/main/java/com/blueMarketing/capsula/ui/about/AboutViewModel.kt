package com.blueMarketing.capsula.ui.about

import androidx.lifecycle.viewModelScope
import com.blueMarketing.base.BaseViewModel
import com.blueMarketing.capsula.data.repository.GeneralRepository
import com.blueMarketing.capsula.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AboutViewModel(private val repo: GeneralRepository) : BaseViewModel<AboutNavigator>() {
    var aboutResponse = SingleLiveEvent<String>()

    init {
        initRepository(repo)
        this.aboutResponse = repo.aboutResponse
        getAbout()
    }

    private fun getAbout() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getAbout()
        }
    }
}