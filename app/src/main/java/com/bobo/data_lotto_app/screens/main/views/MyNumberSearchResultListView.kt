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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
    val twoChunkNumberAndPercent = dataViewModel.twoChunkNumberAndPercentFlow.collectAsState()

    val threeChunkNumberAndPercent = dataViewModel.threeChunkNumberPercentFlow.collectAsState()

    val fourChunkNumberAndPercent = dataViewModel.fourChunkNumberPercentFlow.collectAsState()

    val fiveChunkNumberAndPercent = dataViewModel.fiveChunkNumberPercentFlow.collectAsState()

    val sixNumberAndPercent = dataViewModel.sixChunkNumberPercentFlow.collectAsState()

    when(viewStateValue) {

        0 -> {
            Column(modifier = Modifier
                .fillMaxSize()
                .background(
                    Color.White, shape = RoundedCornerShape(8.dp)
                )) {

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

                LazyColumn(modifier = Modifier.fillMaxWidth()) {

                    // 정렬 지정

                    items(twoChunkNumberAndPercent.value) { twoChunkNumberAndPercent->

                        MyNumberStickBar(
                            firstBall = twoChunkNumberAndPercent[0],
                            secondBall = twoChunkNumberAndPercent[1],
                            allDataCount = twoChunkNumberAndPercent[2], partDataCount = twoChunkNumberAndPercent[3])
                    }
                }
            }

        }

        1 -> {
            Column(modifier = Modifier
                .fillMaxSize()
                .background(
                    Color.White, shape = RoundedCornerShape(8.dp)
                )) {

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp),
                    horizontalArrangement = Arrangement.Center) {

                    val textValue = if(threeChunkNumber.value.isEmpty()) {""} else {"3쌍 중복수"}

                    Text(text = textValue,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = fontFamily
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                LazyColumn(modifier = Modifier.fillMaxWidth()) {

                    // 정렬 지정

                    items(threeChunkNumberAndPercent.value) { threeChunkNumberAndPercent->

                        MyNumberStickBar(
                            firstBall = threeChunkNumberAndPercent[0],
                            secondBall = threeChunkNumberAndPercent[1],
                            thirdBall = threeChunkNumberAndPercent[2],
                            allDataCount = threeChunkNumberAndPercent[3], partDataCount = threeChunkNumberAndPercent[4])
                    }
                }
            }

        }

        2 -> {
            Column(modifier = Modifier
                .fillMaxSize()
                .background(
                    Color.White, shape = RoundedCornerShape(8.dp)
                )) {

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp),
                    horizontalArrangement = Arrangement.Center) {

                    val textValue = if(fourChunkNumber.value.isEmpty()) {""} else {"4쌍 중복수"}

                    Text(text = textValue,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = fontFamily
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                LazyColumn(modifier = Modifier.fillMaxWidth()) {

                    // 정렬 지정

                    items(fourChunkNumberAndPercent.value) { fourChunkNumberAndPercent->

                        MyNumberStickBar(
                            firstBall = fourChunkNumberAndPercent[0],
                            secondBall = fourChunkNumberAndPercent[1],
                            thirdBall = fourChunkNumberAndPercent[2],
                            fourthBall = fourChunkNumberAndPercent[3],
                            allDataCount = fourChunkNumberAndPercent[4], partDataCount = fourChunkNumberAndPercent[5])
                    }
                }
            }

        }

        3 -> {
            Column(modifier = Modifier
                .fillMaxSize()
                .background(
                    Color.White, shape = RoundedCornerShape(8.dp)
                )) {

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp),
                    horizontalArrangement = Arrangement.Center) {

                    val textValue = if(fiveChunkNumber.value.isEmpty()) {""} else {"5쌍 중복수"}

                    Text(text = textValue,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = fontFamily
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                LazyColumn(modifier = Modifier.fillMaxWidth()) {

                    // 정렬 지정

                    items(fiveChunkNumberAndPercent.value) { fiveChunkNumberAndPercent->

                        MyNumberStickBar(
                            firstBall = fiveChunkNumberAndPercent[0],
                            secondBall = fiveChunkNumberAndPercent[1],
                            thirdBall = fiveChunkNumberAndPercent[2],
                            fourthBall = fiveChunkNumberAndPercent[3],
                            fifthBall = fiveChunkNumberAndPercent[4],
                            allDataCount = fiveChunkNumberAndPercent[5], partDataCount = fiveChunkNumberAndPercent[6])
                    }
                }
            }

        }

        4 -> {
            Column(modifier = Modifier
                .fillMaxSize()
                .background(
                    Color.White, shape = RoundedCornerShape(8.dp)
                )) {

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp),
                    horizontalArrangement = Arrangement.Center) {

                    val textValue = if(sixChunkNumber.value.isEmpty()) {""} else {"1등 당첨"}

                    Text(text = textValue,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = fontFamily
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                LazyColumn(modifier = Modifier.fillMaxWidth()) {

                    // 정렬 지정

                    item() { sixNumberAndPercent.value

                        MyNumberStickBar(
                            firstBall = sixNumberAndPercent.value[0],
                            secondBall = sixNumberAndPercent.value[1],
                            thirdBall = sixNumberAndPercent.value[2],
                            fourthBall = sixNumberAndPercent.value[3],
                            fifthBall = sixNumberAndPercent.value[4],
                            sixthBall = sixNumberAndPercent.value[5],
                            allDataCount = sixNumberAndPercent.value[6], partDataCount = sixNumberAndPercent.value[7])
                    }
                }
            }

        }


    }
}
