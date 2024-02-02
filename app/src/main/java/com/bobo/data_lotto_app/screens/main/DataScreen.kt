@file:OptIn(ExperimentalMaterialApi::class)

package com.bobo.data_lotto_app.screens.main

import android.app.Activity
import android.content.Context
import android.provider.ContactsContract.CommonDataKinds.Im
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.TextField
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import com.bobo.data_lotto_app.MainActivity.Companion.TAG
import com.bobo.data_lotto_app.components.StickGageBar
import com.bobo.data_lotto_app.R
import com.bobo.data_lotto_app.ViewModel.DataViewModel
import com.bobo.data_lotto_app.components.LottoNumberTextField
import com.bobo.data_lotto_app.components.StickBar
import com.bobo.data_lotto_app.extentions.toPer
import com.bobo.data_lotto_app.ui.theme.DateBackgroundColor
import com.bobo.data_lotto_app.ui.theme.DbContentColor
import com.bobo.data_lotto_app.ui.theme.WelcomeScreenBackgroundColor
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.Calendar
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.ThresholdConfig
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.bobo.data_lotto_app.ViewModel.AuthViewModel
import com.bobo.data_lotto_app.ViewModel.BigDataDate
import com.bobo.data_lotto_app.components.AutoSizeText
import com.bobo.data_lotto_app.components.MyNumberLottoRowView
import com.bobo.data_lotto_app.components.RangeDateDialog
import com.bobo.data_lotto_app.components.SliderAdvanced
import com.bobo.data_lotto_app.components.TopTitleButton
import com.bobo.data_lotto_app.components.UseCountDialog
import com.bobo.data_lotto_app.components.UseType
import com.bobo.data_lotto_app.components.admob.showInterstitial
import com.bobo.data_lotto_app.extentions.toWon
import com.bobo.data_lotto_app.screens.main.views.MyNumberViewPager
import com.bobo.data_lotto_app.ui.theme.MainFirstBackgroundColor
import com.bobo.data_lotto_app.ui.theme.MainMenuBarColor
import java.time.LocalDate


