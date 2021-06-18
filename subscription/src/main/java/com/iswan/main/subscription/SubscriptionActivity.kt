package com.iswan.main.subscription

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import com.iswan.main.thatchapterfan.R
import com.iswan.main.thatchapterfan.databinding.ActivitySubscriptionBinding
import com.iswan.main.thatchapterfan.di.SubscriptionDependencies
import com.iswan.main.thatchapterfan.utils.Preferences
import dagger.hilt.android.EntryPointAccessors
import javax.inject.Inject

class SubscriptionActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySubscriptionBinding

    @Inject
    lateinit var preferences: Preferences
    private var isPremium: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        initInject()
        super.onCreate(savedInstanceState)
        binding = ActivitySubscriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.title_subscription)

        loadPreferences()

        binding.btnActivate.setOnClickListener {
            hideKeyboard()
            val newPref = JsonObject()
            newPref.addProperty("name", binding.etName.text.toString())
            newPref.addProperty("isPremium", !isPremium)
            preferences.setPref(newPref)
            loadPreferences()
        }

    }

    private fun initInject() {
        DaggerSubscriptionComponent.builder()
            .context(this)
            .subscriptionDependencies(EntryPointAccessors.fromApplication(
                applicationContext, SubscriptionDependencies::class.java
            ))
            .build()
            .inject(this)
    }

    private fun loadPreferences() {
        val pref = preferences.getPref()
        isPremium = pref.get("isPremium").asBoolean
        with(binding) {
            tvSubscription.text =
                if (isPremium) getString(R.string.subscription_active)
                else getString(R.string.subscription_inactive)

            etName.apply {
                setText(pref.get("name").asString)
                isEnabled = isPremium == false
            }

            btnActivate.text =
                if (isPremium) getString(R.string.deactivate)
                else getString(R.string.activate)
        }
    }

    private fun hideKeyboard(){
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }
}