package com.example.filesmanager.utils

import android.content.Context
import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.ads.control.Admod
import com.ads.control.funtion.AdCallback
import com.example.filesmanager.R
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView

object InterstitialUtils {
    private var mInterClickFile: InterstitialAd? = null

    fun initInterstitialStartup(context: Context) {
        if (mInterClickFile == null) {
            mInterClickFile = Admod.getInstance().getInterstitalAds(context, context.getString(R.string.id_interstitial_click_folder))
        }

    }

    fun getInteeClickFIle() : InterstitialAd {
        return mInterClickFile!!
    }
}