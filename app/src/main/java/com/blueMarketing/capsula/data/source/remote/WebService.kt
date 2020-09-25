package com.blueMarketing.capsula.data.source.remote

import com.blueMarketing.base.BaseResponse
import com.blueMarketing.capsula.data.DeliveryOrder
import com.blueMarketing.capsula.data.Order
import com.blueMarketing.capsula.data.Wallet
import com.blueMarketing.capsula.data.requests.CartRequest
import com.blueMarketing.capsula.data.requests.*
import com.blueMarketing.capsula.data.responses.*
import retrofit2.Response
import retrofit2.http.*

interface WebService {

    @POST("Authentication/Login")
    suspend fun login(@Body request: LoginRequest):
            Response<BaseResponse<AuthenticationResponse>>

    @POST("DeliveryManRegisteration/Login")
    suspend fun deliveryLogin(@Body request: LoginRequest):
            Response<BaseResponse<DeliveryAuthenticationResponse>>

    @POST("Authentication/Register")
    suspend fun register(@Body request: RegisterRequest):
            Response<BaseResponse<AuthenticationResponse>>

    @POST("Authentication/Google")
    suspend fun loginWithGoogle(@Body request: SocialMediaRequest):
            Response<BaseResponse<AuthenticationResponse>>

    @POST("Authentication/Facebook")
    suspend fun loginWithFacebook(@Body request: SocialMediaRequest):
            Response<BaseResponse<AuthenticationResponse>>

    @POST("UserProfile/AddUserAddress")
    suspend fun addAddress(@Body request: AddAddressRequest):
            Response<BaseResponse<AuthenticationResponse>>

    @PUT("UserProfile/UpdateUser")
    suspend fun completeProfile(@Body request: CompleteProfileRequest):
            Response<BaseResponse<AuthenticationResponse>>

    @GET("UserProfile/CheckUserExist/{phone}")
    suspend fun checkUserExist(@Path("phone") phone: String): Response<BaseResponse<Any>>

    @POST("UserProfile/ForgetPassword")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<BaseResponse<Any>>

    @PUT("UserProfile/ChangePassword")
    suspend fun changePassword(@Body request: ChangePasswordRequest): Response<BaseResponse<String>>

    @GET("Home/GetHomeData")
    suspend fun getHomeData(): Response<BaseResponse<HomeResponse>>

    @GET("DeliveryMan/GetOrders")
    suspend fun getDeliveryHomeData(): Response<BaseResponse<DeliveryOrdersResponse>>

    @GET("Brand/GetBrands")
    suspend fun getBrands(
        @Query("PageNumber") pageNo: Int,
        @Query("PageSize") pageSize: Int
    ): Response<BaseResponse<BrandsResponse>>

    @GET("Category/GetCategories")
    suspend fun getCategories(
        @Query("PageNumber") pageNo: Int,
        @Query("PageSize") pageSize: Int
    ): Response<BaseResponse<CategoriesResponse>>

    @GET("Category/GetCategoriesByStoreId")
    suspend fun getStoreCategories(
        @Query("PageNumber") pageNo: Int,
        @Query("PageSize") pageSize: Int,
        @Query("storeId") storeId: Int
    ): Response<BaseResponse<CategoriesResponse>>

    @GET("Category/GetSubCategories")
    suspend fun getSubCategories(
        @Query("PageNumber") pageNo: Int,
        @Query("PageSize") pageSize: Int,
        @Query("categoryId") categoryId: Int
    ): Response<BaseResponse<CategoriesResponse>>

    @GET("Category/GetSubCategoriesByStoreId")
    suspend fun getStoreSubCategories(
        @Query("PageNumber") pageNo: Int,
        @Query("PageSize") pageSize: Int,
        @Query("categoryId") categoryId: Int,
        @Query("storeId") storeId: Int
    ): Response<BaseResponse<CategoriesResponse>>

    @GET("Store/GetStores")
    suspend fun getStores(
        @Query("PageNumber") pageNo: Int,
        @Query("PageSize") pageSize: Int
    ): Response<BaseResponse<StoresResponse>>

