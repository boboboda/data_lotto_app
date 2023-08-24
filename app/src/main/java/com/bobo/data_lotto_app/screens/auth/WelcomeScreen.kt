package com.bobo.data_lotto_app.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.bobo.data_lotto_app.Routes.AuthRoute
import com.bobo.data_lotto_app.Routes.AuthRouteAction
import com.bobo.data_lotto_app.components.BaseButton
import com.bobo.data_lotto_app.components.ButtonType
import com.bobo.data_lotto_app.components.autoSizeText
import com.bobo.data_lotto_app.components.fontFamily
import com.bobo.data_lotto_app.ui.theme.MainTextColor
import com.bobo.data_lotto_app.ui.theme.TextButtonColor
import com.bobo.data_lotto_app.ui.theme.WelcomeScreenBackgroundColor

@Composable
fun WelcomeScreen(routeAction: AuthRouteAction) {

    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset(assetName = "cat.json")
    )
    val preloaderProgress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true)

    Column(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(color = WelcomeScreenBackgroundColor)) {

                        Spacer(modifier = Modifier
                            .weight(0.1f)
                            .background(color = WelcomeScreenBackgroundColor))


            Row(modifier = Modifier
                .weight(0.2f)
                .fillMaxWidth()
                .background(color = WelcomeScreenBackgroundColor),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center) {
                autoSizeText(
                    value = "데이터 기반 로또 추첨 앱",
                    fontFamily = fontFamily,
                    fontSize = 35.sp,
                    minFontSize = 15.sp,
                    color = MainTextColor)
            }




            LottieAnimation(
                composition = composition,
                contentScale = ContentScale.FillBounds,
                progress = preloaderProgress,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 20.dp)
                    .align(Alignment.CenterHorizontally)
                    .background(color = WelcomeScreenBackgroundColor),
                alignment = Alignment.Center,
            )
        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.8f)) {

            Row(modifier = Modifier
                .weight(0.2f)
                .fillMaxWidth()
                .background(color = Color.White),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center) {
                autoSizeText(
                    value = "환영합니다 모두 부자되세요",
                    fontFamily = fontFamily,
                    fontSize = 25.sp,
                    minFontSize = 15.sp,
                    color = MainTextColor)
            }
            


            BaseButton(
                title = "로그인",
                onClick = {
                          routeAction.navTo(AuthRoute.LOGIN)
                },
                modifier = Modifier.padding(horizontal = 20.dp))

            Spacer(modifier = Modifier.height(30.dp))

            BaseButton(
                title = "회원가입",
                type = ButtonType.OUTLINE,
                onClick = {

                },
                modifier = Modifier.padding(horizontal = 20.dp))

            Spacer(modifier = Modifier.height(20.dp))

            Row(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
                horizontalArrangement = Arrangement.Center
            ) {
                TextButton(onClick = { }) {
                    Text(text = "게스트로 입장", color = TextButtonColor)
                }
            }

                        Spacer(modifier = Modifier.weight(0.1f))


        }

    }
}