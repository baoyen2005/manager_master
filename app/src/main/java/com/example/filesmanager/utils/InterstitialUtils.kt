package com.example.filesmanager.utils

import android.content.Context
import com.ads.control.Admod
import com.example.filesmanager.R
import com.google.android.gms.ads.InterstitialAd

object InterstitialUtils {
    private var mInterClickFile: InterstitialAd? = null

    fun initInterstitialStartup(context: Context) {
        if (mInterClickFile == null) {
            mInterClickFile = Admod.getInstance().getInterstitalAds(context, context.getString(R.string.id_interstitial_click_file))
        }

    }

    fun getInteeClickFIle() : InterstitialAd {
        return mInterClickFile!!
    }
}