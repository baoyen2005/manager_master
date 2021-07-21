package com.example.filesmanager

import com.ads.control.AdsApplication

class Application : AdsApplication() {
    override fun onCreate() {
        super.onCreate()
    }

    override fun enableAdsResume(): Boolean {
        return false
    }

    override fun getListTestDeviceId(): MutableList<String> {
        return mutableListOf("BC37446E142AA35247DF986F19DCA744")
    }

    override fun getOpenAppAdId(): String {
        return getString(R.string.id_open_app)
    }
}