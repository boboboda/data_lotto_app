package com.bobo.data_lotto_app.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bobo.data_lotto_app.R

@Composable
fun BallDraw(
    ballOder: String,
    ballValue: Int,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .size(45.dp),
        contentAlignment = Alignment.Center
    ) {

        val ballImage =

            when (ballValue) {

                in 0..9 -> {
                    painterResource(id = R.drawable.yellow_ball)
                }

                in 10..19 -> {
                    painterResource(id = R.drawable.blue_ball)
                }

                in 20..29 -> {
                    painterResource(id = R.drawable.red_ball)
                }

                in 30..39 -> {
                    painterResource(id = R.drawable.gray_ball)
                }

                in 40..45 -> {
                    painterResource(id = R.drawable.green_ball)
                }

                else -> {
                    null
                }
            }


        Image(
            modifier = Modifier.size(40.dp),
            painter = ballImage!!,
            contentDescription = "image description",
//                    contentScale = ContentScale.None
        )

        Text(
            modifier = Modifier.padding(bottom = 5.dp, end = 6.dp),
            text = ballValue.toString(),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }

}