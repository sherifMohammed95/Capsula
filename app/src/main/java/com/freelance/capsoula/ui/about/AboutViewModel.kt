package com.freelance.capsoula.ui.about

import androidx.lifecycle.viewModelScope
import com.freelance.base.BaseViewModel
import com.freelance.capsoula.data.repository.GeneralRepository
import com.freelance.capsoula.utils.SingleLiveEvent
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