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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.bobo.data_lotto_app.Localdb.NormalModeNumber
import com.bobo.data_lotto_app.MainActivity.Companion.TAG
import com.bobo.data_lotto_app.ViewModel.DataViewModel
import com.bobo.data_lotto_app.screens.main.BallDraw
import com.bobo.data_lotto_app.ui.theme.DataSelectFirstColor
import com.bobo.data_lotto_app.ui.theme.DbContentColor
import com.bobo.data_lotto_app.ui.theme.DisableButtonColor
import com.bobo.data_lotto_app.ui.theme.EnableButtonColor
import com.bobo.data_lotto_app.ui.theme.LottoButtonColor
import com.bobo.data_lotto_app.ui.theme.WelcomeScreenBackgroundColor
import com.bobo.data_lotto_app.ui.theme.WelcomeScreenLoginButtonColor
import kotlinx.coroutines.launch

@Composable
fun FilterDialog(
    dataViewModel: DataViewModel,
    onDismissRequest: (Boolean) -> Unit,
) {

    val viewStateId = remember { mutableStateOf(1) }

    val addValue = remember { mutableStateOf("") }

    val removeValue = remember { mutableStateOf("") }

    val initRemoveNumber = dataViewModel.normalRemoveNumber.collectAsState()

    val scope = rememberCoroutineScope()

    val fixNumber = dataViewModel.normalFixNumber.collectAsState()

    val removeNumber = dataViewModel.viewRemoveNumber.collectAsState()

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
                            scope.launch {
                                val addNumber = dataViewModel.normalFixNumber.value.toMutableList()

                                if(addValue.value.isNullOrEmpty() && addValue.value.isBlank()) {
                                    return@launch
                                } else {
                                    when(fixNumber.value.count()) {
                                        0 -> {

                                            if(fixNumber.value.contains(addValue.value.toInt())) {
                                                Log.d(TAG, "이미 고정수로 지정되어 있습니다.")
                                            } else {
                                                addNumber.add(addValue.value.toInt())
                                                dataViewModel.normalFixNumber.emit(addNumber)
                                                dataViewModel.normalRemoveNumber.emit(addNumber)
                                                onDismissRequest(false)
                                            }
                                        }

                                        1 ->  {
                                            if(fixNumber.value.contains(addValue.value.toInt())) {
                                                Log.d(TAG, "이미 고정수로 지정되어 있습니다.")
                                            } else {
                                                addNumber.add(addValue.value.toInt())
                                                dataViewModel.normalFixNumber.emit(addNumber)
                                                dataViewModel.normalRemoveNumber.emit(addNumber)
                                                onDismissRequest(false)
                                            }
                                        }

                                        2 ->  {
                                            if(fixNumber.value.contains(addValue.value.toInt())) {
                                                Log.d(TAG, "이미 고정수로 지정되어 있습니다.")
                                            } else {
                                                addNumber.add(addValue.value.toInt())
                                                dataViewModel.normalFixNumber.emit(addNumber)
                                                dataViewModel.normalRemoveNumber.emit(addNumber)
                                                onDismissRequest(false)
                                            }
                                        }

                                        3 ->  {
                                            if(fixNumber.value.contains(addValue.value.toInt())) {
                                                Log.d(TAG, "이미 고정수로 지정되어 있습니다.")
                                            } else {
                                                addNumber.add(addValue.value.toInt())
                                                dataViewModel.normalFixNumber.emit(addNumber)
                                                dataViewModel.normalRemoveNumber.emit(addNumber)
                                                onDismissRequest(false)
                                            }
                                        }

                                        4 ->  {
                                            if(fixNumber.value.contains(addValue.value.toInt())) {
                                                Log.d(TAG, "이미 고정수로 지정되어 있습니다.")
                                            } else {
                                                addNumber.add(addValue.value.toInt())
                                                dataViewModel.normalFixNumber.emit(addNumber)
                                                dataViewModel.normalRemoveNumber.emit(addNumber)
                                                onDismissRequest(false)
                                            }
                                        }

                                        5 ->  {
                                            if(fixNumber.value.contains(addValue.value.toInt())) {
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

                            Log.d(TAG, "고정값 ${fixNumber}")

                            Log.d(TAG, "초기 제외값 ${initRemoveNumber.value}")

                            Log.d(TAG, "제외값 ${removeNumber.value}")

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

                                            Log.d(TAG, "고정값 ${fixNumber.value}")

                                            Log.d(TAG, "초기 제외값 ${initRemoveNumber.value}")

                                            Log.d(TAG, "제외값 ${removeNumber}")

                                            onDismissRequest(false)
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
                .height(350.dp)
                .background(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White
                ),
            horizontalAlignment = Alignment.CenterHorizontally) {
            
            Spacer(modifier = Modifier.height(10.dp))

            Text(text = "로또 그림을 클릭해주세요")

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
                    label = "추가",
                    onClicked = {
                        scope.launch {
                            if(newNumberData.value.firstNumber != null){
                                dataViewModel.emitNormalNumber()
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