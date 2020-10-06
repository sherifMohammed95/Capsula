package com.blueMarketing.capsula.data.repository


import com.blueMarketing.capsula.R
import com.blueMarketing.capsula.data.source.remote.WebService
import com.blueMarketing.capsula.utils.Domain
import com.blueMarketing.capsula.utils.SingleLiveEvent
import io.reactivex.functions.Action
import org.json.JSONException
import org.json.JSONObject
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.io.IOException

open class BaseRepository : KoinComponent {

    val webService: WebService by inject()
    var networkMessageError = SingleLiveEvent<String>()
    var apiErrorMessage = SingleLiveEvent<String>()
    var progressLoading: SingleLiveEvent<Boolean> = SingleLiveEvent()
    var showLoadingLayout: SingleLiveEvent<Boolean> = SingleLiveEvent()
    var isPagingLoadingEvent: SingleLiveEvent<Boolean> = SingleLiveEvent()
    var mAction: Action? = null

    fun handleNetworkError(mAction: Action) {
        progressLoading.value = false
        showLoadingLayout.value = false
        isPagingLoadingEvent.value = false
//        if (!Utils.isConnectingToInternet(Domain.application)) {
        this.mAction = mAction
        networkMessageError.value = Domain.application.getString(R.string.no_internet_message)
        return
//        }
    }

    fun handleApiError(errorMessage: String, code: Int) {
        progressLoading.value = false
        showLoadingLayout.value = false
        isPagingLoadingEvent.value = false
        when (code) {
            422, 404, 403, 406, 405, 400, 401 -> try {
                try {
                    val jsonObject = JSONObject(errorMessage)
                    apiErrorMessage.value = jsonObject.getString("errors")

                } catch (e1: JSONException) {
                    e1.printStackTrace()
                }

            } catch (e1: IOException) {
                e1.printStackTrace()
            }
            500, 503 -> {
                apiErrorMessage.value = (Domain.application.getString(R.string.went_wrong))
            }
        }
    }
}