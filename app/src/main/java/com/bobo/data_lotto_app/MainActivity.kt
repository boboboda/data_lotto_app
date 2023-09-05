package com.bobo.data_lotto_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Scaffold
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bobo.data_lotto_app.MainActivity.Companion.TAG
import com.bobo.data_lotto_app.Routes.AuthRoute
import com.bobo.data_lotto_app.Routes.AuthRouteAction
import com.bobo.data_lotto_app.ViewModel.AuthViewModel
import com.bobo.data_lotto_app.screens.main.DataScreen
import com.bobo.data_lotto_app.components.MainBottomBar
import com.bobo.data_lotto_app.screens.main.MainScreen
import com.bobo.data_lotto_app.components.MainTopBar
import com.bobo.data_lotto_app.screens.auth.WelcomeScreen
import com.bobo.data_lotto_app.ViewModel.DataViewModel
import com.bobo.data_lotto_app.ViewModel.MainViewModel
import com.bobo.data_lotto_app.screens.auth.LoginScreen
import com.bobo.data_lotto_app.screens.auth.RegisterScreen
import com.bobo.data_lotto_app.ui.theme.Data_lotto_appTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow


class MainActivity : ComponentActivity() {

    companion object {
        const val TAG = "메인"
    }

    private val mainViewModel: MainViewModel by viewModels()
    private val dataViewModel: DataViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Data_lotto_appTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppScreen(
                        mainViewModel = mainViewModel,
                        dataViewModel = dataViewModel,
                        authViewModel = authViewModel)

                }
            }
        }
    }
}


@Composable
fun AppScreen(
    mainViewModel: MainViewModel,
    dataViewModel: DataViewModel,
    authViewModel: AuthViewModel,
) {

    val mainNavController = rememberNavController()
    val mainRouteAction = remember(mainNavController) {
        MainRouteAction(mainNavController)
    }

    val mainBackStack = mainNavController.currentBackStackEntryAsState()

    val authNavController = rememberNavController()

    val scaffoldState = rememberScaffoldState()

    val scope = rememberCoroutineScope()

    val isLoggedIn = authViewModel.isLoggedIn.collectAsState()

    val authRouteAction = remember(authNavController) {
        AuthRouteAction(authNavController)
    }

    val needAuth = authViewModel.needAuthContext.collectAsState()


    if (needAuth.value) {

        Scaffold(
            scaffoldState = scaffoldState,
            topBar = { MainTopBar() },
            bottomBar = {
                MainBottomBar(
                    mainRouteAction,
                    mainBackStack.value,
                    mainViewModel = mainViewModel
                )
            }) {
            Column(
                modifier = Modifier.padding(
                    top = it.calculateTopPadding(),
                    bottom = it.calculateBottomPadding()
                )
            ) {
                MainNaHost(
                    mainNavController = mainNavController,
                    mainRouteAction = mainRouteAction,
                    mainViewModel = mainViewModel,
                    dataViewModel = dataViewModel,
                    authViewModel = authViewModel
                )
            }


        }
    } else {

        Scaffold(
            scaffoldState = scaffoldState,
        ) {
            Column(modifier = Modifier.padding(
                top = it.calculateTopPadding(),
                bottom = it.calculateBottomPadding())

            ) {

                AuthNavHost(
                    authNavController = authNavController,
                    routeAction = authRouteAction,
                    authViewModel = authViewModel,

                    mainRouteAction = mainRouteAction)

            }
        }

    }

}

@Composable
fun MainNaHost(
    mainNavController: NavHostController,
    startRouter: MainRoute = MainRoute.Main,
    mainRouteAction: MainRouteAction,
    mainViewModel: MainViewModel,
    dataViewModel: DataViewModel,
    authViewModel: AuthViewModel
) {
    val coroutineScope = rememberCoroutineScope()

    NavHost(
        navController = mainNavController,
        startDestination = startRouter.routeName!!) {
        composable(MainRoute.Main.routeName!!) {
            MainScreen(mainViewModel, dataViewModel, authViewModel)
        }
        composable(MainRoute.Data.routeName!!) {
            DataScreen(dataViewModel)
        }
        composable(MainRoute.Draw.routeName!!) {
            DrawScreen()
        }
    }
}


@Composable
fun AuthNavHost(
    authNavController: NavHostController,
    startRouter: AuthRoute = AuthRoute.WELCOME,
    routeAction: AuthRouteAction,
    authViewModel: AuthViewModel,
    mainRouteAction: MainRouteAction
) {


    NavHost(
        navController = authNavController,
        startDestination = startRouter.routeName) {
        composable(AuthRoute.LOGIN.routeName) {
            LoginScreen(authViewModel = authViewModel, routeAction = routeAction)
        }

        composable(AuthRoute.REGISTER.routeName) {
            RegisterScreen(authViewModel = authViewModel, routeAction = routeAction)
        }

        composable(AuthRoute.WELCOME.routeName) {
            WelcomeScreen(routeAction = routeAction, authViewModel)
        }

    }
}





@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DrawScreen() {



    val viewStateValue = remember { mutableStateOf(0) }



Column(modifier = Modifier.fillMaxSize()) {




}

}



@Composable
fun LoadingDialog(
) {

    val isFirstMoving = remember { mutableStateOf(false) }

    val isSecondMoving = remember { mutableStateOf(false) }

    val isThreeMoving = remember { mutableStateOf(false) }

    val isFourMoving = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        isFirstMoving.value = true
        delay(200)
        isSecondMoving.value = true
        delay(200)
        isThreeMoving.value = true
        delay(200)
        isFourMoving.value = true
    }


    val ballAnimate = animateIntAsState(
        targetValue = if(isFirstMoving.value) 80 else 0 ,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse))

    val redBallAnimate = animateIntAsState(
        targetValue = if(isSecondMoving.value) 80 else 0 ,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse))

    val grayBallAnimate = animateIntAsState(
        targetValue = if(isThreeMoving.value) 80 else 0 ,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse))

    val greenBallAnimate = animateIntAsState(
        targetValue = if(isFourMoving.value) 80 else 0 ,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse))




    Column(modifier = Modifier.fillMaxSize()) {

        Row(modifier = Modifier
            .weight(1f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.blue_ball),
                contentDescription = "C",
                modifier = Modifier
                    .size(50.dp)
                    .offset(
                        x = 100.dp,
                        y = ballAnimate.value.dp
                    )
            )

            Spacer(modifier = Modifier.width(5.dp))

            Image(
                painter = painterResource(id = R.drawable.red_ball),
                contentDescription = "C",
                modifier = Modifier
                    .size(50.dp)
                    .offset(
                        x = 100.dp,
                        y = redBallAnimate.value.dp
                    )
            )

            Spacer(modifier = Modifier.width(5.dp))

            Image(
                painter = painterResource(id = R.drawable.gray_ball),
                contentDescription = "C",
                modifier = Modifier
                    .size(50.dp)
                    .offset(
                        x = 100.dp,
                        y = grayBallAnimate.value.dp
                    )
            )

            Spacer(modifier = Modifier.width(5.dp))

            Image(
                painter = painterResource(id = R.drawable.green_ball),
                contentDescription = "C",
                modifier = Modifier
                    .size(50.dp)
                    .offset(
                        x = 100.dp,
                        y = greenBallAnimate.value.dp
                    )
            )

        }

        Row(modifier = Modifier.weight(1f)) {
        }
    }

}

