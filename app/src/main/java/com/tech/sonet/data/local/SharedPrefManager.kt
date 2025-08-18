package com.tech.sonet.data.local

import android.content.SharedPreferences
import com.google.gson.Gson
import com.tech.sonet.data.model.response.LoginResponse
import com.tech.sonet.utils.getValue
import com.tech.sonet.utils.saveValue
import javax.inject.Inject

class SharedPrefManager @Inject constructor(private val sharedPreferences: SharedPreferences) {

    object KEY {
        const val USER = "user"
        const val USER_ID = "user_id"
        const val BEARER_TOKEN = "bearer_token"
        const val PROFILE_COMPLETED = "profile_completed"
        const val APPEARANCE_KEY = "appearance_key"
        const val LOCALE = "locale_key"
        const val TODAY = "today"
        const val ANS = "ans"
        const val LOCATION = "location"
        const val PUSHNOTIFICATION = "pushnotification"
        const val USER_TOKEN = "user_token"
        const val OPINION = "opinion"
        const val ONLINE = "online"
    }


    fun saveToken(token: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY.USER_TOKEN, token)
        editor.apply()
    }

    fun getToken(): String? {
        val token: String? = sharedPreferences.getString(KEY.USER_TOKEN, null)
        return token
    }
    fun saveUser(bean: LoginResponse) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY.USER, Gson().toJson(bean))
        editor.apply()
    }


    fun getCurrentUser(): LoginResponse? {
        val s: String? = sharedPreferences.getString(KEY.USER, null)
        return Gson().fromJson(s, LoginResponse::class.java)
    }

    fun saveUserId(userId: String) {
        sharedPreferences.saveValue(KEY.USER_ID, userId)
    }

    fun getCurrentUserId(): String? {
        return sharedPreferences.getValue(KEY.USER_ID, null)
    }

    fun saveLocation(type: Boolean) {
        sharedPreferences.saveValue(KEY.LOCATION, type)
    }

    fun getLocation(): Boolean? {
        return sharedPreferences.getValue(KEY.LOCATION, true)
    }
    fun savePushNotification(type: Boolean) {
        sharedPreferences.saveValue(KEY.PUSHNOTIFICATION, type)
    }

    fun getPushNotification(): Boolean? {
        return sharedPreferences.getValue(KEY.PUSHNOTIFICATION, false)
    }
    fun saveOpinion(opinion: String) {
        sharedPreferences.saveValue(KEY.OPINION, opinion)
    }

    fun getOpinion(): String? {
        return sharedPreferences.getValue(KEY.OPINION)
    }

    fun profileCompleted(isProfile: Boolean) {
        sharedPreferences.saveValue(KEY.PROFILE_COMPLETED, isProfile)
    }

    fun isProfileCompleted(): Boolean? {
        return sharedPreferences.getValue(KEY.PROFILE_COMPLETED, false)
    }

    fun setAppearance(type: Int) {
        sharedPreferences.saveValue(KEY.APPEARANCE_KEY, type)
    }

    fun getAppearance(): Int {
        return sharedPreferences.getInt(KEY.APPEARANCE_KEY, 0)
    }
    fun setOnline(type : Int) {
        sharedPreferences.saveValue(KEY.ONLINE, type)
    }
    fun getOnline() : Int{
      return  sharedPreferences.getInt(KEY.ONLINE , 0)
    }

    fun setLocaleType(type: String?) {
        sharedPreferences.saveValue(KEY.LOCALE, type)
    }

    fun getLocaleType(): String? {
        return sharedPreferences.getString(KEY.LOCALE, "en")
    }

    /*fun setTodayRecord(bean: TodaySleepRecord) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY.TODAY_RECORD, Gson().toJson(bean))
        editor.apply()
    }

    fun getTodayRecord(): TodaySleepRecord? {
        val s: String? = sharedPreferences.getString(KEY.TODAY_RECORD, null)
        return Gson().fromJson(s, TodaySleepRecord::class.java)
    }*/

    fun getToday(): Int {
        return sharedPreferences.getInt(KEY.TODAY, 0)
    }

    fun setToday(type: Int?) {
        sharedPreferences.saveValue(KEY.TODAY, type)

    }

    fun ansToday(): Int {
        return sharedPreferences.getInt(KEY.ANS, 0)
    }

    fun setAnsToday(type: Int?) {
        sharedPreferences.saveValue(KEY.ANS, type)
    }

    /*fun getToken(): String {
        return getCurrentUser()?.token?.let { token ->
            "Bearer $token"
        }.toString()
    }*/

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }

    fun saveCountNumber(count : Int){
        sharedPreferences.saveValue("COUNT", count)
    }

    fun getCountNumber() : Int{
        return sharedPreferences.getInt("COUNT", 0)
    }
}
