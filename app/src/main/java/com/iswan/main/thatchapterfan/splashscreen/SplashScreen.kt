package com.iswan.main.thatchapterfan.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.iswan.main.thatchapterfan.MainActivity
import com.iswan.main.thatchapterfan.R
import com.iswan.main.thatchapterfan.databinding.ActivitySplashScreenBinding

@Suppress("DEPRECATION")
class SplashScreen : AppCompatActivity() {

    private lateinit var binding : ActivitySplashScreenBinding
    private val timeout: Long = 3000

    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT < 16) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()

        val fadeInAnim = AnimationUtils.loadAnimation(this, R.anim.anim_fade_in)
        val appVersion = getString(R.string.version) + " " + getString(R.string.app_version)

        with (binding) {
            logo.animation = fadeInAnim
            tvAppVersion.text = appVersion
        }

        Handler(Looper.myLooper() ?: return).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, timeout)
    }
}