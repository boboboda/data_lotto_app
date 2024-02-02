package com.bobo.data_lotto_app.components

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.bobo.data_lotto_app.Localdb.BigDataModeNumber
import com.bobo.data_lotto_app.Localdb.Lotto
import com.bobo.data_lotto_app.Localdb.NormalModeNumber
import com.bobo.data_lotto_app.MainActivity.Companion.TAG
import com.bobo.data_lotto_app.ViewModel.AuthViewModel
import com.bobo.data_lotto_app.ViewModel.DataViewModel
import com.bobo.data_lotto_app.components.admob.showInterstitial
import com.bobo.data_lotto_app.ui.theme.DataSelectFirstColor
import com.bobo.data_lotto_app.ui.theme.DbContentColor
import com.bobo.data_lotto_app.ui.theme.DisableButtonColor
import com.bobo.data_lotto_app.ui.theme.EnableButtonColor
import com.bobo.data_lotto_app.ui.theme.LottoButtonColor
import com.bobo.data_lotto_app.ui.theme.ProportionButtonColor
import com.bobo.data_lotto_app.ui.theme.UseCountButtonColor
import com.bobo.data_lotto_app.ui.theme.WelcomeScreenBackgroundColor
import com.bobo.data_lotto_app.ui.theme.WelcomeScreenLoginButtonColor
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun FilterDialog(
    dataViewModel: DataViewModel,
    fixNumber: List<Int>,
    onDismissRequest: (Boolean) -> Unit,
    type: DataViewModel.LotteryType = DataViewModel.LotteryType.NORMAL
) {

    val viewStateId = remember { mutableStateOf(1) }

    val addValue = remember { mutableStateOf("") }

    val removeValue = remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    Dialog(onDismissRequest = {
        onDismissRequest(false)
    }) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(16.dp)
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
                .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.Start) {
                Buttons(
                    label = "고정수",
                    onClicked = { viewStateId.value = 1},
                    buttonColor = if(viewStateId.value == 1) { EnableButtonColor } else { DisableButtonColor },
                    fontColor = if(viewStateId.value == 1) { Color.Black } else { Color.LightGray },
                    modifier = Modifier,
                    fontSize = 20
                )

                Spacer(modifier = Modifier.width(10.dp))

                Buttons(
                    label = "제외수",
                    onClicked = { viewStateId.value = 2},
                    buttonColor = if(viewStateId.value == 2) { EnableButtonColor } else { DisableButtonColor },
                    fontColor = if(viewStateId.value == 2) { Color.Black } else { Color.LightGray },
                    modifier = Modifier,
                    fontSize = 20
                )
            }

            Column(modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                when(viewStateId.value) {
                    1 -> { fixNumberView(
                        value = addValue.value,
                        onValueChanged = {
                            addValue.value = it.trim()
                        },
                        onClicked = {

                            when(type) {
                                DataViewModel.LotteryType.NORMAL ->  {
                                    scope.launch {
                                        val addNumber = dataViewModel.normalFixNumber.value.toMutableList()

                                        if(addValue.value.isNullOrEmpty() && addValue.value.isBlank()) {
                                            return@launch
                                        } else {
                                            when(fixNumber.count()) {
                                                0 -> {

                                                    if(fixNumber.contains(addValue.value.toInt())) {
                                                        Log.d(TAG, "이미 고정수로 지정되어 있습니다.")
                                                    } else {
                                                        addNumber.add(addValue.value.toInt())
                                                        dataViewModel.normalFixNumber.emit(addNumber)
                                                        dataViewModel.normalRemoveNumber.emit(addNumber)
                                                        onDismissRequest(false)
                                                    }
                                                }

                                                1 ->  {
                                                    if(fixNumber.contains(addValue.value.toInt())) {
                                                        Log.d(TAG, "이미 고정수로 지정되어 있습니다.")
                                                    } else {
                                                        addNumber.add(addValue.value.toInt())
                                                        dataViewModel.normalFixNumber.emit(addNumber)
                                                        dataViewModel.normalRemoveNumber.emit(addNumber)
                                                        onDismissRequest(false)
                                                    }
                                                }

                                                2 ->  {
                                                    if(fixNumber.contains(addValue.value.toInt())) {
                                                        Log.d(TAG, "이미 고정수로 지정되어 있습니다.")
                                                    } else {
                                                        addNumber.add(addValue.value.toInt())
                                                        dataViewModel.normalFixNumber.emit(addNumber)
                                                        dataViewModel.normalRemoveNumber.emit(addNumber)
                                                        onDismissRequest(false)
                                                    }
                                                }

                                                3 ->  {
                                                    if(fixNumber.contains(addValue.value.toInt())) {
                                                        Log.d(TAG, "이미 고정수로 지정되어 있습니다.")
                                                    } else {
                                                        addNumber.add(addValue.value.toInt())
                                                        dataViewModel.normalFixNumber.emit(addNumber)
                                                        dataViewModel.normalRemoveNumber.emit(addNumber)
                                                        onDismissRequest(false)
                                                    }
                                                }

                                                4 ->  {
                                                    if(fixNumber.contains(addValue.value.toInt())) {
                                                        Log.d(TAG, "이미 고정수로 지정되어 있습니다.")
                                                    } else {
                                                        addNumber.add(addValue.value.toInt())
                                                        dataViewModel.normalFixNumber.emit(addNumber)
                                                        dataViewModel.normalRemoveNumber.emit(addNumber)
                                                        onDismissRequest(false)
                                                    }
                                                }

                                                5 ->  {
                                                    if(fixNumber.contains(addValue.value.toInt())) {
                                                        Log.d(TAG, "이미 고정수로 지정되어 있습니다.")
                                                    } else {
                                                        addNumber.add(addValue.value.toInt())
                                                        dataViewModel.normalFixNumber.emit(addNumber)
                                                        dataViewModel.normalRemoveNumber.emit(addNumber)
                                                        onDismissRequest(false)
                                                    }
                                                }

                                                6 -> {
                                                    onDismissRequest(false)
                                                    return@launch
                                                }
                                            }
                                        }

                                    }
                                }

                                DataViewModel.LotteryType.BIGDATA -> {
                                    scope.launch {
                                        val addNumber = dataViewModel.bigDataFixNumber.value.toMutableList()

                                        if(addValue.value.isNullOrEmpty() && addValue.value.isBlank()) {
                                            return@launch
                                        } else {
                                            when(fixNumber.count()) {
                                                0 -> {

                                                    if(fixNumber.contains(addValue.value.toInt())) {
                                                        Log.d(TAG, "이미 고정수로 지정되어 있습니다.")
                                                    } else {
                                                        addNumber.add(addValue.value.toInt())
                                                        dataViewModel.bigDataFixNumber.emit(addNumber)
                                                        dataViewModel.bigDataRemoveNumber.emit(addNumber)
                                                        onDismissRequest(false)
                                                    }
                                                }

                                                1 ->  {
                                                    if(fixNumber.contains(addValue.value.toInt())) {
                                                        Log.d(TAG, "이미 고정수로 지정되어 있습니다.")
                                                    } else {
                                                        addNumber.add(addValue.value.toInt())
                                                        dataViewModel.bigDataFixNumber.emit(addNumber)
                                                        dataViewModel.bigDataRemoveNumber.emit(addNumber)
                                                        onDismissRequest(false)
                                                    }
                                                }

                                                2 ->  {
                                                    if(fixNumber.contains(addValue.value.toInt())) {
                                                        Log.d(TAG, "이미 고정수로 지정되어 있습니다.")
                                                    } else {
                                                        addNumber.add(addValue.value.toInt())
                                                        dataViewModel.bigDataFixNumber.emit(addNumber)
                                                        dataViewModel.bigDataRemoveNumber.emit(addNumber)
                                                        onDismissRequest(false)
                                                    }
                                                }

                                                3 ->  {
                                                    if(fixNumber.contains(addValue.value.toInt())) {
                                                        Log.d(TAG, "이미 고정수로 지정되어 있습니다.")
                                                    } else {
                                                        addNumber.add(addValue.value.toInt())
                                                        dataViewModel.bigDataFixNumber.emit(addNumber)
                                                        dataViewModel.bigDataRemoveNumber.emit(addNumber)
                                                        onDismissRequest(false)
                                                    }
                                                }

                                                4 ->  {
                                                    if(fixNumber.contains(addValue.value.toInt())) {
                                                        Log.d(TAG, "이미 고정수로 지정되어 있습니다.")
                                                    } else {
                                                        addNumber.add(addValue.value.toInt())
                                                        dataViewModel.bigDataFixNumber.emit(addNumber)
                                                        dataViewModel.bigDataRemoveNumber.emit(addNumber)
                                                        onDismissRequest(false)
                                                    }
                                                }

                                                5 ->  {
                                                    if(fixNumber.contains(addValue.value.toInt())) {
                                                        Log.d(TAG, "이미 고정수로 지정되어 있습니다.")
                                                    } else {
                                                        addNumber.add(addValue.value.toInt())
                                                        dataViewModel.bigDataFixNumber.emit(addNumber)
                                                        dataViewModel.bigDataRemoveNumber.emit(addNumber)
                                                        onDismissRequest(false)
                                                    }
                                                }

                                                6 -> {
                                                    onDismissRequest(false)
                                                    return@launch
                                                }
                                            }
                                        }

                                    }
                                }
                            }

                        },
                        closeClicked = {
                            onDismissRequest(false)
                        }) }

                    2 -> { ExceptNumberView(
                        value = removeValue.value,
                        onValueChanged = {
                            removeValue.value = it.trim()
                        },
                        onClicked = {

                            when(type) {
                                DataViewModel.LotteryType.NORMAL ->  {
                                    scope.launch {
                                        val removeNumber = dataViewModel.normalRemoveNumber.value.toMutableList()

                                        val viewRemoveNumber = dataViewModel.viewRemoveNumber.value.toMutableList()

                                        if(removeValue.value.isNullOrEmpty() && removeValue.value.isBlank()) {
                                            return@launch
                                        } else {
                                            if(removeNumber.contains(removeValue.value.toInt())) {
                                                Log.d(TAG, "이미 제외수로 지정되어 있습니다.")
                                            } else {
                                                removeNumber.add(removeValue.value.toInt())

                                                dataViewModel.normalRemoveNumber.emit(removeNumber)

                                                viewRemoveNumber.add(removeValue.value.toInt())

                                                dataViewModel.viewRemoveNumber.emit(viewRemoveNumber)

                                                onDismissRequest(false)
                                            }
                                        }
                                    }
                                }

                                DataViewModel.LotteryType.BIGDATA -> {
                                    scope.launch {
                                        val removeNumber = dataViewModel.bigDataRemoveNumber.value.toMutableList()

                                        val viewRemoveNumber = dataViewModel.bigDataViewRemoveNumber.value.toMutableList()


                                        if(removeValue.value.isNullOrEmpty() && removeValue.value.isBlank()) {
                                            return@launch
                                        } else {
                                            if(removeNumber.contains(removeValue.value.toInt())) {
                                                Log.d(TAG, "이미 제외수로 지정되어 있습니다.")
                                            } else {
                                                removeNumber.add(removeValue.value.toInt())

                                                dataViewModel.bigDataRemoveNumber.emit(removeNumber)

                                                viewRemoveNumber.add(removeValue.value.toInt())

                                                dataViewModel.bigDataViewRemoveNumber.emit(viewRemoveNumber)

                                                onDismissRequest(false)
                                            }
                                        }
                                    }
                                }
                            }




                        },
                        closeClicked = {
                            onDismissRequest(false)
                        }

                    ) }
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun fixNumberView(value: String,
                  onValueChanged: (String) -> Unit,
                  onClicked: () -> Unit,
                  closeClicked: () -> Unit) {
    androidx.compose.material3.TextField(
        value = value,
        onValueChange = onValueChanged,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = WelcomeScreenLoginButtonColor,
            focusedIndicatorColor = Color.White,
            unfocusedIndicatorColor = Color.White

        ),
        placeholder = { Text(text = "고정수를 입력해주세요", fontSize = 18.sp, color = Color.LightGray) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next)
    )

    Spacer(modifier = Modifier.height(20.dp))

    Row(modifier = Modifier
        .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center) {
        Buttons(
            label = "확인",
            onClicked = onClicked,
            buttonColor = DataSelectFirstColor,
            fontColor = Color.Black,
            modifier = Modifier,
            fontSize = 20
        )

        Spacer(modifier = Modifier.width(20.dp))

        Buttons(
            label = "닫기",
            onClicked = closeClicked,
            buttonColor = DataSelectFirstColor,
            fontColor = Color.Black,
            modifier = Modifier,
            fontSize = 20
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExceptNumberView(
    value: String,
    onValueChanged: (String) -> Unit,
    onClicked: () -> Unit,
    closeClicked: () -> Unit
) {
    androidx.compose.material3.TextField(
        value = value,
        onValueChange = onValueChanged,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = WelcomeScreenLoginButtonColor,
            focusedIndicatorColor = Color.White,
            unfocusedIndicatorColor = Color.White

        ),
        placeholder = { Text(text = "제외수를 입력해주세요", fontSize = 18.sp, color = Color.LightGray) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next)
    )

    Spacer(modifier = Modifier.height(20.dp))

    Row(modifier = Modifier
        .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center) {
        Buttons(
            label = "확인",
            onClicked = onClicked,
            buttonColor = DataSelectFirstColor,
            fontColor = Color.Black,
            modifier = Modifier,
            fontSize = 20
        )

        Spacer(modifier = Modifier.width(20.dp))

        Buttons(
            label = "닫기",
            onClicked = closeClicked,
            buttonColor = DataSelectFirstColor,
            fontColor = Color.Black,
            modifier = Modifier,
            fontSize = 20
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LottoAnimationDialog(
    closeClicked: (Boolean) -> Unit,
    dataViewModel: DataViewModel,
    onDismissRequest: (Boolean) -> Unit,
) {

    val scope = rememberCoroutineScope()

    val animationStop = remember { mutableStateOf(LottieConstants.IterateForever) }

    val newNumberData = dataViewModel.haveNormalNumberData.collectAsState()

    val removeNumber = dataViewModel.normalRemoveNumber.collectAsState()


    val lottieComposition by rememberLottieComposition(
        LottieCompositionSpec.Asset(assetName = "lotto.json")
    )
    val lottieAnimateble by animateLottieCompositionAsState(
        lottieComposition,
        iterations = animationStop.value,
        isPlaying = true,
        restartOnPlay = true)


    var isSlideSpring = remember { mutableStateOf(false) }

    val firstSlideAnimation = animateIntAsState(
        targetValue = if(isSlideSpring.value) 0 else -500,
        animationSpec = tween(durationMillis = 300,
            delayMillis = if(isSlideSpring.value) 50 else 0,
            easing = LinearOutSlowInEasing))

    val secondSlideAnimation = animateIntAsState(
        targetValue = if(isSlideSpring.value) 0 else -500,
        animationSpec = tween(durationMillis = 300,
            delayMillis = if(isSlideSpring.value) 200 else 0,
            easing = LinearOutSlowInEasing))

    val thirdSlideAnimation = animateIntAsState(
        targetValue = if(isSlideSpring.value) 0 else -500,
        animationSpec = tween(durationMillis = 300,
            delayMillis = if(isSlideSpring.value) 400 else 0,
            easing = LinearOutSlowInEasing))

    val fourSlideAnimation = animateIntAsState(
        targetValue = if(isSlideSpring.value) 0 else -500,
        animationSpec = tween(durationMillis = 300,
            delayMillis = if(isSlideSpring.value) 600 else 0,
            easing = LinearOutSlowInEasing))

    val fiveSlideAnimation = animateIntAsState(
        targetValue = if(isSlideSpring.value) 0 else -500,
        animationSpec = tween(durationMillis = 300,
            delayMillis = if(isSlideSpring.value) 800 else 0,
            easing = LinearOutSlowInEasing))

    val sixSlideAnimation = animateIntAsState(
        targetValue = if(isSlideSpring.value) 0 else -500,
        animationSpec = tween(durationMillis = 300,
            delayMillis = if(isSlideSpring.value) 1000 else 0,
            easing = LinearOutSlowInEasing))

    Dialog(onDismissRequest = {
        onDismissRequest(false)
    }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White
                ),
            horizontalAlignment = Alignment.CenterHorizontally) {
            
            Spacer(modifier = Modifier.height(10.dp))

            Text(fontWeight = FontWeight.Bold,
                text = "로또 그림을 클릭해주세요")

            Card(
                modifier = Modifier.size(200.dp),
                onClick = {
                    if(newNumberData.value.firstNumber == null){
                        animationStop.value = 3
                        isSlideSpring.value = true
                        dataViewModel.lottery(
                            removeNumber = removeNumber.value
                        )
                    } else {
                        return@Card
                    }
            }
            ) {
                LottieAnimation(
                    composition = lottieComposition,
                    contentScale = ContentScale.Fit,
                    progress = lottieAnimateble,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .background(color = Color.White),
                    alignment = Alignment.Center,
                )
            }


            Row(modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
                horizontalArrangement = Arrangement.Center) {

                if(newNumberData.value.firstNumber != null) {
                    BallDraw(
                        modifier = Modifier
                            .offset(y = firstSlideAnimation.value.dp),
                        ballOder = "",
                        ballValue = newNumberData.value.firstNumber!!)
                }

                if(newNumberData.value.secondNumber != null) {
                    BallDraw(
                        modifier = Modifier
                            .offset(y = secondSlideAnimation.value.dp),
                        ballOder = "",
                        ballValue = newNumberData.value.secondNumber!!)
                }

                if(newNumberData.value.thirdNumber != null) {
                    BallDraw(
                        modifier = Modifier
                            .offset(y = thirdSlideAnimation.value.dp),
                        ballOder = "",
                        ballValue = newNumberData.value.thirdNumber!!)
                }

                if(newNumberData.value.fourthNumber != null) {
                    BallDraw(
                        modifier = Modifier
                            .offset(y = fourSlideAnimation.value.dp),
                        ballOder = "",
                        ballValue = newNumberData.value.fourthNumber!!)
                }

                if(newNumberData.value.fifthNumber != null) {
                    BallDraw(
                        modifier = Modifier
                            .offset(y = fiveSlideAnimation.value.dp),
                        ballOder = "",
                        ballValue = newNumberData.value.fifthNumber!!)
                }

                if(newNumberData.value.sixthNumber != null) {
                    BallDraw(
                        modifier = Modifier
                            .offset(y = sixSlideAnimation.value.dp),
                        ballOder = "",
                        ballValue = newNumberData.value.sixthNumber!!)
                }

            }
            Row(modifier = Modifier
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center) {

                Buttons(
                    label = "다시 뽑기",
                    onClicked = {
                        scope.launch {
                            isSlideSpring.value = false
                            animationStop.value = LottieConstants.IterateForever
                            dataViewModel.haveNormalNumberData.emit(NormalModeNumber())
                        }
                    },
                    buttonColor = DisableButtonColor,
                    fontColor = Color.Black,
                    modifier = Modifier,
                    fontSize = 20
                )

            }


            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center) {
                Buttons(
                    label = "추가",
                    onClicked = {
                        scope.launch {
                            if(newNumberData.value.firstNumber != null){
                                dataViewModel.emitNumber()
                                dataViewModel.haveNormalNumberData.emit(NormalModeNumber())
                                closeClicked.invoke(false)
                            } else {
                                return@launch
                            }
                        }
                    },
                    buttonColor = DataSelectFirstColor,
                    fontColor = Color.Black,
                    modifier = Modifier,
                    fontSize = 20
                )

                Spacer(modifier = Modifier.width(20.dp))

                Buttons(
                    label = "닫기",
                    onClicked = {

                        isSlideSpring.value = false
                        closeClicked.invoke(false)

                        scope.launch {
                            dataViewModel.haveNormalNumberData.emit(NormalModeNumber())
                        }

                    },
                    buttonColor = DataSelectFirstColor,
                    fontColor = Color.Black,
                    modifier = Modifier,
                    fontSize = 20
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BigDataLottoAnimationDialog(
    closeClicked: (Boolean) -> Unit,
    dataViewModel: DataViewModel,
    authViewModel: AuthViewModel,
    onDismissRequest: (Boolean) -> Unit,
) {

    val scope = rememberCoroutineScope()

    val animationStop = remember { mutableStateOf(LottieConstants.IterateForever) }

    val newNumberData = dataViewModel.haveBigDataNumberData.collectAsState()

    val removeNumber = dataViewModel.bigDataRemoveNumber.collectAsState()

    val lotteryCount = authViewModel.numberLotteryCountFlow.collectAsState()

    val isSelectedDate = dataViewModel.bigDataModeRangeNumberStateFlow.collectAsState()

    val context = LocalContext.current






    val lottieComposition by rememberLottieComposition(
        LottieCompositionSpec.Asset(assetName = "lotto.json")
    )
    val lottieAnimatable by animateLottieCompositionAsState(
        lottieComposition,
        iterations = animationStop.value,
        isPlaying = true,
        restartOnPlay = true)


    var isSlideSpring = remember { mutableStateOf(false) }

    val firstSlideAnimation = animateIntAsState(
        targetValue = if(isSlideSpring.value) 0 else -500,
        animationSpec = tween(durationMillis = 300,
            delayMillis = 50,
            easing = LinearOutSlowInEasing))

    val secondSlideAnimation = animateIntAsState(
        targetValue = if(isSlideSpring.value) 0 else -500,
        animationSpec = tween(durationMillis = 300,
            delayMillis = 200,
            easing = LinearOutSlowInEasing))

    val thirdSlideAnimation = animateIntAsState(
        targetValue = if(isSlideSpring.value) 0 else -500,
        animationSpec = tween(durationMillis = 300,
            delayMillis = 400,
            easing = LinearOutSlowInEasing))

    val fourSlideAnimation = animateIntAsState(
        targetValue = if(isSlideSpring.value) 0 else -500,
        animationSpec = tween(durationMillis = 300,
            delayMillis = 600,
            easing = LinearOutSlowInEasing))

    val fiveSlideAnimation = animateIntAsState(
        targetValue = if(isSlideSpring.value) 0 else -500,
        animationSpec = tween(durationMillis = 300,
            delayMillis = 800,
            easing = LinearOutSlowInEasing))

    val sixSlideAnimation = animateIntAsState(
        targetValue = if(isSlideSpring.value) 0 else -500,
        animationSpec = tween(durationMillis = 300,
            delayMillis = 1000,
            easing = LinearOutSlowInEasing))





    Dialog(onDismissRequest = {
        onDismissRequest(false)
    }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White
                ),
            horizontalAlignment = Alignment.CenterHorizontally) {

            Spacer(modifier = Modifier.height(10.dp))

            Text(text = "로또 그림을 클릭해주세요")
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                fontSize = 20.sp
                ,text = "기회: ${lotteryCount.value}")


            Card(
                modifier = Modifier.size(200.dp),
                onClick = {
                    if(lotteryCount.value == 0) {
                        showInterstitial(context) {
                            if (newNumberData.value.firstNumber == null) {
                                animationStop.value = 3
                                isSlideSpring.value = true
                                dataViewModel.lottery(
                                    removeNumber = removeNumber.value,
                                    modeType = DataViewModel.LotteryType.BIGDATA
                                )
                            } else {
                                return@showInterstitial
                            }
                        }
                    } else {

                        if (newNumberData.value.firstNumber == null) {
                            animationStop.value = 3
                            isSlideSpring.value = true
                            dataViewModel.lottery(
                                removeNumber = removeNumber.value,
                                modeType = DataViewModel.LotteryType.BIGDATA
                            )

                            // 기회 차감
                            authViewModel.filterItem(
                                itemCount = lotteryCount.value,
                                useType = UseType.LOTTERY
                            )

                        } else {
                            return@Card
                        }


                    }
                }
            ) {
                LottieAnimation(
                    composition = lottieComposition,
                    contentScale = ContentScale.Fit,
                    progress = lottieAnimatable,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .background(color = Color.White),
                    alignment = Alignment.Center,
                )
            }


            Row(modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
                horizontalArrangement = Arrangement.Center) {

                if(newNumberData.value.firstNumber != null) {
                    BallDraw(
                        modifier = Modifier
                            .offset(y = firstSlideAnimation.value.dp),
                        ballOder = "",
                        ballValue = newNumberData.value.firstNumber!!)
                }

                if(newNumberData.value.secondNumber != null) {
                    BallDraw(
                        modifier = Modifier
                            .offset(y = secondSlideAnimation.value.dp),
                        ballOder = "",
                        ballValue = newNumberData.value.secondNumber!!)
                }

                if(newNumberData.value.thirdNumber != null) {
                    BallDraw(
                        modifier = Modifier
                            .offset(y = thirdSlideAnimation.value.dp),
                        ballOder = "",
                        ballValue = newNumberData.value.thirdNumber!!)
                }

                if(newNumberData.value.fourthNumber != null) {
                    BallDraw(
                        modifier = Modifier
                            .offset(y = fourSlideAnimation.value.dp),
                        ballOder = "",
                        ballValue = newNumberData.value.fourthNumber!!)
                }

                if(newNumberData.value.fifthNumber != null) {
                    BallDraw(
                        modifier = Modifier
                            .offset(y = fiveSlideAnimation.value.dp),
                        ballOder = "",
                        ballValue = newNumberData.value.fifthNumber!!)
                }

                if(newNumberData.value.sixthNumber != null) {
                    BallDraw(
                        modifier = Modifier
                            .offset(y = sixSlideAnimation.value.dp),
                        ballOder = "",
                        ballValue = newNumberData.value.sixthNumber!!)
                }

            }

            Row(modifier = Modifier
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center) {

                Buttons(
                    label = "다시 뽑기",
                    onClicked = {
                        scope.launch {
                            isSlideSpring.value = false
                            animationStop.value = LottieConstants.IterateForever
                            dataViewModel.haveBigDataNumberData.emit(BigDataModeNumber())
                        }
                    },
                    buttonColor = DisableButtonColor,
                    fontColor = Color.Black,
                    modifier = Modifier,
                    fontSize = 20
                )

            }


            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center) {

                Buttons(
                    label = "추가",
                    onClicked = {
                        scope.launch {
                            if(newNumberData.value.firstNumber != null){
                                dataViewModel.emitNumber(modeType = DataViewModel.LotteryType.BIGDATA)
                                dataViewModel.haveBigDataNumberData.emit(BigDataModeNumber())
                                closeClicked.invoke(false)
                            } else {
                                return@launch
                            }
                        }
                    },
                    buttonColor = DataSelectFirstColor,
                    fontColor = Color.Black,
                    modifier = Modifier,
                    fontSize = 20
                )

                Spacer(modifier = Modifier.width(20.dp))

                Buttons(
                    label = "닫기",
                    onClicked = {

                        isSlideSpring.value = false
                        closeClicked.invoke(false)

                        scope.launch {
                            dataViewModel.haveNormalNumberData.emit(NormalModeNumber())
                        }

                    },
                    buttonColor = DataSelectFirstColor,
                    fontColor = Color.Black,
                    modifier = Modifier,
                    fontSize = 20
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

        }
    }

}

@Composable
fun ProportionSelectDialog(
    snackBarHostState: SnackbarHostState,
    closeClicked: (Boolean) -> Unit,
    dataViewModel: DataViewModel,
    authViewModel: AuthViewModel,
    onDismissRequest: (Boolean) -> Unit,
) {

    val showLottoAnimationDialog = remember{ mutableStateOf( false ) }

    val scope = rememberCoroutineScope()

    val isSelectDateValue = dataViewModel.bigDataModeRangeNumberStateFlow.collectAsState()

    Dialog(onDismissRequest = { onDismissRequest(false)}
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize()
                .background(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White
                ),
            horizontalAlignment = Alignment.CenterHorizontally) {

            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    fontWeight = FontWeight.Bold,
                    fontFamily = fontFamily,
                    textAlign = TextAlign.Center,
                    lineHeight = 23.sp,
                    text = "날짜 범위를 선택하면 범위 내 \n" +
                        "고비율과 저비율을 계산할 수 있습니다.\n" +
                            "(날짜 범위를 선택하지 않으면 전체 범위에서 선택됩니다. \n " +
                            "전체 범위는 기회가 차감되지 않습니다)")
            }

            Spacer(modifier = Modifier.height(10.dp))

            Buttons(
                label = "고비율",
                onClicked = {

                    if(isSelectDateValue.value.count() <= 0) {

                        Log.d(TAG, "UseCountDialog 예외처리됨")

                        scope.launch {
                            snackBarHostState.showSnackbar(
                                "날짜 범위가 지정되어 있지 않습니다.\n" +
                                        "지정 후 사용해주세요"
                            )
                        }
                        showLottoAnimationDialog.value = false

                    } else {
                        dataViewModel.proportionStateFlow.value = "1"
                        showLottoAnimationDialog.value = true
                    }
                },
                buttonColor = ProportionButtonColor,
                fontColor = Color.Black,
                modifier = Modifier.fillMaxWidth(0.7f),
                fontSize = 15
            )

            Spacer(modifier = Modifier.height(10.dp))

            Buttons(
                label = "저비율",
                onClicked = {
                    if(isSelectDateValue.value.count() <= 0) {

                        Log.d(TAG, "UseCountDialog 예외처리됨")

                        scope.launch {
                            snackBarHostState.showSnackbar(
                                "날짜 범위가 지정되어 있지 않습니다.\n" +
                                        "지정 후 사용해주세요"
                            )
                        }
                        showLottoAnimationDialog.value = false

                    } else {
                        dataViewModel.proportionStateFlow.value = "2"
                        showLottoAnimationDialog.value = true
                    }
                },
                buttonColor = ProportionButtonColor,
                fontColor = Color.Black,
                modifier = Modifier.fillMaxWidth(0.7f),
                fontSize = 15
            )

            Spacer(modifier = Modifier.height(10.dp))

//            Buttons(
//                label = "고비율3/ 저비율3",
//                onClicked = {
//
//
//                },
//                buttonColor = ProportionButtonColor,
//                fontColor = Color.Black,
//                modifier = Modifier.fillMaxWidth(0.7f),
//                fontSize = 15
//            )
//
//            Spacer(modifier = Modifier.height(10.dp))
//
//
//            Buttons(
//                label = "범위 내 숫자 추첨",
//                onClicked = {
//
//
//                },
//                buttonColor = ProportionButtonColor,
//                fontColor = Color.Black,
//                modifier = Modifier.fillMaxWidth(0.7f),
//                fontSize = 15
//            )
//
//            Spacer(modifier = Modifier.height(10.dp))
        }
    }

    if(showLottoAnimationDialog.value) {
        BigDataLottoAnimationDialog(
            closeClicked = {
                closeClicked(it)
            },
            dataViewModel = dataViewModel,
            authViewModel = authViewModel,
            onDismissRequest = {
                showLottoAnimationDialog.value = it
            }
        )
    }
}

enum class UseType(name: String){
    ALLNUMBER("allNumber"),
    MYNUMBER("myNumber"),
    LOTTERY("lottery")
}
@Composable
fun UseCountDialog(
    label: String,
    onClicked: () -> Unit,
    onDismissRequest: (Boolean) -> Unit,
) {

    Dialog(onDismissRequest = {
        onDismissRequest.invoke(false)
    }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 30.dp)
                .background(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(vertical = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Buttons(
                    label = label,
                    onClicked = {
                                onClicked.invoke()
                    },
                    buttonColor = UseCountButtonColor,
                    fontColor = Color.Black,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .fillMaxWidth(0.8f)
                        .height(50.dp),
                    fontSize = 20
                )
            }
        }

    }


}


