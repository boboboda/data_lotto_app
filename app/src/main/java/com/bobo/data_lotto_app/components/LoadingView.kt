package com.bobo.data_lotto_app.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.bobo.data_lotto_app.R
import kotlinx.coroutines.delay

@Composable
fun LoadingDialog(
) {

    val isFirstMoving = remember { mutableStateOf(false) }

    val isSecondMoving = remember { mutableStateOf(false) }

    val isThreeMoving = remember { mutableStateOf(false) }

    val isFourMoving = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        isFirstMoving.value = true
        delay(200)
        isSecondMoving.value = true
        delay(200)
        isThreeMoving.value = true
        delay(200)
        isFourMoving.value = true
    }


    val ballAnimate = animateIntAsState(
        targetValue = if(isFirstMoving.value) 80 else 0 ,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse)
    )

    val redBallAnimate = animateIntAsState(
        targetValue = if(isSecondMoving.value) 80 else 0 ,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse)
    )

    val grayBallAnimate = animateIntAsState(
        targetValue = if(isThreeMoving.value) 80 else 0 ,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse)
    )

    val greenBallAnimate = animateIntAsState(
        targetValue = if(isFourMoving.value) 80 else 0 ,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse)
    )


    Dialog(onDismissRequest = {

    }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Row(modifier = Modifier
                .wrapContentSize()
                .padding(top = 20.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.blue_ball),
                    contentDescription = "C",
                    modifier = Modifier
                        .size(50.dp)
                        .offset(
                            x = 0.dp,
                            y = ballAnimate.value.dp
                        )
                )

                Spacer(modifier = Modifier.width(5.dp))

                Image(
                    painter = painterResource(id = R.drawable.red_ball),
                    contentDescription = "C",
                    modifier = Modifier
                        .size(50.dp)
                        .offset(
                            x = 0.dp,
                            y = redBallAnimate.value.dp
                        )
                )

                Spacer(modifier = Modifier.width(5.dp))

                Image(
                    painter = painterResource(id = R.drawable.gray_ball),
                    contentDescription = "C",
                    modifier = Modifier
                        .size(50.dp)
                        .offset(
                            x = 0.dp,
                            y = grayBallAnimate.value.dp
                        )
                )

                Spacer(modifier = Modifier.width(5.dp))

                Image(
                    painter = painterResource(id = R.drawable.green_ball),
                    contentDescription = "C",
                    modifier = Modifier
                        .size(50.dp)
                        .offset(
                            x = 0.dp,
                            y = greenBallAnimate.value.dp
                        )
                )

            }
        }
    }




}