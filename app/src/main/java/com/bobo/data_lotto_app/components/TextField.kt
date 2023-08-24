package com.bobo.data_lotto_app.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bobo.data_lotto_app.R
import kotlin.math.sin


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailTextField(
    modifier: Modifier,
    label: String,
    keyboardActions: KeyboardActions = KeyboardActions(),
    keyboardOptions: KeyboardOptions  = KeyboardOptions(
        keyboardType = KeyboardType.Email,
        imeAction = ImeAction.Next),
    singleLine: Boolean = true,
    value: String,
    onValueChanged: (String) -> Unit

) {
    Column() {

            TextField(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(0.dp),
                textStyle = TextStyle(fontSize = 18.sp),
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                singleLine = singleLine,
                value = value,
                placeholder = { Text(text = label, fontSize = 18.sp, color = LightGray) },
                onValueChange = onValueChanged,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = White,
                    focusedIndicatorColor = Gray
                )
            )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextField(
    modifier: Modifier,
    label: String,
    value: String,
    onValueChanged: (String) -> Unit,
    keyboardActions: KeyboardActions = KeyboardActions()
) {

    val passwordVisible = remember {
        mutableStateOf(false)
    }

    val passwordVisibleIcon = if (passwordVisible.value) R.drawable.ic_visible else R.drawable.ic_invisible

    Column {
               TextField(
                    modifier = modifier.fillMaxWidth(),
                    textStyle = TextStyle(fontSize = 18.sp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                    keyboardActions = keyboardActions,
                    singleLine = true,
                    visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                    value = value,
                    onValueChange = onValueChanged,
                   placeholder = { Text(text = label, fontSize = 18.sp, color = LightGray) },
                   trailingIcon = {
                       IconButton(onClick = {
                           passwordVisible.value = !passwordVisible.value
                       }) {
                           Image(
                               painter = painterResource(id = passwordVisibleIcon),
                               contentDescription = "비밀번호 노출여부 버튼"
                           )

                       }
                   },
                   colors = TextFieldDefaults.textFieldColors(
                       containerColor = White,
                       focusedIndicatorColor = Gray
                   )

                )
        }
    }