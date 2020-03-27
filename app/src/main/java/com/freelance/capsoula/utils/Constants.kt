package com.freelance.capsoula.utils

object Constants {
    const val USER = "user"
    const val BEARER = "Bearer "
    const val TOKEN = "token"
    const val LANGUAGE = "language"
    const val PER_PAGE = 50
    const val FROM_WHERE = "from_where"
    const val USER_CART = "user_cart"
    const val UPDATE_CART_NUMBER = "update_cart_number"
    const val OPEN_CHECKOUT = "open_checkout"

    /**
     * Extras
     * */
    const val EXTRA_REGISTER_REQUEST = "register_request"
    const val EXTRA_COMPLETE_PROFILE_REQUEST = "complete_profile_request"
    const val EXTRA_PHONE = "phone"
    const val EXTRA_STORE_ID = "store_id"
    const val EXTRA_BRAND_ID = "brand_id"
    const val EXTRA_SUB_CATEGORY = "sub_category"
    const val EXTRA_CATEGORY = "category"
    const val EXTRA_SELECTION_TITLE = "selection_title"
    const val EXTRA_SELECTION_LIST = "selection_list"
    const val EXTRA_PRODUCT = "product"


    /**
     * APIs Constants
     * **/
    const val BASE_URL = "http://capsula-001-site1.atempurl.com/api/"
    const val CONTENT_TYPE = "application/json"


    /**
     * Screen numbers
     * **/

    const val REGISTER_SCREEN = 1
    const val FORGET_PASSWORD_SCREEN = 2
    const val COMPLETE_PROFILE_SCREEN = 3
    const val FROM_TOP_RATED = 4
    const val FROM_BEST_SELLER = 5
    const val FROM_FREE_DELIVERY = 6
    const val FROM_BRANDS = 7
    const val FROM_SUB_CATEGORIES = 8


    /**
     * GPS Code
     */
    const val GPS_REQUEST = 4321


    /**
     * Offer types
     * **/

    const val FREE_DELIVERY_OFFER = 1
    const val PRODUCT_OFFER = 2
    const val DISCOUNT_OFFER = 3
}