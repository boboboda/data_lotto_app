package com.bobo.data_lotto_app.screens.main

import android.text.AutoText
import android.util.Log
import android.widget.ScrollView
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bobo.data_lotto_app.Localdb.BigDataModeNumber
import com.bobo.data_lotto_app.Localdb.NormalModeNumber
import com.bobo.data_lotto_app.MainActivity.Companion.TAG
import com.bobo.data_lotto_app.R
import com.bobo.data_lotto_app.ViewModel.BigDataDate
import com.bobo.data_lotto_app.ViewModel.DataViewModel
import com.bobo.data_lotto_app.components.AutoSizeText
import com.bobo.data_lotto_app.components.BigDataLottoAnimationDialog
import com.bobo.data_lotto_app.components.FilterDialog
import com.bobo.data_lotto_app.components.LottoAnimationDialog
import com.bobo.data_lotto_app.components.LottoSelectCard
import com.bobo.data_lotto_app.components.ProportionSelectDialog
import com.bobo.data_lotto_app.components.rangeDateDialog
import com.bobo.data_lotto_app.ui.theme.DeleteColor
import com.bobo.data_lotto_app.ui.theme.NormalModeLottoNumberBackgroundColor
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DrawScreen(dataViewModel: DataViewModel) {

    val currentId = dataViewModel.lottoCardId.collectAsState()

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
            LottoSelectCard(dataViewModel = dataViewModel)
        }

        Column(
            modifier = Modifier
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally) {
            when (currentId.value) {
                1 -> {
                    NormalModeView(dataViewModel = dataViewModel)

                }

                2 -> {
                    BigDataModeView(dataViewModel = dataViewModel)
                }
            }
        }

    }

}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NormalModeView(dataViewModel: DataViewModel) {

    val lottoNumberData = dataViewModel.normalLottoNumberList.collectAsState()

    val scope = rememberCoroutineScope()

    var lazyListState = rememberLazyListState()

    val addNumber = dataViewModel.normalFixNumber.collectAsState()

    val removeNumber = dataViewModel.viewRemoveNumber.collectAsState()

    val showOpenDialog = remember { mutableStateOf(false) }

    val showLottoAnimationDialog = remember{ mutableStateOf( false ) }


    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(5.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Text(
                    modifier = Modifier
                        .padding(start = 20.dp),
                    text = "추첨번호",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize()
                    .padding(horizontal = 13.dp),
                columns = GridCells.Fixed(4), content = {
                items(addNumber.value) {addNumber ->
                    FilterCard(number = " 고정수 $addNumber",
                        deleteClicked = {
                            scope.launch {
                            val addList = dataViewModel.normalFixNumber.value.toMutableList().apply {
                                remove(addNumber)
                            }
                                dataViewModel.normalFixNumber.emit(addList)

                            val removeList = dataViewModel.normalRemoveNumber.value.toMutableList().apply {
                                remove(addNumber)
                            }
                                dataViewModel.normalRemoveNumber.emit(removeList)
                            }
                        })
                }

                items(removeNumber.value) { removeNumber ->

                    FilterCard(number = " 제외수 $removeNumber",
                        deleteClicked = {
                            scope.launch {
                                val removeList = dataViewModel.normalRemoveNumber.value.toMutableList().apply {
                                    remove(removeNumber)
                                }
                                dataViewModel.normalRemoveNumber.emit(removeList)

                                val viewRemoveNumber = dataViewModel.viewRemoveNumber.value.toMutableList().apply {
                                    remove(removeNumber)
                                }

                                dataViewModel.viewRemoveNumber.emit(viewRemoveNumber)
                            }
                        })
                }
            })
        
        Spacer(modifier = Modifier.height(5.dp))

            LazyColumn(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(10.dp))
                    .fillMaxWidth(0.9f)
                    .background(color = Color.White)
                    .weight(1f)
                    ,
                state = lazyListState
            ) {

                items(lottoNumberData.value, {item -> item.id}) {item ->


                    val dismissState = rememberDismissState()

                    if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                        dataViewModel.deleteNormalNumber(item)

                    }
                    SwipeToDismiss(
                        state = dismissState,
                        modifier = Modifier
                            .padding(vertical = Dp(1f)),
                        directions = setOf(
                            DismissDirection.EndToStart),
                        dismissThresholds = { FractionalThreshold(0.25f) },
                        background = {
                            val color by animateColorAsState(
                                when (dismissState.targetValue) {
                                    DismissValue.Default -> Color.White
                                    else -> DeleteColor
                                }
                            )
                            val alignment = Alignment.CenterEnd
                            val icon = Icons.Default.Delete

                            val scale by animateFloatAsState(
                                if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
                            )

                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .background(color)
                                    .padding(horizontal = Dp(20f)),
                                contentAlignment = alignment
                            ) {
                                Image(
                                    icon,
                                    contentDescription = "Delete Icon",
                                    modifier = Modifier.scale(scale)
                                )
                            }
                        },
                        dismissContent = {


                            Card(
                                elevation = animateDpAsState(
                                    if (dismissState.dismissDirection != null) 4.dp else 0.dp).value,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(Dp(50f))
                                    .padding(0.dp)
                            ) {
                                LottoNumberArrayView(data = item)
                            }
                        }
                    )
                }
            }


            Box(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, bottom = 10.dp, end = 20.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    FloatingActionButton(
                        onClick = {
                            scope.launch {
                                showOpenDialog.value = true
                            }

                        },
                        modifier = Modifier
                            .size(80.dp)
                            .padding(8.dp)
                    ) {
                        Image(
                            modifier = Modifier.size(70.dp),
                            painter = painterResource(id = R.drawable.filter_icon),
                            contentDescription = ""
                        )
                    }

                    FloatingActionButton(
                        onClick = {
                            scope.launch {
                                showLottoAnimationDialog.value = true
                            }

                        },
                        modifier = Modifier
                            .size(80.dp)
                            .padding(8.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.lottey_icon),
                            contentScale = ContentScale.Fit,
                            contentDescription = ""
                        )
                    }
                }
            }

            if(showOpenDialog.value) {
                FilterDialog(
                    dataViewModel = dataViewModel,
                    onDismissRequest = {
                    showOpenDialog.value = it
                })
            }

        if(showLottoAnimationDialog.value) {
            LottoAnimationDialog(
                closeClicked = {
                              showLottoAnimationDialog.value = it
                },
                dataViewModel = dataViewModel,
                onDismissRequest = {
                    showLottoAnimationDialog.value = it
                }
            )
        }
    }
}

