package com.freelance.capsoula.data.repository

import com.freelance.capsoula.data.requests.*
import com.freelance.capsoula.data.source.local.UserDataSource
import com.freelance.capsoula.utils.SingleLiveEvent
import io.reactivex.functions.Action
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthenticationRepository : BaseRepository() {

    val loginResponse = SingleLiveEvent<Void>()
    val registerResponse = SingleLiveEvent<Void>()
    val socialMediaResponse = SingleLiveEvent<Void>()
    val completeProfileResponse = SingleLiveEvent<Void>()
    val checkUserExistResponse = SingleLiveEvent<Void>()
    val resetPasswordResponse = SingleLiveEvent<Void>()

    suspend fun login(phoneOrEmail: String, password: String) {
        try {
            withContext(Main) {

                progressLoading.value = true
            }
            val request = LoginRequest()
            request.password = password
            request.phoneOrMail = phoneOrEmail
            val response = webService.login(request)
            if (response.isSuccessful && response.body()!!.data!!.token.isNotEmpty()) {
                withContext(Main) {

                    progressLoading.value = false
                }
                UserDataSource.saveUser(response.body()!!.data!!.authUserData)
                UserDataSource.saveUserToken(response.body()!!.data!!.token)

                withContext(Main) {
                    loginResponse.call()
                }
            } else {
                withContext(Main) {
                    handleApiError(response.errorBody()!!.string(), response.code())
                }
            }
        } catch (e: Exception) {
            withContext(Main) {
                handleNetworkError(Action {
                    CoroutineScope(IO).launch {
                        login(phoneOrEmail, password)
                    }
                })
            }
        }
    }

    suspend fun register(request: RegisterRequest) {
        try {
            withContext(Main) {

                progressLoading.value = true
            }

            val response = webService.register(request)
            if (response.isSuccessful && response.body()!!.data!!.token.isNotEmpty()) {
                withContext(Main) {
                    progressLoading.value = false
                }
                UserDataSource.saveUser(response.body()!!.data!!.authUserData)
                UserDataSource.saveUserToken(response.body()!!.data!!.token)
                withContext(Main) {
                    registerResponse.call()
                }
            } else {
                withContext(Main) {
                    handleApiError(response.errorBody()!!.string(), response.code())
                }
            }
        } catch (e: Exception) {
            withContext(Main) {
                handleNetworkError(Action {
                    CoroutineScope(IO).launch {
                        register(request)
                    }
                })
            }
        }
    }

    suspend fun loginWithGoogle(token: String) {
        try {
            withContext(Main) {

                progressLoading.value = true
            }

            val request = SocialMediaRequest()
            request.token = token
            val response = webService.loginWithGoogle(request)
            if (response.isSuccessful && response.body()!!.data!!.token.isNotEmpty()) {
                withContext(Main) {

                    progressLoading.value = false
                }
                UserDataSource.saveUser(response.body()!!.data!!.authUserData)
                UserDataSource.saveUserToken(response.body()!!.data!!.token)

                withContext(Main) {
                    socialMediaResponse.call()
                }
            } else {
                withContext(Main) {
                    handleApiError(response.errorBody()!!.string(), response.code())
                }
            }
        } catch (e: Exception) {
            withContext(Main) {
                handleNetworkError(Action {
                    CoroutineScope(IO).launch {
                        loginWithGoogle(token)
                    }
                })
            }
        }
    }

    suspend fun loginWithFacebook(token: String) {
        try {
            withContext(Main) {

                progressLoading.value = true
            }

            val request = SocialMediaRequest()
            request.token = token
            val response = webService.loginWithFacebook(request)
            if (response.isSuccessful && response.body()!!.data!!.token.isNotEmpty()) {
                withContext(Main) {

                    progressLoading.value = false
                }
                UserDataSource.saveUser(response.body()!!.data!!.authUserData)
                UserDataSource.saveUserToken(response.body()!!.data!!.token)

                withContext(Main) {
                    socialMediaResponse.call()
                }
            } else {
                withContext(Main) {
                    handleApiError(response.errorBody()!!.string(), response.code())
                }
            }
        } catch (e: Exception) {
            withContext(Main) {
                handleNetworkError(Action {
                    CoroutineScope(IO).launch {
                        loginWithFacebook(token)
                    }
                })
            }
        }
    }

    suspend fun completeProfile(request: CompleteProfileRequest) {
        try {
            withContext(Main) {

                progressLoading.value = true
            }

            val response = webService.completeProfile(request)
            if (response.isSuccessful) {
                withContext(Main) {
                    progressLoading.value = false
                }
                UserDataSource.saveUser(response.body()!!.data!!.authUserData)
                withContext(Main) {
                    completeProfileResponse.call()
                }
            } else {
                withContext(Main) {
                    handleApiError(response.errorBody()!!.string(), response.code())
                }
            }
        } catch (e: Exception) {
            withContext(Main) {
                handleNetworkError(Action {
                    CoroutineScope(IO).launch {
                        completeProfile(request)
                    }
                })
            }
        }
    }

    suspend fun checkUserExist(phone: String) {
        try {
            withContext(Main) {

                progressLoading.value = true
            }

            val response = webService.checkUserExist(phone)
            if (response.isSuccessful) {
                withContext(Main) {
                    progressLoading.value = false
                    checkUserExistResponse.call()
                }
            } else {
                withContext(Main) {
                    handleApiError(response.errorBody()!!.string(), response.code())
                }
            }
        } catch (e: Exception) {
            withContext(Main) {
                handleNetworkError(Action {
                    CoroutineScope(IO).launch {
                        checkUserExist(phone)
                    }
                })
            }
        }
    }

    suspend fun resetPassword(request: ResetPasswordRequest) {
        try {
            withContext(Main) {
                progressLoading.value = true
            }

            val response = webService.resetPassword(request)
            if (response.isSuccessful) {
                withContext(Main) {
                    progressLoading.value = false
                    resetPasswordResponse.call()
                }
            } else {
                withContext(Main) {
                    handleApiError(response.errorBody()!!.string(), response.code())
                }
            }
        } catch (e: Exception) {
            withContext(Main) {
                handleNetworkError(Action {
                    CoroutineScope(IO).launch {
                        resetPassword(request)
                    }
                })
            }
        }
    }
}