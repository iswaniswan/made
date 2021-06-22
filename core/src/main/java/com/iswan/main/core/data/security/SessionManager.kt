package com.iswan.main.core.data.security

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.JsonObject
import com.securepreferences.SecurePreferences
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SessionManager @Inject constructor(
    context: Context
) {

    private var pref: SharedPreferences = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val spec = KeyGenParameterSpec.Builder(
            MasterKey.DEFAULT_MASTER_KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        ).setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setKeySize(MasterKey.DEFAULT_AES_GCM_MASTER_KEY_SIZE)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .build()

        val masterKey = MasterKey.Builder(context)
            .setKeyGenParameterSpec(spec)
            .build()

        EncryptedSharedPreferences
            .create(
                context,
                "SESSION",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
    } else {
        SecurePreferences(context)
    }

    private var editor: SharedPreferences.Editor = pref.edit()

    companion object {
        const val NAME = "name"
        const val IS_SUBSCRIBED = "isPremium"
    }

    fun saveToPreferences(obj: JsonObject) {
        editor
            .putString(NAME, obj.get("name").asString)
            .putBoolean(IS_SUBSCRIBED, obj.get("isSubscribed").asBoolean)
            .commit()
    }

    fun getFromPreferences(): JsonObject {
        return JsonObject().apply {
            addProperty("name", pref.getString(NAME, ""))
            addProperty("isSubscribed", pref.getBoolean(IS_SUBSCRIBED, false))
        }
    }


//    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
//
//    fun setPref(obj: JsonObject) {
//        preferences.edit {
//            putString(NAME, obj.get("name").asString)
//            putBoolean(IS_PREMIUM, obj.get("isPremium").asBoolean)
//        }
//    }
//
//    fun getPref(): JsonObject {
//        return JsonObject().apply {
//            this.addProperty("name", preferences.getString(NAME, ""))
//            this.addProperty("isPremium", preferences.getBoolean(IS_PREMIUM, false))
//        }
//    }


}