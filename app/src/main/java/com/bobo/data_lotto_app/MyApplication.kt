package com.bobo.data_lotto_app

import android.app.Application
import androidx.activity.viewModels
import com.bobo.data_lotto_app.ViewModel.AuthViewModel
import com.bobo.data_lotto_app.billing.BillingClientLifecycle
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel


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


        // 결제 부분
        instance = this
        this.billingClientLifecycle = BillingClientLifecycle.getInstance(this)

    }




}

