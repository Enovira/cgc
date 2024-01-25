package com.yxh.cgc.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.preference.Preference
import com.yxh.cgc.App
import java.util.prefs.Preferences

class SharedPreferenceUtil() {

    private val name = "cgc"
    private val mode = Context.MODE_PRIVATE
    private var editor: Editor? = null
    private var sharedPreferences: SharedPreferences? = null

    init {
        sharedPreferences = App.instance().getContext().getSharedPreferences(name, mode)
        editor = sharedPreferences?.edit()
    }

    companion object {
        private var _instance: SharedPreferenceUtil? = null
        fun getInstance(): SharedPreferenceUtil {
            if (_instance == null) {
                synchronized(this) {
                    if (_instance == null) {
                        _instance = SharedPreferenceUtil()
                    }
                }
            }
            return _instance!!
        }
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return sharedPreferences?.getInt(key, defaultValue) ?: defaultValue
    }

    fun putInt(key: String, value: Int): Boolean {
        editor?.putInt(key, value)
        return editor?.commit() ?: false
    }
}