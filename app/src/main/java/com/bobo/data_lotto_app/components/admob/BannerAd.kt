package com.bobo.data_lotto_app.components.admob

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.bobo.data_lotto_app.BuildConfig
import com.bobo.data_lotto_app.R
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun BannerAd() {

    Box(
        modifier = Modifier
            .wrapContentSize()
            .padding(start = 10.dp, end = 10.dp)
    ) {

        val adRequest = AdRequest.Builder().build()
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = {context ->
                AdView(context).apply {
                    setAdSize(AdSize.BANNER)
                    adUnitId = BuildConfig.BANNER_AD_KEY
                    loadAd(adRequest)
                }

            },
            update = {adView ->
                adView.loadAd(adRequest)
            }


        )

    }
}