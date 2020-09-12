package com.freelance.capsoula.utils

object Constants {
    const val USER = "user"
    const val DELIVERY_USER = "delivery_user"
    const val BEARER = "Bearer "
    const val TOKEN = "token"
    const val LANGUAGE = "language"
    const val PER_PAGE = 70
    const val FROM_WHERE = "from_where"
    const val USER_CART = "user_cart"
    const val UPDATE_CART_NUMBER = "update_cart_number"
    const val OPEN_CHECKOUT = "open_checkout"
    const val NOTIFICATIONS_IS_ENABLED = "notifications_is_enabled"
    const val FCM_TOKEN = "fcm_token"
    const val FROM_HISTORY = "from_history"
    const val FROM_CHANGE_PASSWORD = "from_change_password"
    const val IS_EDIT_MODE = "is_edit_mode"
    const val GPS_CODE = 105

    var REFRESH_DELIVERY_ORDER = false

    const val ORDER_IS_PROCESSING = 5

    const val INTERCOM_API_KEY = "android_sdk-e04c55cdabfd3596c71a4011672b78a21a1bcf19"
    const val INTERCOM_APP_ID = "zwlcn8xj"

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
    const val EXTRA_ORDER = "order"
    const val EXTRA_ORDER_ID = "order_id"
    const val EXTRA_ADD_NEW_ADDRESS = "add_new_address"


    /**
     * APIs Constants
     * **/
    const val BASE_URL = "http://capsulasa-001-site2.itempurl.com/api/"
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
    const val FROM_CART = 9
    const val FROM_CHECKOUT_DETAILS = 10
    const val FROM_ORDER_DETAILS = 11
    const val FROM_MORE = 12
    const val FROM_DELIVERY_PERSONAL_DETAILS = 13


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

    /**
     * Image numbers
     * **/
    const val INSURANCE_NUMBER_ID = 2
    const val PRESCRIPTION_ID = 1
    const val CAR_LICENSE = 3
    const val CAR_FRONT = 4
    const val CAR_BACK = 5
    const val CAR_REGISTRATION = 6
    const val NATIONAL_ID_IMAGE = 7

    /**
     * Payment methods
     * **/
    const val CASH = 1
    const val CREDIT_CARD = 4
    const val STC_PAY = 3
    const val MADA = 5
    const val GOOGLE_PAY = 6

    /**
     * Delivery man registration steps
     * **/
    const val PERSONAL_DETAILS_STEP = 1
    const val CAR_DETAILS_STEP = 2
    const val REQUIRED_DOCS_STEP = 3
}