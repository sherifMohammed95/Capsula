package com.blueMarketing.capsula.di


import com.blueMarketing.capsula.custom.bottomSheet.BottomSheetViewModel
import com.blueMarketing.capsula.ui.about.AboutViewModel
import com.blueMarketing.capsula.ui.addAddress.AddAddressViewModel
import com.blueMarketing.capsula.ui.authentication.AuthenticationViewModel
import com.blueMarketing.capsula.ui.brands.BrandsViewModel
import com.blueMarketing.capsula.ui.categories.CategoriesViewModel
import com.blueMarketing.capsula.ui.checkout.CheckoutViewModel
import com.blueMarketing.capsula.ui.checkout.fragment.cart.CartViewModel
import com.blueMarketing.capsula.ui.checkout.fragment.details.DetailsViewModel
import com.blueMarketing.capsula.ui.checkout.fragment.done.DoneViewModel
import com.blueMarketing.capsula.ui.completeProfile.CompleteProfileViewModel
import com.blueMarketing.capsula.ui.deliveryMan.deliveryAuthentication.DeliveryAuthenticationViewModel
import com.blueMarketing.capsula.ui.deliveryMan.deliveryAuthentication.fragments.deliveryLogin.DeliveryLoginViewModel
import com.blueMarketing.capsula.ui.deliveryMan.deliveryAuthentication.fragments.deliveryRegister.steps.carDetails.CarDetailsViewModel
import com.blueMarketing.capsula.ui.deliveryMan.deliveryAuthentication.fragments.deliveryRegister.steps.personalDetails.PersonalDetailsViewModel
import com.blueMarketing.capsula.ui.deliveryMan.deliveryAuthentication.fragments.deliveryRegister.steps.requiredDocuments.RequiredDocumentsViewModel
import com.blueMarketing.capsula.ui.deliveryMan.deliveryHome.DeliveryHomeViewModel
import com.blueMarketing.capsula.ui.deliveryMan.deliveryOrderDetails.DeliveryOrderDetailsViewModel
import com.blueMarketing.capsula.ui.deliveryMan.editDeliveryProfile.EditDeliveryProfileViewModel
import com.blueMarketing.capsula.ui.deliveryMan.history.HistoryViewModel
import com.blueMarketing.capsula.ui.deliveryMan.viewProfile.ViewProfileViewModel
import com.blueMarketing.capsula.ui.deliveryMan.wallet.WalletViewModel
import com.blueMarketing.capsula.ui.editProfile.EditProfileViewModel
import com.blueMarketing.capsula.ui.faqs.FAQsViewModel
import com.blueMarketing.capsula.ui.forgetPassword.ForgetPasswordViewModel
import com.blueMarketing.capsula.ui.home.HomeViewModel
import com.blueMarketing.capsula.ui.more.MoreViewModel
import com.blueMarketing.capsula.ui.myOrders.MyOrdersViewModel
import com.blueMarketing.capsula.ui.notifications.NotificationsViewModel
import com.blueMarketing.capsula.ui.orderDetails.OrderDetailsViewModel
import com.blueMarketing.capsula.ui.orderTracking.OrderTrackingViewModel
import com.blueMarketing.capsula.ui.privacyPolicy.PrivacyPolicyViewModel
import com.blueMarketing.capsula.ui.productDetails.ProductDetailsViewModel
import com.blueMarketing.capsula.ui.products.ProductsViewModel
import com.blueMarketing.capsula.ui.resetPassword.ResetPasswordViewModel
import com.blueMarketing.capsula.ui.search.SearchViewModel
import com.blueMarketing.capsula.ui.stores.StoresViewModel
import com.blueMarketing.capsula.ui.subCategories.SubCategoriesViewModel
import com.blueMarketing.capsula.ui.terms.TermsViewModel
import com.blueMarketing.capsula.ui.userProfile.UserProfileViewModel
import com.blueMarketing.capsula.ui.userTypes.UserTypesViewModel
import com.blueMarketing.capsula.ui.verification.VerificationViewModel
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
    viewModel<PersonalDetailsViewModel>()
    viewModel<CarDetailsViewModel>()
    viewModel<RequiredDocumentsViewModel>()
    viewModel<DeliveryHomeViewModel>()
    viewModel<DeliveryOrderDetailsViewModel>()
    viewModel<HistoryViewModel>()
    viewModel<TermsViewModel>()
    viewModel<UserProfileViewModel>()
    viewModel<WalletViewModel>()
    viewModel<EditProfileViewModel>()
    viewModel<ViewProfileViewModel>()
    viewModel<EditDeliveryProfileViewModel>()
    viewModel<AboutViewModel>()
    viewModel<FAQsViewModel>()
    viewModel<NotificationsViewModel>()
    viewModel<PrivacyPolicyViewModel>()
}