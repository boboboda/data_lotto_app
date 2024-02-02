package com.bobo.data_lotto_app.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bobo.data_lotto_app.R
import com.bobo.data_lotto_app.extentions.toWon
import com.bobo.data_lotto_app.ui.theme.MainFirstBackgroundColor
import com.bobo.data_lotto_app.ui.theme.MainMenuBarColor

@Composable
fun LottoRowView(
    oneBall: Long,
    twoBall: Long,
    threeBall: Long,
    fourBall: Long,
    fiveBall: Long,
    sixBall: Long,
    bonusBall: Long,
    firstAccount: Long,
    firstWinamnt: Long,
    firstPrzwnerCo: Long,
    date: String,
    index: Int
) {

    var isVisibleLowViewState by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true, block = {
        if(index == 0) {
            isVisibleLowViewState = true
        }
    })

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clickable {
                    if (!isVisibleLowViewState) isVisibleLowViewState =
                        true else isVisibleLowViewState = false
                }
                .padding(start = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            BallDraw(ballOder = "첫번째 볼", ballValue = oneBall.toInt())

            BallDraw(ballOder = "두번째 볼", ballValue = twoBall.toInt())

            BallDraw(ballOder = "세번째 볼", ballValue = threeBall.toInt())

            BallDraw(ballOder = "네번째 볼", ballValue = fourBall.toInt())

            BallDraw(ballOder = "다섯번째 볼", ballValue = fiveBall.toInt())

            BallDraw(ballOder = "여섯번째 볼", ballValue = sixBall.toInt())

            Image(
                modifier = Modifier
                    .size(20.dp)
                    .padding(bottom = 3.dp),
                painter = painterResource(id = R.drawable.plus_icon), contentDescription = ""
            )

            Spacer(modifier = Modifier.width(13.dp))

            BallDraw(ballOder = "보너스 볼", ballValue = bonusBall.toInt())
        }

        Spacer(modifier = Modifier.height(20.dp))

        AnimatedVisibility(visible = isVisibleLowViewState) {
            Column(
                Modifier
                    .clip(shape = RoundedCornerShape(10.dp))
                    .background(MainFirstBackgroundColor)
                    .fillMaxWidth(0.9f)
                    .fillMaxSize(0.9f)
                    .padding(top = 20.dp, bottom = 20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(10.dp))
                        .fillMaxWidth(0.9f)
                        .wrapContentHeight()
                        .background(MainMenuBarColor),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .padding(vertical = 10.dp),
                        text = "당첨날짜: ${date} "
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))

                Row(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(10.dp))
                        .fillMaxWidth(0.9f)
                        .wrapContentHeight()
                        .background(MainMenuBarColor),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .padding(vertical = 10.dp),
                        text = "누적금액: ${firstAccount.toWon()}원 "
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))

                Row(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(10.dp))
                        .fillMaxWidth(0.9f)
                        .wrapContentHeight()
                        .background(MainMenuBarColor),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .padding(vertical = 10.dp),
                        text = "1등 당첨금액: ${firstWinamnt.toWon()}원"
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))

                Row(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(10.dp))
                        .fillMaxWidth(0.9f)
                        .wrapContentHeight()
                        .background(MainMenuBarColor),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .padding(vertical = 10.dp),
                        text = "1등 당첨 인원: ${firstPrzwnerCo} 명"
                    )
                }



            }
        }
    }


}