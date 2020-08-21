package com.freelance.capsoula.data

class User {
    var userId = 0
    var name: String? = null
    var email: String? = null
    var phone: String? = null
    var imageLink: String? = null
    var userAddresses: ArrayList<UserAddress>? = null
    var cartContent: ArrayList<Product>? = null
    var defaultAddress: UserAddress? = null
}