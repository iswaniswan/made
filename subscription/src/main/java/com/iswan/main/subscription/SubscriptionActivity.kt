package com.iswan.main.subscription

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import com.iswan.main.thatchapterfan.R
import com.iswan.main.thatchapterfan.databinding.ActivitySubscriptionBinding
import com.iswan.main.thatchapterfan.di.SubscriptionDependencies
import dagger.hilt.android.EntryPointAccessors
import javax.inject.Inject

class SubscriptionActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: SubscriptionViewModelFactory
    private val viewModel: SubscriptionViewModel by viewModels { factory }

    private lateinit var binding: ActivitySubscriptionBinding
    private var userName: String = ""
    private var isSubscribed: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        initInject()
        super.onCreate(savedInstanceState)
        binding = ActivitySubscriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.title_subscription)

        loadPreferences()

        binding.btnActivate.setOnClickListener {
            hideKeyboard()
            subscribeUser()
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

    private fun subscribeUser() {
        userName = binding.etName.text.toString()
        isSubscribed = !isSubscribed
        val data = JsonObject()
        data.addProperty("name", userName)
        data.addProperty("isSubscribed", isSubscribed)
        viewModel.subscribeUser(data)
    }

    private fun loadPreferences() {
        viewModel.getUser().let {
            userName = it.get("name").asString
            isSubscribed = it.get("isSubscribed").asBoolean
        }

        with(binding) {
            tvSubscription.text =
                if (isSubscribed) getString(R.string.subscription_active)
                else getString(R.string.subscription_inactive)

            etName.apply {
                setText(userName)
                isEnabled = isSubscribed == false
            }

            btnActivate.apply {
                if (isSubscribed) text = getString(R.string.deactivate)
                else text = getString(R.string.activate)
            }
        }
    }

    private fun hideKeyboard(){
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }
}