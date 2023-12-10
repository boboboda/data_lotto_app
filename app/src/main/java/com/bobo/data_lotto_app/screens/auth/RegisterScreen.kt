package com.bobo.data_lotto_app.screens.auth

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bobo.data_lotto_app.Routes.AuthRoute
import com.bobo.data_lotto_app.Routes.AuthRouteAction
import com.bobo.data_lotto_app.ViewModel.AuthViewModel
import com.bobo.data_lotto_app.components.BaseButton
import com.bobo.data_lotto_app.components.EmailTextField
import com.bobo.data_lotto_app.components.LogInBackButton
import com.bobo.data_lotto_app.components.PasswordTextField
import com.bobo.data_lotto_app.components.fontFamily
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(authViewModel: AuthViewModel,
                   routeAction: AuthRouteAction) {

    val emailInput = authViewModel.registerEmailInputFlow.collectAsState()

    val nickname = authViewModel.registerNicknameFlow.collectAsState()

    val passwordInput = authViewModel.registerPasswordInputFlow.collectAsState()

    val passwordConfirmInput = authViewModel.registerPasswordConfirmInputFlow.collectAsState()

    val isPasswordsNotEmpty = passwordInput.value.isNotEmpty() &&
            passwordConfirmInput.value.isNotEmpty()


    val isPasswordMatched = passwordInput.value == passwordConfirmInput.value

    val isRegisterBtnActive = emailInput.value.isNotEmpty() &&
            isPasswordsNotEmpty && isPasswordMatched

    val scrollState = rememberScrollState()

    val snackBarHostState = remember{ SnackbarHostState() }

    val coroutineScope = rememberCoroutineScope()

    val isLoading = authViewModel.registerIsLoadingFlow.collectAsState()

    val registerSuccess = authViewModel.registerSuccessFlow.collectAsState()

    val focusManager = LocalFocusManager.current






    Column(
        modifier = Modifier
            .padding(horizontal = 22.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        Row(modifier = Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            LogInBackButton(
                modifier = Modifier
                    .padding(top = 20.dp),
                onClick = {
                    coroutineScope.launch {
                        authViewModel.registerEmailInputFlow.emit("")
                        authViewModel.registerPasswordInputFlow.emit("")
                        authViewModel.registerPasswordConfirmInputFlow.emit("")
                        authViewModel.registerNicknameFlow.emit("")
                        routeAction.navTo(AuthRoute.WELCOME)
                    }
                }
            )
        }

        Text(
            "REGISTER",
            fontSize = 35.sp,
            fontFamily = fontFamily,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        EmailTextField(
            label = "이메일",
            value = emailInput.value,
            onValueChanged = {
                coroutineScope.launch {
                    authViewModel.registerEmailInputFlow.emit(it)
                    scrollState.animateScrollTo(emailInput.value.length)
                }
            },
            modifier = Modifier,
            keyboardActions = KeyboardActions(onNext = {focusManager.moveFocus(FocusDirection.Down)})
        )

        EmailTextField(
            label = "닉네임",
            value = nickname.value,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            onValueChanged = {
                coroutineScope.launch {
                    authViewModel.registerNicknameFlow.emit(it)
                    scrollState.animateScrollTo(nickname.value.length)
                }
            },
            modifier = Modifier,
            keyboardActions = KeyboardActions(onNext = {focusManager.moveFocus(FocusDirection.Down)})
        )


        PasswordTextField(
            label = "비밀번호",
            value = passwordInput.value,
            onValueChanged = {
                coroutineScope.launch {
                    authViewModel.registerPasswordInputFlow.emit(it)
                    scrollState.animateScrollTo(passwordInput.value.length)
                }
            },
            modifier = Modifier,
            keyboardActions = KeyboardActions(onNext = {focusManager.moveFocus(FocusDirection.Down)})
        )

       PasswordTextField(
            label = "비밀번호 확인",
            value = passwordConfirmInput.value,
            onValueChanged = {
                coroutineScope.launch {
                    authViewModel.registerPasswordConfirmInputFlow.emit(it)
                    scrollState.animateScrollTo(passwordConfirmInput.value.length)
                }
            },
            modifier = Modifier,
            keyboardActions = KeyboardActions(onNext = {focusManager.moveFocus(FocusDirection.Down)})
        )


        BaseButton(
            modifier = Modifier,
            title = "회원가입",
            enabled = isRegisterBtnActive,
            isLoading = isLoading.value,
            onClick = {
                Log.d("회원가입화면", "회원가입 클릭")
                coroutineScope.launch {
                    focusManager.clearFocus()
                    authViewModel.registerUser()
                    authViewModel.registerIsLoadingFlow.value = true

                    authViewModel.registerSuccessFlow.collectLatest {
                        if(registerSuccess.value) {
                            routeAction.navTo(AuthRoute.LOGIN)
                        }
                    }



                }

            })


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = "이미 계정이 있으신가요?")
            TextButton(onClick = { routeAction.navTo(AuthRoute.LOGIN) }) {
                Text(text = "로그인 하러가기")

            }
        }
    }

    Spacer(modifier = Modifier.height(30.dp))

}