    @GET("Item/GetItemsByBrandId")
    suspend fun getBrandProducts(
        @Query("PageNumber") pageNo: Int,
        @Query("PageSize") pageSize: Int,
        @Query("brandId") brandId: Int
    ): Response<BaseResponse<ProductsResponse>>

    @GET("Item/GetItemsBySubCategoryId")
    suspend fun getSubCategoryProducts(
        @Query("PageNumber") pageNo: Int,
        @Query("PageSize") pageSize: Int,
        @Query("subCategoryId") subCategoryId: Int
    ): Response<BaseResponse<ProductsResponse>>

    @GET("Item/GetItemsByStoreCategoryId")
    suspend fun getCategoryProducts(
        @Query("PageNumber") pageNo: Int,
        @Query("PageSize") pageSize: Int,
        @Query("categoryId") categoryId: Int,
        @Query("storeId") storeId: Int
    ): Response<BaseResponse<ProductsResponse>>

    @GET("Item/GetItemsByStoreSubCategoryId")
    suspend fun getStoreProducts(
        @Query("PageNumber") pageNo: Int,
        @Query("PageSize") pageSize: Int,
        @Query("subCategoryId") subCategoryId: Int,
        @Query("storeId") storeId: Int
    ): Response<BaseResponse<ProductsResponse>>

    @GET("Item/GetTopRatingItems")
    suspend fun getTopRated(
        @Query("PageNumber") pageNo: Int,
        @Query("PageSize") pageSize: Int
    ): Response<BaseResponse<ProductsResponse>>

    @GET("Item/GetTopSellingItems")
    suspend fun getBestSellers(
        @Query("PageNumber") pageNo: Int,
        @Query("PageSize") pageSize: Int
    ): Response<BaseResponse<ProductsResponse>>

    @GET("Item/GetFreeDeliveryItems")
    suspend fun getFreeDelivery(
        @Query("PageNumber") pageNo: Int,
        @Query("PageSize") pageSize: Int
    ): Response<BaseResponse<ProductsResponse>>

    @GET("Item/ItemsSearch")
    suspend fun getSearchResults(
        @Query("itemName") searchText: String,
        @Query("filterType") filterType: Int,
        @Query("PageNumber") pageNo: Int,
        @Query("PageSize") pageSize: Int
    ): Response<BaseResponse<ProductsResponse>>

    @POST("Cart/Add")
    suspend fun addProductsToCart(
        @Body cartRequestItems: ArrayList<CartRequest>
    ): Response<BaseResponse<ProductsResponse>>

    @DELETE("Cart/Delete/{mainId}")
    suspend fun deleteProductFromCart(
        @Path("mainId") mainId: Int
    ): Response<BaseResponse<ProductsResponse>>

    @DELETE("Cart/DeleteAll")
    suspend fun deleteCart(): Response<BaseResponse<String>>

    @PUT("Cart/Update")
    suspend fun updateCart(@Body cartRequest: CartRequest): Response<BaseResponse<ProductsResponse>>

    @PUT("UserProfile/UpdateDefaultAddress/{addressId}")
    suspend fun updateDefaultAddress(@Path("addressId") addressId: Int):
            Response<BaseResponse<AuthenticationResponse>>

    @GET("Cart/GetContent")
    suspend fun getUpdatedCart(): Response<BaseResponse<UserCartResponse>>

    @GET("Cart/Validate")
    suspend fun validateCart(): Response<BaseResponse<String>>

    @POST("Checkout")
    suspend fun submitCheckoutDetails(@Body request: CheckoutDetailsRequest): Response<BaseResponse<String>>

    @GET("Order/GetOrders")
    suspend fun getOrders(): Response<BaseResponse<OrdersResponse>>

    @GET("Order/GetOrderDetails")
    suspend fun getOrderDetails(@Query("orderId") orderId: Int): Response<BaseResponse<Order>>