@Composable
fun LottoNumberArrayView(data: NormalModeNumber) {

    Row(modifier = Modifier
        .fillMaxSize()
        .background(color = NormalModeLottoNumberBackgroundColor, shape = RoundedCornerShape(5.dp)),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically) {

        Spacer(modifier = Modifier.weight(1f))

        BallDraw(ballOder = "", ballValue = data.firstNumber!!)
        Spacer(modifier = Modifier.width(8.dp))
        BallDraw(ballOder = "", ballValue = data.secondNumber!!)
        Spacer(modifier = Modifier.width(8.dp))
        BallDraw(ballOder = "", ballValue = data.thirdNumber!!)
        Spacer(modifier = Modifier.width(8.dp))
        BallDraw(ballOder = "", ballValue = data.fourthNumber!!)
        Spacer(modifier = Modifier.width(8.dp))
        BallDraw(ballOder = "", ballValue = data.fifthNumber!!)
        Spacer(modifier = Modifier.width(8.dp))
        BallDraw(ballOder = "", ballValue = data.sixthNumber!!)

        Spacer(modifier = Modifier.weight(1f))

    }

}


@Composable
fun BigdataLottoNumberArrayView(data: BigDataModeNumber) {

    Row(modifier = Modifier
        .fillMaxSize()
        .background(color = NormalModeLottoNumberBackgroundColor, shape = RoundedCornerShape(5.dp)),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically) {

        Spacer(modifier = Modifier.weight(1f))

        BallDraw(ballOder = "", ballValue = data.firstNumber!!)
        Spacer(modifier = Modifier.width(8.dp))
        BallDraw(ballOder = "", ballValue = data.secondNumber!!)
        Spacer(modifier = Modifier.width(8.dp))
        BallDraw(ballOder = "", ballValue = data.thirdNumber!!)
        Spacer(modifier = Modifier.width(8.dp))
        BallDraw(ballOder = "", ballValue = data.fourthNumber!!)
        Spacer(modifier = Modifier.width(8.dp))
        BallDraw(ballOder = "", ballValue = data.fifthNumber!!)
        Spacer(modifier = Modifier.width(8.dp))
        BallDraw(ballOder = "", ballValue = data.sixthNumber!!)

        Spacer(modifier = Modifier.weight(1f))

    }

}



