package com.teameverywhere.taximobility.util

import android.content.Context
import android.content.SharedPreferences

//이름, 번호, 자격증번호, 차종, 차량번호, 비밀번호
class PreferenceUtil(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("prefs_name", Context.MODE_PRIVATE)

    //이름
    fun getName(key: String, defValue: String): String{
        return prefs.getString(key, defValue).toString()
    }
    fun setName(key: String, defValue: String){
        prefs.edit().putString(key, defValue).apply()
    }

    //번호
    fun getPhone(key: String, defValue: String): String{
        return prefs.getString(key, defValue).toString()
    }
    fun setPhone(key: String, defValue: String){
        prefs.edit().putString(key, defValue).apply()
    }

    //자격증번호
    fun getLicense(key: String, defValue: String): String{
        return prefs.getString(key, defValue).toString()
    }
    fun setLicense(key: String, defValue: String){
        prefs.edit().putString(key, defValue).apply()
    }

    //차종
    fun getCar(key: String, defValue: String): String{
        return prefs.getString(key, defValue).toString()
    }
    fun setCar(key: String, defValue: String){
        prefs.edit().putString(key, defValue).apply()
    }

    //차량번호
    fun getCarNumber(key: String, defValue: String): String{
        return prefs.getString(key, defValue).toString()
    }
    fun setCarNumber(key: String, defValue: String){
        prefs.edit().putString(key, defValue).apply()
    }

    //차 제조사
    fun getCarCompany(key: String, defValue: String): String{
        return prefs.getString(key, defValue).toString()
    }
    fun setCarCompany(key: String, defValue: String){
        prefs.edit().putString(key, defValue).apply()
    }

    //비밀번호
    fun getPassword(key: String, defValue: String): String{
        return prefs.getString(key, defValue).toString()
    }
    fun setPassword(key: String, defValue: String){
        prefs.edit().putString(key, defValue).apply()
    }

    //자동로그인
    fun getRemember(key: String, defValue: Boolean): Boolean {
        return prefs.getBoolean(key, defValue)
    }
    fun setRemember(key: String, defValue: Boolean){
        prefs.edit().putBoolean(key, defValue).apply()
    }

    //차량변경
    fun getCarChange(key: String, defValue: String): String {
        return prefs.getString(key, defValue).toString()
    }
    fun setCarChange(key: String, defValue: String){
        prefs.edit().putString(key, defValue).apply()
    }

    //진동모드
    fun getIsVibration(key: String, defValue: Boolean): Boolean {
        return prefs.getBoolean(key, defValue)
    }
    fun setIsVibration(key: String, defValue: Boolean){
        prefs.edit().putBoolean(key, defValue).apply()
    }
}