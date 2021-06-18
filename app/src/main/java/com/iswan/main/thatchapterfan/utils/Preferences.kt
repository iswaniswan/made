package com.iswan.main.thatchapterfan.utils

import android.content.Context
import androidx.core.content.edit
import com.google.gson.JsonObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Preferences @Inject constructor(
    private val context: Context
) {

    companion object {
        const val PREFS_NAME = "prefs"
        const val NAME = "name"
        const val IS_PREMIUM = "isPremium"
    }

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setPref(obj: JsonObject) {
        preferences.edit {
            putString(NAME, obj.get("name").asString)
            putBoolean(IS_PREMIUM, obj.get("isPremium").asBoolean)
        }
    }

    fun getPref(): JsonObject {
        return JsonObject().apply {
            this.addProperty("name", preferences.getString(NAME, ""))
            this.addProperty("isPremium", preferences.getBoolean(IS_PREMIUM, false))
        }
    }

}