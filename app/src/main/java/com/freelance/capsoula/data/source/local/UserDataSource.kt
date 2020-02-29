package com.freelance.capsoula.data.source.local

import com.freelance.capsoula.data.User
import com.freelance.capsoula.utils.Constants.TOKEN
import com.freelance.capsoula.utils.Constants.USER
import com.freelance.capsoula.utils.preferencesGateway
import com.google.gson.Gson

class UserDataSource {
    companion object {
         fun saveUser(user: User?) {
            preferencesGateway.save(USER, Gson().toJson(user))
        }

         fun getUser(): User? {
            return Gson().fromJson(
                preferencesGateway.load(USER, ""),
                User::class.java
            )
        }

         fun saveUserToken(token: String) {
            preferencesGateway.save(TOKEN, token)
        }

         fun getToken(): String {
            return preferencesGateway.load(TOKEN, "")
        }
    }
}