    @GET("DeliveryMan/GetOrderDetails/{orderId}")
    suspend fun getDeliveryOrderDetails(@Path("orderId") orderId: Int): Response<BaseResponse<DeliveryOrder>>

    @GET("Order/GetOrderTracking")
    suspend fun getOrderTracking(@Query("orderId") orderId: Int): Response<BaseResponse<OrderTrackingResponse>>

    @PUT("Order/cancelOrder")
    suspend fun cancelOrder(@Query("orderId") orderId: Int): Response<BaseResponse<String>>

    @GET("DeliveryManRegisteration/GetNationalities")
    suspend fun getNationalities(): Response<BaseResponse<NationalitiesResponse>>

    @GET("DeliveryManRegisteration/GetRegisterBasicData")
    suspend fun getRegisterBasicData(): Response<BaseResponse<DeliveryRegisterBasicResponse>>

    @GET("DeliveryManRegisteration/GetCarModels/{carId}")
    suspend fun getCarModels(@Path("carId") carId: Int): Response<BaseResponse<NationalitiesResponse>>

    @POST("DeliveryManRegisteration/Register")
    suspend fun deliveryRegister(@Body request: DeliveryRegisterRequest): Response<BaseResponse<String>>

    @PUT("DeliveryManRegisteration/Update")
    suspend fun updateDeliveryProfile(@Body request: EditDeliveryProfileRequest):
            Response<BaseResponse<DeliveryAuthenticationResponse>>

    @GET("DeliveryMan/StartDelivery/{orderId}")
    suspend fun startDelivery(@Path("orderId") orderId: Int): Response<BaseResponse<Any>>

    @GET("DeliveryMan/EndDelivery/{orderId}")
    suspend fun endDelivery(@Path("orderId") orderId: Int): Response<BaseResponse<Any>>

    @GET("DeliveryMan/GetOrdersHistory")
    suspend fun getHistory(
        @Query("PageNumber") pageNo: Int,
        @Query("PageSize") pageSize: Int,
        @Query("date") date: String
    ): Response<BaseResponse<DeliveryOrdersResponse>>

    @GET("Home/TermsAndConditions")
    suspend fun getTerms(): Response<BaseResponse<String>>

    @GET("Home/About")
    suspend fun getAbout(): Response<BaseResponse<String>>

    @GET("Home/FAQs")
    suspend fun getFAQs(): Response<BaseResponse<FaqsResponse>>

    @GET("CheckOut/GetDeliveryCost")
    suspend fun getDeliveryCost(): Response<BaseResponse<PaymentDetailsResponse>>

    @GET("CheckOut/PrepareCheckout/{paymentMethodType}")
    suspend fun prepareCheckout(@Path("paymentMethodType") paymentMethodType: Int):
            Response<BaseResponse<String>>

    @GET("CheckOut/PrepareRegisteration/{registerMethodType}")
    suspend fun prepareRegistration(@Path("registerMethodType") paymentMethodType: Int):
            Response<BaseResponse<String>>

    @GET("CheckOut/SaveCard")
    suspend fun saveCard(
        @Query("registerMethodType") paymentMethodType: Int,
        @Query("resourcePath") resourcePath: String
    ): Response<BaseResponse<String>>

    @GET("DeliveryMan/Wallet")
    suspend fun getWallet(): Response<BaseResponse<Wallet>>

    @PUT("DeliveryManRegisteration/UpdateLocation")
    suspend fun updateDeliveryCurrentLocation(@Body request: AddAddressRequest):
            Response<BaseResponse<Any>>

    @GET("Authentication/Logout")
    suspend fun logout(): Response<BaseResponse<String>>

    @POST("Authentication/RefreshDevice")
    suspend fun refreshDevice(@Body request: RefreshDeviceRequest): Response<BaseResponse<Any>>

    @GET("UserProfile/GetNotifications")
    suspend fun getNotifications(
        @Query("PageNumber") pageNo: Int,
        @Query("PageSize") pageSize: Int
    ): Response<BaseResponse<NotificationsResponse>>
}
