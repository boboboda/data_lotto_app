@file:OptIn(ExperimentalMaterialApi::class)

package com.bobo.data_lotto_app.screens.main

import android.app.Activity
import android.content.Context
import android.provider.ContactsContract.CommonDataKinds.Im
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
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
import com.bobo.data_lotto_app.components.SelectCard
import com.bobo.data_lotto_app.components.StickGageBar
import com.bobo.data_lotto_app.R
import com.bobo.data_lotto_app.ViewModel.DataViewModel
import com.bobo.data_lotto_app.components.LottoNumberTextField
import com.bobo.data_lotto_app.components.StickBar
import com.bobo.data_lotto_app.components.rangeDateDialog
import com.bobo.data_lotto_app.extentions.toPer
import com.bobo.data_lotto_app.screens.main.BallDraw
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
import com.bobo.data_lotto_app.components.AutoSizeText
import com.bobo.data_lotto_app.screens.main.views.MyNumberViewPager


// 데이터 리셋 추가, 데이터 리스트 조회하기 팝업
@Composable
fun DataScreen(dataViewModel: DataViewModel) {

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

        Column(
            modifier = Modifier
            .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally) {
            when (currentId.value) {
                1 -> {
                    
                    BigDataSearchView(dataViewModel = dataViewModel)
                }

                2 -> {
                    MyNumberSearchView(dataViewModel = dataViewModel)
                }
            }
        }

    }
        
}


