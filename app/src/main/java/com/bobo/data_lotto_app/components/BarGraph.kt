package com.bobo.data_lotto_app.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bobo.data_lotto_app.extentions.toPer
import com.bobo.data_lotto_app.ui.theme.AColor
import com.bobo.data_lotto_app.ui.theme.BColor
import com.bobo.data_lotto_app.ui.theme.CColor
import com.bobo.data_lotto_app.ui.theme.DColor
import com.bobo.data_lotto_app.ui.theme.EColor
import com.bobo.data_lotto_app.ui.theme.FColor
import com.bobo.data_lotto_app.ui.theme.GColor
import com.bobo.data_lotto_app.ui.theme.HColor
import com.bobo.data_lotto_app.ui.theme.IColor
import com.bobo.data_lotto_app.ui.theme.JColor

@Composable
fun ProportionBar(
    data: List<Number>,
    colors: List<Color>,
    strokeWidth: Float,
    cornerRadius: CornerRadius = CornerRadius(strokeWidth),
    modifier: Modifier
) {
    val sumOfData = data.map { it.toFloat() }.sum()
    Canvas(
        modifier = modifier
    ) {
        //canvas size 폭의 5%, 95% 지점을 시작점과 끝점으로 했습니다.
        val lineStart = size.width * 0.02f
        val lineEnd = size.width * 0.98f
        //차트 길이
        val lineLength = (lineEnd - lineStart)
        //(canvas높이 - 차트 높이) * 0.5 를 하면 차트를 그릴 위쪽 오프셋을 구할 수 있습니다.
        val lineHeightOffset = (size.height - strokeWidth) * 0.5f
        val path = Path().apply {
            addRoundRect(
                RoundRect(
                    Rect(
                        offset = Offset(lineStart, lineHeightOffset),
                        size = Size(lineLength, strokeWidth)
                    ),
                    cornerRadius = CornerRadius(20f)
                )
            )
        }
        val dataAndColor = data.zip(colors)
        clipPath(
            path
        ) {
            var dataStart = lineStart
            dataAndColor.forEach { (number, color) ->
                //끝점은 시작점 + (변량의 비율 * 전체 길이)
                val dataEnd =
                    dataStart + ((number.toFloat() / sumOfData) * lineLength)
                drawRect(
                    color = color,
                    topLeft = Offset(dataStart, lineHeightOffset),
                    size = Size(dataEnd - dataStart, strokeWidth)
                )
                //다음 사각형의 시작점은 현재의 끝점
                dataStart = dataEnd
            }
        }

    }
}

@Composable
fun StickGageBar(percent: Float = 0f,
                 modifier: Modifier) {


    val a = if(percent >= 1) 1 else 0
    val b = if(percent >= 10) 1 else 0
    val c = if(percent >= 20) 1 else 0
    val d = if(percent >= 30) 1 else 0
    val e = if(percent >= 40) 1 else 0
    val f = if(percent >= 50) 1 else 0
    val g = if(percent >= 60) 1 else 0
    val h = if(percent >= 70) 1 else 0
    val i = if(percent >= 80) 1 else 0
    val j = if(percent >= 90) 1 else 0

    val calculateGage = (10 - (a + b + c + d + e + f + g + h + i + j))




    ProportionBar(
        data = listOf(a,b,c,d,e,f,g,h,i,j, calculateGage),
        colors = listOf(
            AColor,
            BColor,
            CColor,
            DColor,
            EColor,
            FColor,
            GColor,
            HColor,
            IColor,
            JColor,
            Color.Gray
        ),
        strokeWidth = 100f,
        modifier = modifier
    )
}

@Composable
fun StickBar(
    ballNumber: Int,
    count: Int,
    data: Float) {

    Row(modifier = Modifier
        .wrapContentHeight()
        .padding(bottom = 3.dp),
        verticalAlignment = Alignment.CenterVertically) {

        Spacer(modifier = Modifier.width(10.dp))

        BallDraw(ballOder = "$ballNumber 번", ballValue = ballNumber)

        StickGageBar(percent = data,
            modifier = Modifier
                .height(50.dp)
                .weight(1f)
                .padding(end = 3.dp))

        Column(
            modifier = Modifier
            .weight(0.4f)
            .padding(end = 20.dp),
            horizontalAlignment = Alignment.End) {
            AutoSizeText(
                value = "${data.toPer()} %",
                fontSize = 16.sp,
                minFontSize = 13.sp,
                color = Color.Black,
                modifier = Modifier,
                maxLines = 1,
                textAlign = TextAlign.Start)

            AutoSizeText(
                value = "(${count}회)",
                fontSize = 16.sp,
                minFontSize = 13.sp,
                color = Color.Black,
                modifier = Modifier,
                maxLines = 1,
                textAlign = TextAlign.Start)
        }


    }
}


@Composable
fun MyNumberStickBar(
    firstBall: Int? = null,
    secondBall: Int? = null,
    thirdBall: Int? = null,
    fourthBall: Int? = null,
    fifthBall: Int? = null,
    sixthBall: Int? = null,
    allDataCount: Int,
    partDataCount: Int) {

    var data = (partDataCount.toFloat()/allDataCount.toFloat()) * 100

    Column {

        Row(modifier = Modifier
            .wrapContentHeight()
            .padding(bottom = 3.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(10.dp))

            if(firstBall != null) {
                BallDraw(ballOder = "$firstBall 번", ballValue = firstBall)
            }

            if(secondBall != null) {
                BallDraw(ballOder = "$secondBall 번", ballValue = secondBall)
            }

            if(thirdBall != null) {
                BallDraw(ballOder = "$thirdBall 번", ballValue = thirdBall)
            }

            if(fourthBall != null) {
                BallDraw(ballOder = "$fourthBall 번", ballValue = fourthBall)
            }

            if(fifthBall != null) {
                BallDraw(ballOder = "$fifthBall 번", ballValue = fifthBall)
            }

            if(sixthBall != null) {
                BallDraw(ballOder = "$sixthBall 번", ballValue = sixthBall)
            }
        }

        Row(modifier = Modifier
            .wrapContentHeight()
            .padding(start = 10.dp, bottom = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start) {
            Text(text = "${allDataCount} 중 ${partDataCount} 일치", fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }

        Row(modifier = Modifier
            .wrapContentHeight()
            .padding(bottom = 3.dp),
            verticalAlignment = Alignment.CenterVertically) {


            StickGageBar(percent = data,
                modifier = Modifier
                    .height(50.dp)
                    .weight(1f)
                    .padding(end = 3.dp))

            AutoSizeText(
                value = "${data.toPer()} %",
                fontSize = 18.sp,
                minFontSize = 13.sp,
                color = Color.Black,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(end = 20.dp),
                maxLines = 1)
        }
    }


}