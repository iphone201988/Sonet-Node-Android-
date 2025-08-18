package com.tech.sonet.data.local

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.tech.sonet.data.model.response.LoginResponse
import com.tech.sonet.utils.getValue
import com.tech.sonet.utils.saveValue

class CustomSharedPrefManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "custom_prefs" // ✅ Added missing constant
        const val USER = "user"
    }

    fun saveUser(bean: LoginResponse) {
        val editor = sharedPreferences.edit()
        editor.putString(USER, Gson().toJson(bean)) // ✅ Fixed incorrect reference
        editor.apply()
    }

    fun getCurrentUser(): LoginResponse? {
        val s: String? = sharedPreferences.getString(USER, null) // ✅ Fixed incorrect reference
        return Gson().fromJson(s, LoginResponse::class.java)
    }


    fun getLocation(): Boolean? {
        return sharedPreferences.getValue(SharedPrefManager.KEY.LOCATION, true)
    }
    fun saveLocation(type: Boolean) {
        sharedPreferences.saveValue(SharedPrefManager.KEY.LOCATION, type)
    }

    fun clearUserData() {
        sharedPreferences.edit().clear().apply()
    }
}
