package com.freelance.capsoula.data.source.remote

import com.freelance.base.BaseResponse
import com.freelance.capsoula.data.Brand
import com.freelance.capsoula.data.requests.*
import com.freelance.capsoula.data.responses.*
import retrofit2.Response
import retrofit2.http.*

interface WebService {

    @POST("Authentication/Login")
    suspend fun login(@Body request: LoginRequest):
            Response<BaseResponse<AuthenticationResponse>>

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

    @POST("UserProfile/ChangeUserPassword")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<BaseResponse<Any>>

    @GET("Home/GetHomeData")
    suspend fun getHomeData(): Response<BaseResponse<HomeResponse>>

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
}
