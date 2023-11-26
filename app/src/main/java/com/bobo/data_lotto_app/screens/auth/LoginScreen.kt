package com.bobo.data_lotto_app.screens.auth

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bobo.data_lotto_app.MainActivity
import com.bobo.data_lotto_app.MainActivity.Companion.TAG
import com.bobo.data_lotto_app.MainRoute
import com.bobo.data_lotto_app.MainRouteAction
import com.bobo.data_lotto_app.R
import com.bobo.data_lotto_app.Routes.AuthRoute
import com.bobo.data_lotto_app.Routes.AuthRouteAction
import com.bobo.data_lotto_app.ViewModel.AuthViewModel
import com.bobo.data_lotto_app.components.BaseButton
import com.bobo.data_lotto_app.components.Buttons
import com.bobo.data_lotto_app.components.EmailTextField
import com.bobo.data_lotto_app.components.LoadingDialog
import com.bobo.data_lotto_app.components.LogInBackButton
import com.bobo.data_lotto_app.components.PasswordTextField
import com.bobo.data_lotto_app.components.fontFamily
import com.bobo.data_lotto_app.ui.theme.TextButtonColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(authViewModel: AuthViewModel,
                routeAction: AuthRouteAction,
                activity: Activity) {

    val coroutineScope = rememberCoroutineScope()

    val focusManager = LocalFocusManager.current

    val emailInput = authViewModel.logInEmailInputFlow.collectAsState()

    val passwordInput = authViewModel.logInPasswordInputFlow.collectAsState()

    val isLoginBtnActive = emailInput.value.isNotEmpty() && passwordInput.value.isNotEmpty()

    val logInIsLoading = authViewModel.isLoadingFlow.collectAsState()

    val kakaoLogInIsLoading = authViewModel.kakaoisLoadingFlow.collectAsState()

    val failedLogin = authViewModel.failedLogIn.collectAsState()

    val isLoggedIn = authViewModel.isLoggedIn.collectAsState()

    val needAuth = authViewModel.needAuthContext.collectAsState()

    val snackBarHostState = remember { SnackbarHostState() }

    val loadOpenDialog = remember { mutableStateOf(false) }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp)) {
        LogInBackButton(
            modifier = Modifier,
            onClick = {
                coroutineScope.launch {
                    authViewModel.isLoggedIn.emit(false)
                    routeAction.navTo(AuthRoute.WELCOME)
                }
            }
        )
        Text(
            "LOGIN",
            fontSize = 35.sp,
            fontFamily = fontFamily,
            modifier = Modifier.padding(top = 20.dp, bottom = 30.dp)
        )

        EmailTextField(
            label = "이메일",
            value = emailInput.value,
            onValueChanged = {
                coroutineScope.launch {
                    authViewModel.logInEmailInputFlow.emit(it)
                }
            },
            keyboardActions = KeyboardActions(onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            }),
            modifier = Modifier
        )

        Spacer(modifier = Modifier.height(30.dp))

        PasswordTextField(
            label = "비밀번호",
            value = passwordInput.value,
            onValueChanged = {
                coroutineScope.launch {
                    authViewModel.logInPasswordInputFlow.emit(it)
                }
            },
            keyboardActions = KeyboardActions(onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            }),
            modifier = Modifier
        )


        Spacer(modifier = Modifier.height(30.dp))


        BaseButton(
            title = "로그인",
            enabled = isLoginBtnActive,
            isLoading = logInIsLoading.value,
            onClick = {
                Log.d(TAG, "로딩오픈 다이로그 확인 ${loadOpenDialog.value}")
                focusManager.clearFocus()
                coroutineScope.launch {
                    authViewModel.isLoadingFlow.value = true
                    loadOpenDialog.value = true
                    delay(2000)
                    authViewModel.loginUser(type = AuthViewModel.LoginType.DEFAULT)
                    authViewModel.failedLogIn.collectLatest {
                        if (failedLogin.value == true) {
                            authViewModel.isLoadingFlow.value = false
                            loadOpenDialog.value = false
                            snackBarHostState.showSnackbar(
                                "로그인에 실패하였습니다. 다시 확인해주세요",
                                actionLabel = "닫기", SnackbarDuration.Short
                            )
                            authViewModel.failedLogIn.value = false

                        }
                    }

                }
               loadOpenDialog.value = false
            },
            modifier = Modifier.imePadding())

        Spacer(modifier = Modifier.height(15.dp))


        Row(
            modifier = Modifier
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Divider(modifier = Modifier.weight(1f))
            
            Text(modifier = Modifier
                .padding(horizontal = 10.dp),
                text = "Or Login with")

            Divider(modifier = Modifier.weight(1f))

        }

        Spacer(modifier = Modifier.height(15.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            BaseButton(
                title = "카카오 로그인",
                isLoading = kakaoLogInIsLoading.value,
                image = R.drawable.kakaotalk_icon,
                onClick = {
                    focusManager.clearFocus()
                    coroutineScope.launch {
                        loadOpenDialog.value = true
                        authViewModel.kakaoisLoadingFlow.value = true
                        delay(2000)
                        authViewModel.loginUser(type = AuthViewModel.LoginType.KAKAO, activity)

                        authViewModel.failedLogIn.collectLatest {
                            if (failedLogin.value == true) {
                                authViewModel.kakaoisLoadingFlow.value = false
                                snackBarHostState.showSnackbar(
                                    "로그인에 실패하였습니다. 다시 확인해주세요",
                                    actionLabel = "닫기", SnackbarDuration.Short
                                )
                                authViewModel.failedLogIn.value = false
                            }

                        }


                        loadOpenDialog.value = false
                    }
                    Log.d("웰컴스크린", "로그인 버튼 클릭")


                },
                modifier = Modifier.imePadding())



        }


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = "계정이 없으신가요?")
            TextButton(onClick = { routeAction.navTo(AuthRoute.REGISTER)}) {
                Text(text = "회원가입 하러가기", color = TextButtonColor)
            }

        }

        Spacer(modifier = Modifier.weight(1f))
            SnackbarHost(hostState = snackBarHostState, modifier = Modifier,
                snackbar = { snackbarData ->


                        Card(
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(2.dp, Color.Black),
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(text = snackbarData.message)

                                Spacer(modifier = Modifier.width(10.dp))

                                Card(
                                    modifier = Modifier.wrapContentSize(),
                                    onClick = {
                                    snackBarHostState.currentSnackbarData?.dismiss()
                                }) {
                                    Text(
                                        modifier = Modifier.padding(8.dp),
                                        text = "닫기")
                                }
                            }
                        }



                }

            )

        if(loadOpenDialog.value) {
            LoadingDialog()
        }

        Spacer(modifier = Modifier.weight(0.5f))

    }

}