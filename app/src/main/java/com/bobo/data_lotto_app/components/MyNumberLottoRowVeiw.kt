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
fun MyNumberLottoRowView(
    oneBall: Int,
    twoBall: Int,
    threeBall: Int,
    fourBall: Int,
    fiveBall: Int,
    sixBall: Int,
    onClicked:() ->Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable {
                onClicked.invoke()
            }
            .padding(start = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(15.dp, alignment = Alignment.CenterHorizontally)
    ) {

        BallDraw(ballOder = "첫번째 볼", ballValue = oneBall.toInt())

        BallDraw(ballOder = "두번째 볼", ballValue = twoBall.toInt())

        BallDraw(ballOder = "세번째 볼", ballValue = threeBall.toInt())

        BallDraw(ballOder = "네번째 볼", ballValue = fourBall.toInt())

        BallDraw(ballOder = "다섯번째 볼", ballValue = fiveBall.toInt())

        BallDraw(ballOder = "여섯번째 볼", ballValue = sixBall.toInt())
    }


}