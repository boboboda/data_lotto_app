package com.bobo.data_lotto_app.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
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