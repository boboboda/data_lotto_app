@file:OptIn(ExperimentalMaterialApi::class)

package com.bobo.data_lotto_app.screens.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.OutlinedCard
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import com.bobo.data_lotto_app.components.SelectCard
import com.bobo.data_lotto_app.components.StickGageBar
import com.bobo.data_lotto_app.R
import com.bobo.data_lotto_app.ViewModel.DataViewModel
import com.bobo.data_lotto_app.components.autoSizeText
import com.bobo.data_lotto_app.components.rangeDateDialog
import com.bobo.data_lotto_app.extentions.toPer
import com.bobo.data_lotto_app.screens.main.BallDraw
import com.bobo.data_lotto_app.ui.theme.DbContentColor
import java.text.SimpleDateFormat
import java.util.Calendar


@Composable
fun DataScreen(dataViewModel: DataViewModel) {

    val scrollState = rememberScrollState()

    val lottoNumber = (1..45).toList()

    val resentLottoNumber = dataViewModel.resentLottoNumber.collectAsState()

    val selectedRangeLotto = dataViewModel.selectRangeLottoNumber.collectAsState()
    
    val showOpenDialog = remember { mutableStateOf(false) }

    val currentId = dataViewModel.dataCardId.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.LightGray),
    horizontalAlignment = Alignment.CenterHorizontally) {

        Spacer(
            modifier = Modifier
                .height(20.dp)
        )

        Row(
            modifier = Modifier
                .weight(0.1f)
        ) {
            SelectCard(dataViewModel = dataViewModel)
        }

        when (currentId.value) {
            1 -> {

                Column(modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally) {

                    Spacer(modifier = Modifier
                        .height(20.dp))

                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.3f)
                        .padding(horizontal = 15.dp)
                        .clip(shape = RoundedCornerShape(10.dp))

                    ) {
                        Column(modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                            .background(Color.LightGray),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            dataRangeView(dataViewModel = dataViewModel)

                        }

                        Column(modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .background(Color.LightGray),
                            verticalArrangement = Arrangement.Center) {


                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .padding(horizontal = 10.dp)
                                    .clip(shape = RoundedCornerShape(5.dp))
                                    .background(color = DbContentColor),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                autoSizeText(
                                    value = "조회 로또 번호: ${selectedRangeLotto.value.count()}개",
                                    fontSize = 15.sp,
                                    minFontSize = 13.sp,
                                    modifier = Modifier
                                        .padding(5.dp),
                                    color = Color.Black,
                                    maxLines = 1,
                                )
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .padding(horizontal = 5.dp)
                                    .clip(shape = RoundedCornerShape(5.dp))
                                    .background(color = DbContentColor),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {

                                autoSizeText(
                                    value = "최근 로또 회차: ${resentLottoNumber.value.drwNo}회",
                                    fontSize = 15.sp,
                                    minFontSize = 13.sp,
                                    color = Color.Black,
                                    modifier = Modifier
                                        .padding(5.dp),
                                    maxLines = 1
                                )
                            }


                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))


                    Row(modifier = Modifier
                        .fillMaxWidth()) {
                        Text(
                            modifier = Modifier.padding(start = 10.dp, bottom = 10.dp),
                            text = "데이터 리스트",
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Column(modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .weight(1f)
                        .background(Color.White, shape = RoundedCornerShape(5.dp))
                        .verticalScroll(scrollState),
                    ) {

                        Spacer(modifier = Modifier.height(10.dp))

                        lottoNumber.forEach { number ->
                            val dataValue = dataViewModel.calculate(number.toString())
                            StickBar(ballNumber = number, data = dataViewModel.calculate(number.toString()))
                        }
                    }

                    Spacer(modifier = Modifier.height(5.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, bottom = 10.dp, end = 20.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        
                        FloatingActionButton(
                            onClick = {
                                showOpenDialog.value = true
                            },
                            modifier = Modifier.padding(8.dp)) {
                            Image(painter = painterResource(id = R.drawable.outline_calendar_icon), contentDescription = "")
                        }

                        if(showOpenDialog.value) {
                            rangeDateDialog(
                                onDismissRequest = {
                                    showOpenDialog.value = it
                                },
                                dataViewModel
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(5.dp))
                        
                        FloatingActionButton(
                            onClick = {
                                dataViewModel.filterRange()
                            },
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Image(painter = painterResource(id = R.drawable.search_icon), contentDescription = "")
                        }
                    }
                }
            }

            2 -> {
                
                Column(modifier = Modifier.weight(1f)) {

                }
                
            }
        }


    }
        
}






@Composable
fun StickBar(
    ballNumber: Int,
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

        autoSizeText(
            value = "${data.toPer()} %",
            fontSize = 18.sp,
            minFontSize = 13.sp,
            color = Color.Black,
            modifier = Modifier
                .weight(0.4f)
                .padding(end = 20.dp),
            maxLines = 1)
    }
}




@Composable
fun dataRangeView(
                 dataViewModel: DataViewModel
) {
    val time = Calendar.getInstance().time

    val formatter = SimpleDateFormat("yyyy-MM-dd")

    val today = formatter.format(time)

    val callStartDate = dataViewModel.startDateFlow.collectAsState()

    var callEndDate = dataViewModel.endDateFlow.collectAsState()

    var firstDate = if(today == "") "$today" else {callStartDate.value}

    var secondDate = if(today == "") "$today" else {callEndDate.value}



    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            border = BorderStroke(1.dp, color = Color.Black),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(all = 10.dp)
                    .padding(horizontal = 5.dp),
                contentAlignment = Alignment.Center
            ) {

                autoSizeText(
                    value = "시작: $firstDate",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier,
                    maxLines = 1,
                    minFontSize = 13.sp,
                    color = Color.Black)
            }
        }
        
        Spacer(modifier = Modifier.height(15.dp))

        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            border = BorderStroke(1.dp, color = Color.Black)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(all = 10.dp)
                    .padding(horizontal = 5.dp)
                , contentAlignment = Alignment.Center
            ) {
                autoSizeText(
                    value = "종료: $secondDate",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier,
                    maxLines = 1,
                    minFontSize = 13.sp,
                    color = Color.Black)
            }
        }
    }
 }

