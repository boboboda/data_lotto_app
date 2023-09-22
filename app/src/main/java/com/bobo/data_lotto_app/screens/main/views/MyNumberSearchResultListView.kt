package com.bobo.data_lotto_app.screens.main.views

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bobo.data_lotto_app.MainActivity
import com.bobo.data_lotto_app.ViewModel.ChunkNumber
import com.bobo.data_lotto_app.ViewModel.DataViewModel
import com.bobo.data_lotto_app.components.MyNumberStickBar
import com.bobo.data_lotto_app.components.fontFamily


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyNumberViewPager(dataViewModel: DataViewModel) {

    val twoChunkNumber = dataViewModel.twoChunkNumberFlow.collectAsState()

    val threeChunkNumber = dataViewModel.threeChunkNumberFlow.collectAsState()

    val fourChunkNumber = dataViewModel.fourChunkNumberFlow.collectAsState()

    val fiveChunkNumber = dataViewModel.fiveChunkNumberFlow.collectAsState()

    val sixChunkNumber = dataViewModel.sixChunkNumberFlow.collectAsState()



    val pagerState = rememberPagerState(pageCount = {5})

    HorizontalPager(state = pagerState) { page ->
        // Our page content
        MyNumberDataList(
            viewStateValue = page,
            twoChunkNumber = twoChunkNumber,
            threeChunkNumber = threeChunkNumber,
            fourChunkNumber = fourChunkNumber,
            fiveChunkNumber = fiveChunkNumber,
            sixChunkNumber = sixChunkNumber,
            dataViewModel = dataViewModel
        )
    }

}

@Composable
fun MyNumberDataList(
    viewStateValue: Int = 0,
    twoChunkNumber: State<List<List<Int>>>,
    threeChunkNumber: State<List<List<Int>>>,
    fourChunkNumber: State<List<List<Int>>>,
    fiveChunkNumber: State<List<List<Int>>>,
    sixChunkNumber: State<List<Int>>,
    dataViewModel: DataViewModel

) {
    val scrollState = rememberScrollState()

    when(viewStateValue) {

        0 -> {
            Column(modifier = Modifier
                .fillMaxSize()
                .background(Color.White, shape = RoundedCornerShape(8.dp))
                .verticalScroll(scrollState)) {

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp),
                    horizontalArrangement = Arrangement.Center) {

                    val textValue = if(twoChunkNumber.value.isEmpty()) {""} else {"2쌍 중복수"}

                    Text(text = textValue,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = fontFamily
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                twoChunkNumber.value.forEach {

                    Row(modifier = Modifier.fillMaxWidth()) {

                        // 정렬 지정

                        val testValue = dataViewModel.searchLotto(it)



                        MyNumberStickBar(
                            firstBall = it[0],
                            secondBall = it[1],
                            allDataCount = testValue.second, partDataCount = testValue.first)
                    }

                }
            }

        }

        1 -> {
            Column(modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(scrollState)) {

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp),
                    horizontalArrangement = Arrangement.Center) {
                    Text(text = "3쌍 중복수",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = fontFamily
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                threeChunkNumber.value.forEach {

                    Row(modifier = Modifier.fillMaxWidth()) {

                        val testValue = dataViewModel.searchLotto(it)

                        MyNumberStickBar(
                            firstBall = it[0],
                            secondBall = it[1],
                            thirdBall = it[2],
                            allDataCount = testValue.second, partDataCount = testValue.first)
                    }

                }
            }

        }

        2 -> {
            Column(modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(scrollState)) {

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp),
                    horizontalArrangement = Arrangement.Center) {
                    Text(text = "4쌍 중복수",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = fontFamily
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                fourChunkNumber.value.forEach {

                    Row(modifier = Modifier.fillMaxWidth()) {

                        val testValue = dataViewModel.searchLotto(it)

                        MyNumberStickBar(
                            firstBall = it[0],
                            secondBall = it[1],
                            thirdBall = it[2],
                            fourthBall = it[3],
                            allDataCount = testValue.second, partDataCount = testValue.first)
                    }

                }
            }

        }

        3 -> {
            Column(modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(scrollState)) {

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp),
                    horizontalArrangement = Arrangement.Center) {
                    Text(text = "5쌍 중복수",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = fontFamily
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                fiveChunkNumber.value.forEach {

                    Row(modifier = Modifier.fillMaxWidth()) {

                        val testValue = dataViewModel.searchLotto(it)

                        MyNumberStickBar(
                            firstBall = it[0],
                            secondBall = it[1],
                            thirdBall = it[2],
                            fourthBall = it[3],
                            fifthBall = it[4],
                            allDataCount = testValue.second, partDataCount = testValue.first)
                    }

                }
            }

        }

        4 -> {
            Column(modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(scrollState)) {

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp),
                    horizontalArrangement = Arrangement.Center) {
                    Text(text = "1등 당첨",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = fontFamily
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))


                    Row(modifier = Modifier.fillMaxWidth()) {

                        val testValue = dataViewModel.searchLotto(sixChunkNumber.value)

                        MyNumberStickBar(
                            firstBall = sixChunkNumber.value[0],
                            secondBall = sixChunkNumber.value[1],
                            thirdBall = sixChunkNumber.value[2],
                            fourthBall = sixChunkNumber.value[3],
                            fifthBall = sixChunkNumber.value[4],
                            sixthBall = sixChunkNumber.value[5],
                            allDataCount = testValue.second, partDataCount = testValue.first)
                    }


            }

        }


    }
}
