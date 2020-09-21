package com.blueMarketing.base

import com.google.gson.annotations.SerializedName

open class BaseResponse<T> {

    @SerializedName("code")
    var code: Int = 0
    @SerializedName("error")
    var error: String = ""
    @SerializedName("data")
    var data: T? = null

}
