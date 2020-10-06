package com.dongcompany.knueverywhere

import android.content.Context
import android.content.SharedPreferences

val c0arr = arrayOf("북문", "농장문", "테크노문", "동문", "정문", "수의대문", "쪽문", "조은문", "솔로문", "서문", "수영장문")

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

    //연락처

    //이름
    fun setPhone(value: String) = prefs.edit().putString("Phone", value).apply()

    fun getPhone(): String = prefs.getString("Phone", "null").toString()
    //자동로그인
    fun getAutoLogin(): Boolean = prefs.getBoolean("AutoLogin", false)

    fun setAutoLogin(value: Boolean) = prefs.edit().putBoolean("AutoLogin", value).apply()


    //현재 상태(탐방 중인지 여부)
    fun setTravelState(value: Boolean) = prefs.edit().putBoolean("TravelState", value).apply()
    fun getTravelState() = prefs.getBoolean("TravelState", false)

    //탐방 정보들(탐방 중인 정보들)
    fun setCourseInfo(index: Int, area: String, value: Boolean) = prefs.edit().putBoolean("Course" + index.toString() + area, value).apply()
    fun getCourseInfo(index: Int, area: String) = prefs.getBoolean("Course" + index.toString() + area, false)

    //코스 선택 체크박스 저장 (0~3)
    fun setCourseCheckBox(index: Int, value: Boolean) = prefs.edit().putBoolean("Course" + index.toString(), value).apply()
    fun getCourseCheckBox(index: Int) = prefs.getBoolean("Course" + index.toString(), false)
}