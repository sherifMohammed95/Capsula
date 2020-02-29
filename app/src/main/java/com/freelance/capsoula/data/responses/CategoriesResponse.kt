package com.freelance.capsoula.data.responses

import com.freelance.capsoula.data.Category
import com.google.gson.annotations.SerializedName

class CategoriesResponse {

    @SerializedName("list")
    var categoriesList: ArrayList<Category>? = null

    var count = 0
}