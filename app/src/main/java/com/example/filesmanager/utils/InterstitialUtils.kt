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
            mInterClickFile = Admod.getInstance().getInterstitalAds(context, context.getString(R.string.id_interstitial_click_file))
        }

    }

    fun getInteeClickFIle() : InterstitialAd {
        return mInterClickFile!!
    }
    fun loadAdsNative(context: Context, frAds: FrameLayout) {
        Admod.getInstance().loadUnifiedNativeAd(context,getString(R.string.id_native_top_list_file),object :
            AdCallback(){
            override fun onUnifiedNativeAdLoaded(unifiedNativeAd: UnifiedNativeAd?) {
                super.onUnifiedNativeAdLoaded(unifiedNativeAd)
                val adview = LayoutInflater.from(context).inflate(R.layout.custom_native,null)
                frAds!!.addView(adview)
                Admod.getInstance().populateUnifiedNativeAdView(unifiedNativeAd,
                    adview as UnifiedNativeAdView?
                )
            }
        })
    }
}