@Composable
fun FilterCard(number: String, deleteClicked: () -> Unit) {
    Card(
        elevation = 8.dp,
        modifier = Modifier
            .wrapContentSize()
            .height(35.dp)
            .padding(3.dp),
        backgroundColor = Color.White
    ) {
        Row(
            modifier = Modifier
                .wrapContentSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AutoSizeText(
                value = number,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                maxLines = 1,
                minFontSize = 8.sp,
                color = Color.Black
            )

            IconButton(
                modifier = Modifier
                    .size(18.dp)
                    .clip(shape = CircleShape)
                    .padding(end = 3.dp),
                onClick = deleteClicked) {
                val icon = Icons.Default.Clear
                Icon(imageVector = icon, contentDescription = "")
            }
        }


    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BigDataModeView(dataViewModel: DataViewModel) {

    val callStartDate = dataViewModel.bigDataModeStartDateFlow.collectAsState()

    var callEndDate = dataViewModel.bigDataModeEndDateFlow.collectAsState()

    val lottoNumberData = dataViewModel.bigDataLottoNumberList.collectAsState()

    val scope = rememberCoroutineScope()

    var lazyListState = rememberLazyListState()

    val addNumber = dataViewModel.bigDataFixNumber.collectAsState()

    val removeNumber = dataViewModel.bigDataViewRemoveNumber.collectAsState()

    val dateFilter = dataViewModel.bigDataDateRangeFlow.collectAsState()

    val proportionState = dataViewModel.proportionStateFlow.collectAsState()

    val showOpenDialog = remember { mutableStateOf(false) }

    val showCalendarDialog = remember { mutableStateOf(false) }

    val showLottoAnimationDialog = remember{ mutableStateOf( false ) }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(5.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 20.dp),
                text = "추첨번호",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
        }

        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize()
                .padding(horizontal = 13.dp),
            columns = GridCells.Fixed(4), content = {
                items(addNumber.value) {addNumber ->
                    FilterCard(number = " 고정수 $addNumber",
                        deleteClicked = {
                            scope.launch {
                                val addList = dataViewModel.normalFixNumber.value.toMutableList().apply {
                                    remove(addNumber)
                                }
                                dataViewModel.normalFixNumber.emit(addList)

                                val removeList = dataViewModel.normalRemoveNumber.value.toMutableList().apply {
                                    remove(addNumber)
                                }
                                dataViewModel.normalRemoveNumber.emit(removeList)
                            }
                        })
                }

                items(removeNumber.value) { removeNumber ->

                    FilterCard(number = " 제외수 $removeNumber",
                        deleteClicked = {
                            scope.launch {
                                val removeList = dataViewModel.normalRemoveNumber.value.toMutableList().apply {
                                    remove(removeNumber)
                                }
                                dataViewModel.normalRemoveNumber.emit(removeList)


                            }
                        })
                }

                items(dateFilter.value, span = {
                    GridItemSpan(2)
                }) { date ->

                    FilterCard(number = "${date.startDate}~${date.endDate}",
                        deleteClicked = {
                            scope.launch {
                                val removeList = dataViewModel.bigDataDateRangeFlow.value.toMutableList().apply {
                                    remove(date)
                                }
                                dataViewModel.bigDataDateRangeFlow.emit(removeList)


                            }
                        })
                }
            })

        Spacer(modifier = Modifier.height(5.dp))

        LazyColumn(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(10.dp))
                .fillMaxWidth(0.9f)
                .background(color = Color.White)
                .weight(1f)
            ,
            state = lazyListState
        ) {

            items(lottoNumberData.value, {item -> item.id}) {item ->


                val dismissState = rememberDismissState()

                if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                    dataViewModel.deleteNormalNumber(item, modeType = DataViewModel.LotteryType.BIGDATA)

                }
                SwipeToDismiss(
                    state = dismissState,
                    modifier = Modifier
                        .padding(vertical = Dp(1f)),
                    directions = setOf(
                        DismissDirection.EndToStart),
                    dismissThresholds = { FractionalThreshold(0.25f) },
                    background = {
                        val color by animateColorAsState(
                            when (dismissState.targetValue) {
                                DismissValue.Default -> Color.White
                                else -> DeleteColor
                            }
                        )
                        val alignment = Alignment.CenterEnd
                        val icon = Icons.Default.Delete

                        val scale by animateFloatAsState(
                            if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
                        )

                        Box(
                            Modifier
                                .fillMaxSize()
                                .background(color)
                                .padding(horizontal = Dp(20f)),
                            contentAlignment = alignment
                        ) {
                            Image(
                                icon,
                                contentDescription = "Delete Icon",
                                modifier = Modifier.scale(scale)
                            )
                        }
                    },
                    dismissContent = {


                        Card(
                            elevation = animateDpAsState(
                                if (dismissState.dismissDirection != null) 4.dp else 0.dp).value,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(Dp(50f))
                                .padding(0.dp)
                        ) {
                            BigdataLottoNumberArrayView(data = item)
                        }
                    }
                )
            }
        }

        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, bottom = 10.dp, end = 20.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {

                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            showCalendarDialog.value = true
                        }
                    },
                    modifier = Modifier
                        .size(80.dp)
                        .padding(8.dp)
                ) {
                    Image(
                        modifier = Modifier.size(70.dp),
                        painter = painterResource(id = R.drawable.date_filter_icon),
                        contentDescription = ""
                    )
                }


                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            showOpenDialog.value = true
                        }

                    },
                    modifier = Modifier
                        .size(80.dp)
                        .padding(8.dp)
                ) {
                    Image(
                        modifier = Modifier.size(70.dp),
                        painter = painterResource(id = R.drawable.filter_icon),
                        contentDescription = ""
                    )
                }

                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            if(dataViewModel.bigDataModeRangeNumberStateFlow.value.isEmpty()) {

                                Log.d(TAG, "날짜 값이 없을 때 전체범위로 실행")
                                val rangePercentValue = dataViewModel.calculate(type = DataViewModel.ModeType.LOTTERY) as List<Pair<Int, Float>>

                                dataViewModel.bigDataNumberAndPercentValue.emit(rangePercentValue)
                            }
                            showLottoAnimationDialog.value = true
                        }

                    },
                    modifier = Modifier
                        .size(80.dp)
                        .padding(8.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.lottey_icon),
                        contentScale = ContentScale.Fit,
                        contentDescription = ""
                    )
                }
            }
        }

        if(showOpenDialog.value) {
            FilterDialog(
                dataViewModel = dataViewModel,
                onDismissRequest = {
                    showOpenDialog.value = it
                })
        }

        if(showLottoAnimationDialog.value) {

            ProportionSelectDialog(closeClicked = {
                scope.launch {
                    dataViewModel.bigDataNumberAndPercentValue.emit(emptyList())
                    dataViewModel.haveBigDataNumberData.emit(BigDataModeNumber())
                }
                showLottoAnimationDialog.value = it
            },
                dataViewModel = dataViewModel,
                onDismissRequest = {
                    showLottoAnimationDialog.value = it
                })
        }

        if(showCalendarDialog.value) {
            rangeDateDialog(
                onDismissRequest = {
                    showCalendarDialog.value = it
                },
                callStartDate.value,
                callEndDate.value,
                selectedStartDate = {
                    dataViewModel.bigDataModeStartDateFlow.value = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate().toString()
                },
                selectedEndDate = {
                    dataViewModel.bigDataModeEndDateFlow.value = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate().toString()
                },
                onClicked = {

                    scope.launch {

                        val dateData = BigDataDate(startDate = dataViewModel.bigDataModeStartDateFlow.value, endDate = dataViewModel.bigDataModeEndDateFlow.value )


                        val addList = dataViewModel.bigDataDateRangeFlow.value.toMutableList().apply {
                            add(dateData)
                        }

                        dataViewModel.bigDataDateRangeFlow.emit(addList)

                        val rangePercentValue = dataViewModel.calculate(type = DataViewModel.ModeType.LOTTERY) as List<Pair<Int, Float>>


                        dataViewModel.bigDataNumberAndPercentValue.emit(rangePercentValue)
                    }


                }
            )
        }
    }


}


