package com.freelance.capsoula.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FAQ {
    var answer: String? = ""
    var question: String? = ""
    var isExpanded = false
}