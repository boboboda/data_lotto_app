package com.bobo.data_lotto_app


import androidx.navigation.NavHostController
import com.bobo.data_lotto_app.R



sealed class MainRoute(
    open val routeName: String? = null,
    open val title: String? = null,
    open val selectValue: Int? = null,
    open val iconResId: Int? = null
) {
    object Main: MainRoute("Main", "홈", 1 , R.drawable.home_icon)
    object Data: MainRoute("Data","데이터",2, R.drawable.data_icon)
    object Draw: MainRoute("Draw","뽑기", 3, R.drawable.draw_lotto_icon)
}


// 메인 관련 화면 라우트 액션
class MainRouteAction(navHostController: NavHostController) {

    //특정 라우트로 이동
    val navTo: (MainRoute) -> Unit = { Route ->
        navHostController.navigate(Route.routeName!!) {
            popUpTo(Route.routeName!!){ inclusive = true }
        }
    }

    // 뒤로 가기 이동
    val goBack: () -> Unit = {
        navHostController.navigateUp()
    }

}