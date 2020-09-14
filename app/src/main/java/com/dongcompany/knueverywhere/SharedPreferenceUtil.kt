package com.dongcompany.knueverywhere

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceUtil(context: Context) {
    private val prefs: SharedPreferences =
            context.getSharedPreferences("AppSharedPreference", Context.MODE_PRIVATE)

    //학번
    fun setStdNum(value: String) = prefs.edit().putString("StdNum", value).apply()

    fun getStdNum(): String = prefs.getString("StdNum", "null").toString()

    //ID
    fun setID(value: String) = prefs.edit().putString("ID", value).apply()

    fun getID(): String = prefs.getString("ID", "null").toString()

    //이름
    fun setName(value: String) = prefs.edit().putString("Name", value).apply()

    fun getName(): String = prefs.getString("Name", "null").toString()

    //자동로그인
    fun getLogin(): Boolean = prefs.getBoolean("AutoLogin", false)

    fun setLogin(value: Boolean) = prefs.edit().putBoolean("AutoLogin", value).apply()


    //탐방 정보들(탐방 중인 정보들)

    //코스 선택 체크박스 저장 (0~3)
    fun setCourseCheckBox(index : Int, value:Boolean) = prefs.edit().putBoolean("Course" + index.toString(), value).apply()
    fun getCourseCheckBox(index:Int) = prefs.getBoolean("Course" + index.toString(), false)
}