package com.blueMarketing.capsula.data.responses

import com.blueMarketing.capsula.data.Category
import com.google.gson.annotations.SerializedName

class CategoriesResponse {

    @SerializedName("list")
    var categoriesList: ArrayList<Category>? = null

    var count = 0
}