// 데이터 리셋 추가, 데이터 리스트 조회하기 팝업
@Composable
fun DataScreen(dataViewModel: DataViewModel, authViewModel: AuthViewModel) {

    val currentId = dataViewModel.dataCardId.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(
            modifier = Modifier
                .height(20.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.1f)
        ) {
            TopTitleButton(dataViewModel = dataViewModel)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 3.dp)
                .padding(horizontal = 10.dp)
//                .padding(start = 5.dp)
        ) {
            SliderAdvanced() { sliderValue ->
                dataViewModel.allDataFilter(sliderValue)
            }
        }

        Column(
            modifier = Modifier
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (currentId.value) {
                1 -> {

                    BigDataSearchView(dataViewModel = dataViewModel)
                }

                2 -> {
                    MyNumberSearchView(dataViewModel = dataViewModel, authViewModel)
                }
            }
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun BigDataSearchView(dataViewModel: DataViewModel) {


    val resentLottoNumber = dataViewModel.resentLottoNumber.collectAsState()

    val selectedRangeLotto = dataViewModel.selectRangeLottoNumber.collectAsState()

    val scope = rememberCoroutineScope()

    val snackBarHostState = remember { SnackbarHostState() }

    val lazyListState = rememberLazyListState()

    val allNumberAndPercentValue = dataViewModel.allNumberAndPercentValue.collectAsState()

    val sortState = dataViewModel.allNumberSortState.collectAsState()

    val context = LocalContext.current


    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(
            modifier = Modifier
                .height(5.dp)
        )

        Column(
            Modifier
                .clip(shape = RoundedCornerShape(10.dp))
                .background(MainFirstBackgroundColor)
                .fillMaxWidth(0.9f)
                .weight(0.4f)
                .padding(top = 20.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(10.dp))
                    .fillMaxWidth(0.9f)
                    .weight(1f)
                    .background(MainMenuBarColor),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = "조회 로또 번호: ${selectedRangeLotto.value.count()}개"
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(10.dp))
                    .fillMaxWidth(0.9f)
                    .weight(1f)
                    .background(MainMenuBarColor),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = "최근 로또 회차: ${resentLottoNumber.value.drwNo}회"
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(start = 10.dp),
                text = "데이터 리스트",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(1f))

            Card(modifier = Modifier
                .padding(end = 20.dp)
                .wrapContentSize(),
                shape = RoundedCornerShape(5.dp),
                onClick = {
                    scope.launch {
                        if (sortState.value) {
                            dataViewModel.allNumberSortState.emit(false)
                            dataViewModel.allNumberSort()
                        } else {
                            dataViewModel.allNumberSortState.emit(true)
                            dataViewModel.allNumberSort()
                        }
                    }


                }) {

                Row(
                    modifier = Modifier
                        .padding(5.dp)
                        .wrapContentSize()
                ) {
                    if (sortState.value) {
                        Text(text = "내림차순")

                        Image(
                            painter = painterResource(id = R.drawable.arrow_down_icon),
                            contentDescription = ""
                        )

                    } else {
                        Text(text = "오름차순")

                        Image(
                            painter = painterResource(id = R.drawable.arrow_up_icon),
                            contentDescription = ""
                        )

                    }
                }

            }
        }

        LazyColumn(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(10.dp))
                .fillMaxWidth(0.9f)
                .background(color = Color.White)
                .weight(1f),
            state = lazyListState
        ) {
            items(allNumberAndPercentValue.value) { numberAndPercents ->
                StickBar(
                    ballNumber = numberAndPercents.first,
                    count = numberAndPercents.second,
                    data = numberAndPercents.third
                )
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
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

                            val rangePercentValue =
                                dataViewModel.calculate(type = DataViewModel.ModeType.AllNUMBERSEARCH) as List<Triple<Int, Int, Float>>

                            val sortRangePercent = rangePercentValue.sortedByDescending { it.third }

                            dataViewModel.allNumberAndPercentValue.emit(sortRangePercent)
                        }
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.search_icon),
                        contentDescription = ""
                    )
                }
            }
            SnackbarHost(hostState = snackBarHostState, modifier = Modifier,
                snackbar = { snackbarData ->

                    androidx.compose.material.Card(
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(2.dp, Color.Black),
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .padding(start = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {

                            androidx.compose.material.Text(
                                text = snackbarData.message,
                                fontSize = 15.sp,
                                lineHeight = 20.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            androidx.compose.material.Card(
                                modifier = Modifier.wrapContentSize(),
                                onClick = {
                                    snackBarHostState.currentSnackbarData?.dismiss()
                                }) {
                                androidx.compose.material.Text(
                                    modifier = Modifier.padding(8.dp),
                                    text = "닫기"
                                )
                            }
                        }
                    }
                }
            )
        }


    }
}

