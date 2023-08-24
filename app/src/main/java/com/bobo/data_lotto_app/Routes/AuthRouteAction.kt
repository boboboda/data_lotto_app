package com.bobo.data_lotto_app.Routes

import androidx.navigation.NavHostController

enum class AuthRoute(val routeName: String){
    LOGIN("LOGIN"),
    REGISTER("REGISTER"),
    PASSWORD("PASSWORD"),
    WELCOME("WELCOME")
}


// 인증 관련 화면 라우트 액션
class AuthRouteAction(navHostController: NavHostController) {


    //특정 라우트로 이동
    val navTo: (AuthRoute) -> Unit = { authRoute ->
        navHostController.navigate(authRoute.routeName) {
            popUpTo(authRoute.routeName){ inclusive = true }

        }
    }

    // 뒤로가기 이동
    val goBack: () -> Unit = {
        navHostController.navigateUp()
    }

}