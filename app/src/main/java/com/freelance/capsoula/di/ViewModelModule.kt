package com.freelance.capsoula.di


import com.freelance.capsoula.custom.bottomSheet.BottomSheetViewModel
import com.freelance.capsoula.ui.addAddress.AddAddressViewModel
import com.freelance.capsoula.ui.authentication.AuthenticationViewModel
import com.freelance.capsoula.ui.brands.BrandsViewModel
import com.freelance.capsoula.ui.categories.CategoriesViewModel
import com.freelance.capsoula.ui.checkout.CheckoutViewModel
import com.freelance.capsoula.ui.checkout.fragment.cart.CartViewModel
import com.freelance.capsoula.ui.checkout.fragment.details.DetailsViewModel
import com.freelance.capsoula.ui.checkout.fragment.done.DoneViewModel
import com.freelance.capsoula.ui.completeProfile.CompleteProfileViewModel
import com.freelance.capsoula.ui.deliveryMan.deliveryAuthentication.DeliveryAuthenticationViewModel
import com.freelance.capsoula.ui.deliveryMan.deliveryAuthentication.fragments.deliveryLogin.DeliveryLoginViewModel
import com.freelance.capsoula.ui.forgetPassword.ForgetPasswordViewModel
import com.freelance.capsoula.ui.home.HomeViewModel
import com.freelance.capsoula.ui.more.MoreViewModel
import com.freelance.capsoula.ui.myOrders.MyOrdersViewModel
import com.freelance.capsoula.ui.orderDetails.OrderDetailsViewModel
import com.freelance.capsoula.ui.orderTracking.OrderTrackingViewModel
import com.freelance.capsoula.ui.productDetails.ProductDetailsViewModel
import com.freelance.capsoula.ui.products.ProductsViewModel
import com.freelance.capsoula.ui.resetPassword.ResetPasswordViewModel
import com.freelance.capsoula.ui.search.SearchViewModel
import com.freelance.capsoula.ui.stores.StoresViewModel
import com.freelance.capsoula.ui.subCategories.SubCategoriesViewModel
import com.freelance.capsoula.ui.userTypes.UserTypesViewModel
import com.freelance.capsoula.ui.verification.VerificationViewModel
import org.koin.androidx.experimental.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel<BottomSheetViewModel>()
    viewModel<AuthenticationViewModel>()
    viewModel<VerificationViewModel>()
    viewModel<AddAddressViewModel>()
    viewModel<CompleteProfileViewModel>()
    viewModel<ForgetPasswordViewModel>()
    viewModel<ResetPasswordViewModel>()
    viewModel<HomeViewModel>()
    viewModel<BrandsViewModel>()
    viewModel<CategoriesViewModel>()
    viewModel<StoresViewModel>()
    viewModel<SubCategoriesViewModel>()
    viewModel<ProductsViewModel>()
    viewModel<SearchViewModel>()
    viewModel<ProductDetailsViewModel>()
    viewModel<CheckoutViewModel>()
    viewModel<CartViewModel>()
    viewModel<MoreViewModel>()
    viewModel<DetailsViewModel>()
    viewModel<DoneViewModel>()
    viewModel<MyOrdersViewModel>()
    viewModel<OrderDetailsViewModel>()
    viewModel<OrderTrackingViewModel>()
    viewModel<UserTypesViewModel>()
    viewModel<DeliveryAuthenticationViewModel>()
    viewModel<DeliveryLoginViewModel>()
}