@OptIn(
    ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun MyNumberSearchView(dataViewModel: DataViewModel, authViewModel: AuthViewModel) {

    val oneNumber = dataViewModel.myNumberOneFlow.collectAsState()

    val twoNumber = dataViewModel.myNumberTwoFlow.collectAsState()

    val threeNumber = dataViewModel.myNumberThreeFlow.collectAsState()

    val fourNumber = dataViewModel.myNumberFourFlow.collectAsState()

    val fiveNumber = dataViewModel.myNumberFiveFlow.collectAsState()

    val sixNumber = dataViewModel.myNumberSixFlow.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    val snackBarHostState = remember { SnackbarHostState() }

    val keyboardController = LocalSoftwareKeyboardController.current

    val rangeDateState = dataViewModel.myNumberDateRangeFlow.collectAsState()


    val myNumberCount = authViewModel.myNumberSearchCountFlow.collectAsState()

    val myNumberSortState = dataViewModel.myNumberSortState.collectAsState()

    val context = LocalContext.current

    var inputFieldState by remember { mutableStateOf(false) }

    val userInPutLottoNumber = dataViewModel.userInputLottoNumberFlow.collectAsState()

    LaunchedEffect(key1 = userInPutLottoNumber.value, block = {
        if (userInPutLottoNumber.value.all { it != 0 }) {
            // 모든 요소가 빈 문자열이 아닌 경우
            inputFieldState = true
            Log.d(TAG, "유저 입력 데이터에 값이 있습니다.")
        } else {
            // 하나라도 빈 문자열인 요소가 있는 경우
            inputFieldState = false
            Log.d(TAG, "유저 입력 데이터에 값이 없습니다.")
        }
    })

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(
            modifier = Modifier
                .height(5.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(start = 10.dp),
                text = "나의 로또 번호",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )


            AnimatedVisibility(visible = inputFieldState) {
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = "다시 입력하시려면 로또번호를 클릭해주세요",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if(inputFieldState) {
            Row {
                MyNumberLottoRowView(
                    oneBall = userInPutLottoNumber.value[0].takeIf { it != null } ?: 0,
                    twoBall = userInPutLottoNumber.value[1].takeIf { it != null } ?: 0,
                    threeBall = userInPutLottoNumber.value[2].takeIf { it != null } ?: 0,
                    fourBall = userInPutLottoNumber.value[3].takeIf { it != null } ?: 0,
                    fiveBall = userInPutLottoNumber.value[4].takeIf { it != null } ?: 0,
                    sixBall = userInPutLottoNumber.value[5].takeIf { it != null } ?: 0
                ) {
                    dataViewModel.myNumberUserInPutReset()
                }
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.15f)
                    .padding(horizontal = 15.dp)
            ) {

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    Alignment.CenterHorizontally
                ) {
                    LottoNumberTextField(
                        modifier = Modifier,
                        value = oneNumber.value,
                        textFiledClicked = {
                            dataViewModel.myNumberOneFlow.value = ""
                        },
                        onValueChanged = {

                            // 스트링을 숫자로 변환합니다.
                            val number = it.toIntOrNull()

                            // 숫자로 변환할 수 없으면 빈 문자열을 반환합니다.
                            if (number == null) {
                                dataViewModel.myNumberOneFlow.value = ""
                            } else {
                                // 숫자 범위를 확인합니다.
                                if (0 < number && number <= 45) {
                                    dataViewModel.myNumberOneFlow.value = number.toString()
                                } else {
                                    dataViewModel.myNumberOneFlow.value = ""
                                    coroutineScope.launch {

                                        keyboardController?.hide()
                                        snackBarHostState.showSnackbar(
                                            "1~45 범위의 숫자만 입력해주세요.",
                                            actionLabel = "닫기", SnackbarDuration.Short
                                        )
                                    }
                                }
                            }

                        })
                }
                Spacer(modifier = Modifier.width(10.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    Alignment.CenterHorizontally
                ) {
                    LottoNumberTextField(
                        modifier = Modifier,
                        value = twoNumber.value,
                        textFiledClicked = {
                            dataViewModel.myNumberTwoFlow.value = ""
                        },
                        onValueChanged = {

                            // 스트링을 숫자로 변환합니다.
                            val number = it.toIntOrNull()

                            // 숫자로 변환할 수 없으면 빈 문자열을 반환합니다.
                            if (number == null) {
                                dataViewModel.myNumberTwoFlow.value = ""
                            } else {
                                // 숫자 범위를 확인합니다.
                                if (0 < number && number <= 45) {
                                    dataViewModel.myNumberTwoFlow.value = number.toString()
                                } else {
                                    dataViewModel.myNumberTwoFlow.value = ""
                                    coroutineScope.launch {

                                        keyboardController?.hide()
                                        snackBarHostState.showSnackbar(
                                            "1~45 범위의 숫자만 입력해주세요.",
                                            actionLabel = "닫기", SnackbarDuration.Short
                                        )
                                    }
                                }
                            }

                        })
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    Alignment.CenterHorizontally
                ) {
                    LottoNumberTextField(
                        modifier = Modifier,
                        value = threeNumber.value,
                        textFiledClicked = {
                            dataViewModel.myNumberThreeFlow.value = ""
                        },
                        onValueChanged = {

                            // 스트링을 숫자로 변환합니다.
                            val number = it.toIntOrNull()

                            // 숫자로 변환할 수 없으면 빈 문자열을 반환합니다.
                            if (number == null) {
                                dataViewModel.myNumberThreeFlow.value = ""
                            } else {
                                // 숫자 범위를 확인합니다.
                                if (0 < number && number <= 45) {
                                    dataViewModel.myNumberThreeFlow.value = number.toString()
                                } else {
                                    dataViewModel.myNumberThreeFlow.value = ""
                                    coroutineScope.launch {

                                        keyboardController?.hide()
                                        snackBarHostState.showSnackbar(
                                            "1~45 범위의 숫자만 입력해주세요.",
                                            actionLabel = "닫기", SnackbarDuration.Short
                                        )
                                    }
                                }
                            }

                        })
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    Alignment.CenterHorizontally
                ) {
                    LottoNumberTextField(
                        modifier = Modifier,
                        value = fourNumber.value,
                        textFiledClicked = {
                            dataViewModel.myNumberFourFlow.value = ""
                        },
                        onValueChanged = {

                            // 스트링을 숫자로 변환합니다.
                            val number = it.toIntOrNull()

                            // 숫자로 변환할 수 없으면 빈 문자열을 반환합니다.
                            if (number == null) {
                                dataViewModel.myNumberFourFlow.value = ""
                            } else {
                                // 숫자 범위를 확인합니다.
                                if (0 < number && number <= 45) {
                                    dataViewModel.myNumberFourFlow.value = number.toString()
                                } else {
                                    dataViewModel.myNumberFourFlow.value = ""
                                    coroutineScope.launch {

                                        keyboardController?.hide()
                                        snackBarHostState.showSnackbar(
                                            "1~45 범위의 숫자만 입력해주세요.",
                                            actionLabel = "닫기", SnackbarDuration.Short
                                        )
                                    }
                                }
                            }

                        })
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    Alignment.CenterHorizontally
                ) {
                    LottoNumberTextField(
                        modifier = Modifier,
                        value = fiveNumber.value,
                        textFiledClicked = {
                            dataViewModel.myNumberFiveFlow.value = ""
                        },
                        onValueChanged = {

                            // 스트링을 숫자로 변환합니다.
                            val number = it.toIntOrNull()

                            // 숫자로 변환할 수 없으면 빈 문자열을 반환합니다.
                            if (number == null) {
                                dataViewModel.myNumberFiveFlow.value = ""
                            } else {
                                // 숫자 범위를 확인합니다.
                                if (0 < number && number <= 45) {
                                    dataViewModel.myNumberFiveFlow.value = number.toString()
                                } else {
                                    dataViewModel.myNumberFiveFlow.value = ""
                                    coroutineScope.launch {

                                        keyboardController?.hide()
                                        snackBarHostState.showSnackbar(
                                            "1~45 범위의 숫자만 입력해주세요.",
                                            actionLabel = "닫기", SnackbarDuration.Short
                                        )
                                    }
                                }
                            }

                        })
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    Alignment.CenterHorizontally
                ) {
                    LottoNumberTextField(
                        modifier = Modifier,
                        value = sixNumber.value,
                        textFiledClicked = {
                            dataViewModel.myNumberSixFlow.value = ""
                        },
                        onValueChanged = {

                            // 스트링을 숫자로 변환합니다.
                            val number = it.toIntOrNull()

                            // 숫자로 변환할 수 없으면 빈 문자열을 반환합니다.
                            if (number == null) {
                                dataViewModel.myNumberSixFlow.value = ""
                            } else {
                                // 숫자 범위를 확인합니다.
                                if (0 < number && number <= 45) {
                                    dataViewModel.myNumberSixFlow.value = number.toString()
                                } else {
                                    dataViewModel.myNumberSixFlow.value = ""
                                    coroutineScope.launch {

                                        keyboardController?.hide()
                                        snackBarHostState.showSnackbar(
                                            "1~45 범위의 숫자만 입력해주세요.",
                                            actionLabel = "닫기", SnackbarDuration.Short
                                        )
                                    }
                                }
                            }
                        })
                }
            }
        }



        Spacer(modifier = Modifier.height(20.dp))


        // 필터 태그
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize()
                .padding(horizontal = 13.dp),
            columns = GridCells.Fixed(4), content = {
                items(rangeDateState.value, span = {
                    GridItemSpan(2)
                }) { date ->

                    FilterCard(number = "${date.startDate}~${date.endDate}",
                        deleteClicked = {
                            coroutineScope.launch {
                                val removeList =
                                    dataViewModel.myNumberDateRangeFlow.value.toMutableList()
                                        .apply {
                                            remove(date)
                                        }
                                dataViewModel.myNumberDateRangeFlow.emit(removeList)

                                dataViewModel.myNumberStartDateFlow.emit("${LocalDate.now()}")

                                dataViewModel.myNumberEndDateFlow.emit("${LocalDate.now()}")
                            }
                        })
                }
            }
        )


        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.padding(start = 10.dp, bottom = 10.dp),
                text = "내 번호 조회 결과",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(1f))

            Card(modifier = Modifier
                .padding(end = 20.dp)
                .wrapContentSize(),
                shape = RoundedCornerShape(5.dp),
                onClick = {
                    dataViewModel.myNumberSort()
                }) {

                Row(
                    modifier = Modifier
                        .padding(5.dp)
                        .wrapContentSize()
                ) {
                    if (myNumberSortState.value) {
                        Text(text = "내림차순")

                        Image(
                            painter = painterResource(id = R.drawable.arrow_down_icon),
                            contentDescription = ""
                        )

                    } else {
                        Text(text = "오름차순")

                        Image(
                            painter = painterResource(id = R.drawable.arrow_up_icon),
                            contentDescription = ""
                        )

                    }
                }

            }


        }
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .weight(1f)
                .background(Color.White, shape = RoundedCornerShape(5.dp))
        ) {

            MyNumberViewPager(dataViewModel = dataViewModel)
        }

        Spacer(modifier = Modifier.height(5.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, bottom = 10.dp, end = 20.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(5.dp))

                // 조회 버튼
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {

                            if (oneNumber.value == "" &&
                                twoNumber.value == "" &&
                                threeNumber.value == "" &&
                                fourNumber.value == "" &&
                                fiveNumber.value == "" &&
                                sixNumber.value == ""
                            ) {
                                return@launch
                            } else {

                                val chunkValue = dataViewModel.chunkMake()

                                coroutineScope.launch {
                                    dataViewModel.twoChunkNumberFlow.emit(chunkValue.secondNumber)

                                    dataViewModel.threeChunkNumberFlow.emit(chunkValue.thirdNumber)

                                    dataViewModel.fourChunkNumberFlow.emit(chunkValue.fourthNumber)

                                    dataViewModel.fiveChunkNumberFlow.emit(chunkValue.fifthNumber)

                                    dataViewModel.sixChunkNumberFlow.emit(chunkValue.sixthNumber)


                                    if (chunkValue.secondNumber != null &&
                                        chunkValue.thirdNumber != null &&
                                        chunkValue.fourthNumber != null &&
                                        chunkValue.fifthNumber != null &&
                                        chunkValue.sixthNumber != null
                                    ) {

                                        Log.d(TAG, "myNumberChunkEmit() 실행됨")
                                        dataViewModel.myNumberChunkEmit()

                                        inputFieldState = true

                                    } else {
                                        Log.d(TAG, "myNumberChunkEmit() 실행되지 않음")
                                    }
                                }

                            }


                        }
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.search_icon),
                        contentDescription = ""
                    )
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            SnackbarHost(hostState = snackBarHostState, modifier = Modifier,
                snackbar = { snackbarData ->

                    androidx.compose.material.Card(
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(2.dp, Color.Black),
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .padding(start = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {

                            androidx.compose.material.Text(
                                text = snackbarData.message,
                                fontSize = 15.sp,
                                lineHeight = 20.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            androidx.compose.material.Card(
                                modifier = Modifier.wrapContentSize(),
                                onClick = {
                                    snackBarHostState.currentSnackbarData?.dismiss()
                                }) {
                                androidx.compose.material.Text(
                                    modifier = Modifier.padding(8.dp),
                                    text = "닫기"
                                )
                            }
                        }
                    }
                }
            )
        }
    }

}



