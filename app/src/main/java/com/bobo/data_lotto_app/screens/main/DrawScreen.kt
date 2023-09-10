package com.bobo.data_lotto_app.screens.main

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bobo.data_lotto_app.Localdb.NormalModeNumber
import com.bobo.data_lotto_app.R
import com.bobo.data_lotto_app.ViewModel.DataViewModel
import com.bobo.data_lotto_app.components.FilterDialog
import com.bobo.data_lotto_app.components.LottoSelectCard
import com.bobo.data_lotto_app.ui.theme.DeleteColor
import com.bobo.data_lotto_app.ui.theme.FilterIconColor
import com.bobo.data_lotto_app.ui.theme.NormalModeLottoNumberBackgroundColor
import kotlinx.coroutines.launch

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

    val showOpenDialog = remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Row() {
            Spacer(modifier = Modifier.height(30.dp))
        }

        Column(modifier =  Modifier,
            horizontalAlignment = Alignment.CenterHorizontally) {

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
                        Icon(
                            modifier = Modifier.size(70.dp),
                            painter = painterResource(id = R.drawable.filter_icon),
                            contentDescription = "",
                            tint = FilterIconColor
                        )
                    }

                    FloatingActionButton(
                        onClick = {
                            scope.launch {
                                dataViewModel.normalLottery()
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

        Spacer(modifier = Modifier.width(15.dp))


        Spacer(modifier = Modifier.width(20.dp))

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

    }

}


@Composable
fun BigDataModeView(dataViewModel: DataViewModel) {

}