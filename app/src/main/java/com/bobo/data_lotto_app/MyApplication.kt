package com.bobo.data_lotto_app

import android.app.Application
import com.bobo.data_lotto_app.billing.BillingClientLifecycle
import com.google.android.gms.ads.MobileAds
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class MyApplication : Application() {

    companion object {
        lateinit var instance: MyApplication
            private set
    }

    lateinit var billingClientLifecycle: BillingClientLifecycle
        private set

    override fun onCreate() {

//        MobileAds.initialize(this)
        super.onCreate()
        // 다른 초기화 코드들

        // Kakao SDK 초기화
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
        instance = this
        this.billingClientLifecycle = BillingClientLifecycle.getInstance(this)
    }
}

