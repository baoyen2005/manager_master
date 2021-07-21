package com.example.filesmanager.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ads.control.Admod
import com.ads.control.funtion.AdCallback
import com.example.filesmanager.R
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd

class SplashScreenActivity : AppCompatActivity() {
    private var mInterstitialAd: InterstitialAd? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Admod.getInstance().loadSplashInterstitalAds(
            this,
            getString(R.string.id_interstitial_splash),
            12000,
            object : AdCallback() {
                override fun onAdClosed() {
                    startMain()
                }

                override fun onAdFailedToLoad(i: LoadAdError?) {
                    startMain()
                }
            }
        )

    }

    private fun startMain(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }




}