package com.bobo.data_lotto_app.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.R
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bobo.data_lotto_app.ui.theme.LoginEnableButtonColor
import com.bobo.data_lotto_app.ui.theme.WelcomeScreenBackgroundColor
import com.bobo.data_lotto_app.ui.theme.WelcomeScreenLoginButtonColor
import com.bobo.data_lotto_app.ui.theme.WelcomeScreenRegisterButtonColor

@Composable
fun Buttons(
    label: String,
    onClicked:(()->Unit)?,
    buttonColor: Color,
    fontColor: Color,
    enabled: Boolean = true,
    modifier: Modifier,
    fontSize: Int) {
    Button(
        colors = ButtonDefaults.buttonColors(
            backgroundColor = buttonColor,
            contentColor = fontColor,
            disabledContentColor = Color.White,
            disabledBackgroundColor = Color.Gray),
        modifier = modifier,
        enabled = enabled,
        onClick = {onClicked?.invoke()})
    {
        AutoSizeText(
            value = label,
            fontFamily = fontFamily ,
            fontSize = fontSize.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(0.dp),
            color = fontColor,
            maxLines = 1,
            minFontSize = 12.sp,
            fontWeight = FontWeight.Bold)
    }
}




enum class ButtonType {
    FILL, OUTLINE
}
@Composable
fun BaseButton(
    modifier: Modifier = Modifier,
    type: ButtonType = ButtonType.FILL,
    title: String,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    onClick: () -> Unit,
    image: Int? = null,
    imageModifier: Modifier? = null
) {
    when(type) {
        ButtonType.FILL -> MainFilledButton(
            modifier,
            title,
            enabled,
            isLoading,
            onClick,
            image,
            imageModifier ?: Modifier

        )
        ButtonType.OUTLINE -> MainOutlineButton(
            modifier,
            title,
            enabled,
            isLoading,
            onClick
        )
    }

}

@Composable
fun MainFilledButton(
    modifier: Modifier = Modifier,
    title: String,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    onClick: () -> Unit,
    image: Int?,
    imageModifier: Modifier = Modifier
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.Black,
            backgroundColor = WelcomeScreenLoginButtonColor,
            disabledContentColor = Color.Gray,
            disabledBackgroundColor = LoginEnableButtonColor
        ),
        enabled = enabled,
        onClick = onClick
    ) {
        Row(modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.Start) {
            if(image != null) {

                Image(
                    painter = painterResource(id = image),
                    contentDescription = "",
                    alignment = Alignment.Center,
                    modifier = imageModifier
                )


            }
        }

        if (isLoading) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier
                    .scale(1f)
                    .size(20.dp)
            )
        } else {
            Text(text = title,
                fontSize = 15.sp,
                modifier = Modifier.padding(16.dp))
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun MainOutlineButton(
    modifier: Modifier = Modifier,
    title: String,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.Black,
            backgroundColor = WelcomeScreenRegisterButtonColor,
            disabledContentColor = Color.White,
            disabledBackgroundColor = Color.White
        ),
        enabled = enabled,
        border = BorderStroke(1.dp, Color.Black),
        onClick = onClick
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = Color.Black,
                modifier = Modifier
                    .scale(1f)
                    .size(20.dp)
            )
        } else {
            Text(text = title,
                fontSize = 15.sp,
                color = Color.Black,
                modifier = Modifier.padding(16.dp))
        }
    }
}

@Composable
fun LogInBackButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit) {
    Button(
        modifier = modifier
            .size(50.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.Black,
            backgroundColor = Color.White,
            disabledContentColor = Color.White,
            disabledBackgroundColor = Color.White
        ),
//        border = BorderStroke(1.dp, color = Color.Black),
        onClick = onClick
    ) {
        Image(
            painter = painterResource(id = com.bobo.data_lotto_app.R.drawable.back_arrow),
            "뒤로가기 버튼",
            modifier = Modifier.size(20.dp))

    }
}

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    onClick: () -> Unit,
    composable: @Composable () -> Unit
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.Black,
            backgroundColor = WelcomeScreenLoginButtonColor,
            disabledContentColor = Color.Gray,
            disabledBackgroundColor = LoginEnableButtonColor
        ),
        enabled = enabled,
        onClick = onClick
    ) {

        if (isLoading) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier
                    .scale(1f)
                    .size(20.dp)
            )
        } else {
            composable()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardButton(label: String,
               selectedLabel: String? = null,
               onClicked: (String) -> Unit,
               fontSize: Int,
               modifier: Modifier,
               fontColor: Color,
               buttonColor: Color,
               disableColor: Color? = null
) {

    var cardLabel : String = label




    var color = if(disableColor != null) {
        if (cardLabel == selectedLabel) fontColor else disableColor
    } else {
        fontColor
    }

    androidx.compose.material3.Card(colors = CardDefaults.cardColors(buttonColor),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = modifier,
        border = BorderStroke(1.dp, color),
        shape = RoundedCornerShape(2.dp),
        onClick = {
            onClicked(label)
        }) {
        Row(
            Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                AutoSizeText(
                    value = "$label",
                    modifier = Modifier,
                    fontSize = fontSize.sp,
                    maxLines = 1,
                    minFontSize = 10.sp,
                    color = color!!
                )
            }

        }

    }
}