@Composable
fun dataRangeView(
    startDate: String,
    endDate: String
) {
    val time = Calendar.getInstance().time

    val formatter = SimpleDateFormat("yyyy-MM-dd")

    val today = formatter.format(time)

    var firstDate = if(today == "") "$today" else {startDate}

    var secondDate = if(today == "") "$today" else {endDate}



    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 5.dp)
                .clip(shape = RoundedCornerShape(5.dp))
                .background(color = DateBackgroundColor),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            AutoSizeText(
                value = "시작: ${firstDate}",
                fontSize = 15.sp,
                minFontSize = 13.sp,
                color = Color.Black,
                modifier = Modifier
                    .padding(5.dp),
                maxLines = 1
            )
        }

        
        Spacer(modifier = Modifier.height(15.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 5.dp)
                .clip(shape = RoundedCornerShape(5.dp))
                .background(color = DateBackgroundColor),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            AutoSizeText(
                value = "종료: ${secondDate}",
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

@Composable
fun dataRangeRowTypeView(
    startDate: String,
    endDate: String
) {
    val time = Calendar.getInstance().time

    val formatter = SimpleDateFormat("yyyy-MM-dd")

    val today = formatter.format(time)

    var firstDate = if(today == "") "$today" else {startDate}

    var secondDate = if(today == "") "$today" else {endDate}



    Row(
        modifier = Modifier
            .fillMaxSize(),
    ) {

        Row(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .padding(horizontal = 5.dp)
                .clip(shape = RoundedCornerShape(5.dp))
                .background(color = DateBackgroundColor),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            AutoSizeText(
                value = "시작: ${firstDate}",
                fontSize = 15.sp,
                minFontSize = 13.sp,
                color = Color.Black,
                modifier = Modifier
                    .padding(5.dp),
                maxLines = 1
            )
        }

        Spacer(modifier = Modifier.width(15.dp))

        Row(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .padding(horizontal = 5.dp)
                .clip(shape = RoundedCornerShape(5.dp))
                .background(color = DateBackgroundColor),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            AutoSizeText(
                value = "종료: ${secondDate}",
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

@Composable
fun BigDataSearchView(dataViewModel: DataViewModel) {

    val scrollState = rememberScrollState()

    val showOpenDialog = remember { mutableStateOf(false) }

    val lottoNumber = (1..45).toList()

    val resentLottoNumber = dataViewModel.resentLottoNumber.collectAsState()

    val selectedRangeLotto = dataViewModel.selectRangeLottoNumber.collectAsState()



    val callStartDate = dataViewModel.startDateFlow.collectAsState()

    var callEndDate = dataViewModel.endDateFlow.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Spacer(modifier = Modifier
            .height(20.dp))

        Row(modifier = Modifier
            .fillMaxWidth()
            .weight(0.33f)
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

                dataRangeView(
                    startDate = callStartDate.value,
                    endDate = callEndDate.value)

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
                    AutoSizeText(
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

                    AutoSizeText(
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
                StickBar(ballNumber = number, data = dataViewModel.calculate(number.toString(),
                    type = DataViewModel.ModeType.SEARCH) as Float)
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
                    callStartDate.value,
                    callEndDate.value,
                    selectedStartDate = {
                        dataViewModel.startDateFlow.value = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate().toString()
                    },
                    selectedEndDate = {
                        dataViewModel.endDateFlow.value = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate().toString()
                    }
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

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun MyNumberSearchView(dataViewModel: DataViewModel) {

    val callStartDate = dataViewModel.myNumberStartDateFlow.collectAsState()

    val callEndDate = dataViewModel.myNumberEndDateFlow.collectAsState()

    val showOpenDialog = remember { mutableStateOf(false) }

    val oneNumber = dataViewModel.myNumberOneFlow.collectAsState()

    val twoNumber = dataViewModel.myNumberTwoFlow.collectAsState()

    val threeNumber = dataViewModel.myNumberThreeFlow.collectAsState()

    val fourNumber = dataViewModel.myNumberFourFlow.collectAsState()

    val fiveNumber = dataViewModel.myNumberFiveFlow.collectAsState()

    val sixNumber = dataViewModel.myNumberSixFlow.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    val snackBarHostState = remember { SnackbarHostState() }

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(
            modifier = Modifier
                .height(20.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.12f)
                .padding(horizontal = 15.dp)
                .clip(shape = RoundedCornerShape(10.dp))

        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                dataRangeRowTypeView(
                    startDate = callStartDate.value,
                    endDate = callEndDate.value
                )

            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier
            .fillMaxWidth()) {
            Text(
                modifier = Modifier.padding(start = 10.dp, bottom = 10.dp),
                text = "나의 로또 번호",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
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
                Alignment.CenterHorizontally) {
                LottoNumberTextField(
                    modifier = Modifier,
                    value = oneNumber.value,
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

            Column(modifier = Modifier
                .weight(1f)
                .fillMaxHeight(), verticalArrangement = Arrangement.Center, Alignment.CenterHorizontally) {
                LottoNumberTextField(
                    modifier = Modifier,
                    value = twoNumber.value,
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

            Column(modifier = Modifier
                .weight(1f)
                .fillMaxHeight(), verticalArrangement = Arrangement.Center, Alignment.CenterHorizontally) {
                LottoNumberTextField(
                    modifier = Modifier,
                    value = threeNumber.value,
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

            Column(modifier = Modifier
                .weight(1f)
                .fillMaxHeight(), verticalArrangement = Arrangement.Center, Alignment.CenterHorizontally) {
                LottoNumberTextField(
                    modifier = Modifier,
                    value = fourNumber.value,
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

            Column(modifier = Modifier
                .weight(1f)
                .fillMaxHeight(), verticalArrangement = Arrangement.Center, Alignment.CenterHorizontally) {
                LottoNumberTextField(
                    modifier = Modifier,
                    value = fiveNumber.value,
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

            Column(modifier = Modifier
                .weight(1f)
                .fillMaxHeight(), verticalArrangement = Arrangement.Center, Alignment.CenterHorizontally) {
                LottoNumberTextField(
                    modifier = Modifier,
                    value = sixNumber.value,
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

        Spacer(modifier = Modifier.height(20.dp))


        Row(modifier = Modifier
            .fillMaxWidth()) {
            Text(
                modifier = Modifier.padding(start = 10.dp, bottom = 10.dp),
                text = "내 번호 조회 결과",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Column(modifier = Modifier
            .fillMaxWidth(0.9f)
            .weight(1f)
            .background(Color.White, shape = RoundedCornerShape(5.dp))
        ) {

            MyNumberViewPager(dataViewModel = dataViewModel)
        }

        Spacer(modifier = Modifier.height(5.dp))

        Box(modifier = Modifier.fillMaxWidth()){
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
                        callStartDate = callStartDate.value,
                        callEndDate = callEndDate.value,
                        selectedStartDate = {
                            dataViewModel.myNumberStartDateFlow.value = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate().toString()
                        },
                        selectedEndDate = {
                            dataViewModel.myNumberEndDateFlow.value = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate().toString()
                        }
                    )
                }

                Spacer(modifier = Modifier.width(5.dp))

                FloatingActionButton(
                    onClick = {

                        if(oneNumber.value == "" &&
                            twoNumber.value == "" &&
                            threeNumber.value == "" &&
                            fourNumber.value == "" &&
                            fiveNumber.value == "" &&
                            sixNumber.value == "") {
                            return@FloatingActionButton
                        } else {

                            val chunkValue = dataViewModel.chunkMake()

                            coroutineScope.launch {
                                dataViewModel.twoChunkNumberFlow.emit(chunkValue.secondNumber)

                                dataViewModel.threeChunkNumberFlow.emit(chunkValue.thirdNumber)

                                dataViewModel.fourChunkNumberFlow.emit(chunkValue.fourthNumber)

                                dataViewModel.fiveChunkNumberFlow.emit(chunkValue.fifthNumber)

                                dataViewModel.sixChunkNumberFlow.emit(chunkValue.sixthNumber)
                            }

                            Log.d(TAG,"2개 묶음 값 -> $chunkValue")

                        }
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Image(painter = painterResource(id = R.drawable.search_icon), contentDescription = "")
                }
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                SnackbarHost(hostState = snackBarHostState, modifier = Modifier)
            }
        }

}


}


