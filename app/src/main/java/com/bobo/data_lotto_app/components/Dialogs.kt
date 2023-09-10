package com.bobo.data_lotto_app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.bobo.data_lotto_app.ViewModel.DataViewModel
import com.bobo.data_lotto_app.ui.theme.DataSelectFirstColor
import com.bobo.data_lotto_app.ui.theme.DisableButtonColor
import com.bobo.data_lotto_app.ui.theme.EnableButtonColor
import kotlinx.coroutines.launch

@Composable
fun FilterDialog(
    dataViewModel: DataViewModel,
    onDismissRequest: (Boolean) -> Unit,
) {

    val viewStateId = remember { mutableStateOf(1) }

    val textValue = remember { mutableStateOf("") }

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
            Row {
                Buttons(
                    label = "고정수",
                    onClicked = { viewStateId.value = 1},
                    buttonColor = if(viewStateId.value == 1) { EnableButtonColor } else { DisableButtonColor },
                    fontColor = if(viewStateId.value == 1) { Color.Black } else { Color.LightGray },
                    modifier = Modifier,
                    fontSize = 20
                )

                Buttons(
                    label = "제외수",
                    onClicked = { viewStateId.value = 2},
                    buttonColor = if(viewStateId.value == 2) { EnableButtonColor } else { DisableButtonColor },
                    fontColor = if(viewStateId.value == 2) { Color.Black } else { Color.LightGray },
                    modifier = Modifier,
                    fontSize = 20
                )
            }

            Column {
                when(viewStateId.value) {
                    1 -> { fixNumberView(
                        value = if (textValue.value.isNullOrEmpty()) { "고정수를 입력해주세요" } else { textValue.value },
                        onValueChanged = {
                            textValue.value = it
                        },
                        onClicked = {
                            scope.launch {

                                if(textValue.value.isNullOrEmpty()) {
                                    return@launch
                                } else {
                                    val removeNumber = dataViewModel.removeNumber.value.toMutableList()

                                    removeNumber.add(textValue.value.toInt())
                                }


                            }

                        },
                        closeClicked = {
                            onDismissRequest(false)
                        }) }
                    2 -> { ExceptNumberView(
                        value = if (textValue.value.isNullOrEmpty()) { "고정수를 입력해주세요" } else { textValue.value },
                        onValueChanged = {
                            textValue.value = it
                        },
                        onClicked = {
                            scope.launch {

                                if(textValue.value.isNullOrEmpty()) {
                                    return@launch
                                } else {
                                    val removeNumber = dataViewModel.removeNumber.value.toMutableList()

                                    removeNumber.add(textValue.value.toInt())
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

@Composable
fun fixNumberView(value: String,
                  onValueChanged: (String) -> Unit,
                  onClicked: () -> Unit,
                  closeClicked: () -> Unit) {
    TextField(
        value = value,
        onValueChange = onValueChanged,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next)
    )

    Row {
        Buttons(
            label = "확인",
            onClicked = onClicked,
            buttonColor = DataSelectFirstColor,
            fontColor = Color.Black,
            modifier = Modifier,
            fontSize = 20
        )

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

@Composable
fun ExceptNumberView(
    value: String,
    onValueChanged: (String) -> Unit,
    onClicked: () -> Unit,
    closeClicked: () -> Unit
) {

    TextField(
        value = value,
        onValueChange = onValueChanged,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next)
    )

    Row {
        Buttons(
            label = "확인",
            onClicked = onClicked,
            buttonColor = DataSelectFirstColor,
            fontColor = Color.Black,
            modifier = Modifier,
            fontSize = 20
        )

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