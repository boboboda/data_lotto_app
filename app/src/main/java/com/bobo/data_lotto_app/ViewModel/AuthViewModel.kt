package com.bobo.data_lotto_app.ViewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class AuthViewModel: ViewModel() {


    val isLoggedIn = MutableStateFlow<Boolean>(false)

    val isLoadingFlow = MutableStateFlow(false)

    val needAuthContext = MutableStateFlow(false)

    // 로그인

    val logInEmailInputFlow = MutableStateFlow("")

    val logInPasswordInputFlow = MutableStateFlow("")

    val failedLogIn = MutableStateFlow(false)

    //회원가입

    val registerEmailInputFlow = MutableStateFlow<String>("")

    val registerNicknameFlow = MutableStateFlow<String>("")

    val registerPasswordInputFlow = MutableStateFlow<String>("")

    val registerPasswordConfirmInputFlow = MutableStateFlow<String>("")

    val registerIsLoadingFlow = MutableStateFlow<Boolean>(false)


}