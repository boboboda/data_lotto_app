package com.bobo.data_lotto_app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bobo.data_lotto_app.MainActivity.Companion.TAG
import com.bobo.data_lotto_app.Routes.AuthRoute
import com.bobo.data_lotto_app.Routes.AuthRouteAction
import com.bobo.data_lotto_app.ViewModel.AuthViewModel
import com.bobo.data_lotto_app.ViewModel.DataViewModel
import com.bobo.data_lotto_app.ViewModel.MainViewModel
import com.bobo.data_lotto_app.ViewModel.NoticeViewModel
import com.bobo.data_lotto_app.components.MainBottomBar
import com.bobo.data_lotto_app.components.MainTopBar
import com.bobo.data_lotto_app.components.admob.BannerAd
import com.bobo.data_lotto_app.components.admob.loadInterstitial
import com.bobo.data_lotto_app.screens.auth.LoginScreen
import com.bobo.data_lotto_app.screens.auth.RegisterScreen
import com.bobo.data_lotto_app.screens.auth.WelcomeScreen
import com.bobo.data_lotto_app.screens.main.DataScreen
import com.bobo.data_lotto_app.screens.main.DrawScreen
import com.bobo.data_lotto_app.screens.main.MainScreen
import com.bobo.data_lotto_app.screens.main.NoticeScreen
import com.bobo.data_lotto_app.screens.main.PaymentScreen
import com.bobo.data_lotto_app.ui.theme.Data_lotto_appTheme
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    companion object {
        const val TAG = "메인"
    }

    private val mainViewModel: MainViewModel by viewModels()
    private val dataViewModel: DataViewModel by viewModels()
    private val noticeViewModel: NoticeViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

//            Data_lotto_appTheme {
//                // A surface container using the 'background' color from the theme
//
//            }

            MobileAds.initialize(this)

            loadInterstitial(this)

            val intent = intent

            AppScreen(
                mainViewModel,
                dataViewModel,
                authViewModel,
                noticeViewModel,
                activity = this,
                intent = intent
            )

        }
    }
}


@Composable
fun AppScreen(
    mainViewModel: MainViewModel,
    dataViewModel: DataViewModel,
    authViewModel: AuthViewModel,
    noticeViewModel: NoticeViewModel,
    activity: Activity,
    intent: Intent
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

    val googleEmail = intent.getStringExtra("email")
    val googleLogin = intent.getBooleanExtra("success", false)
//
//    LaunchedEffect(key1 = intent, block = {
//        if(googleLogin) {
//            authViewModel.googleUserDataMake(googleEmail!!)
//            Log.d(TAG, "googleEmail: ${googleEmail}")
//        }
//    })

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
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = it.calculateTopPadding(),
                    bottom = it.calculateBottomPadding()
                )
        ) {

            Column(Modifier.weight(1f)) {
                MainNaHost(
                    mainNavController = mainNavController,
                    mainRouteAction = mainRouteAction,
                    mainViewModel = mainViewModel,
                    dataViewModel = dataViewModel,
                    authViewModel = authViewModel,
                    noticeViewModel = noticeViewModel,
                    activity = activity
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                BannerAd()
            }

            Spacer(modifier = Modifier.height(5.dp))
        }


    }

//    if (needAuth.value) {
//
//
//    } else {
//
//        // 로그인 필요 여부
//        Scaffold(
//            scaffoldState = scaffoldState,
//        ) {
//            Column(modifier = Modifier.padding(
//                top = it.calculateTopPadding(),
//                bottom = it.calculateBottomPadding())
//
//            ) {
//
//                AuthNavHost(
//                    authNavController = authNavController,
//                    routeAction = authRouteAction,
//                    authViewModel = authViewModel,
//                    activity = activity)
//
//            }
//        }
//
//    }

}

@Composable
fun MainNaHost(
    mainNavController: NavHostController,
    startRouter: MainRoute = MainRoute.Main,
    mainRouteAction: MainRouteAction,
    mainViewModel: MainViewModel,
    dataViewModel: DataViewModel,
    authViewModel: AuthViewModel,
    noticeViewModel: NoticeViewModel,
    activity: Activity
) {
    val coroutineScope = rememberCoroutineScope()

    NavHost(
        navController = mainNavController,
        startDestination = startRouter.routeName!!) {
        composable(MainRoute.Main.routeName!!) {
            MainScreen(mainViewModel, dataViewModel, authViewModel, noticeViewModel, mainRouteAction)
        }
        composable(MainRoute.Data.routeName!!) {
            DataScreen(dataViewModel, authViewModel)
        }
        composable(MainRoute.Draw.routeName!!) {
            DrawScreen(dataViewModel, authViewModel)
        }
        composable(MainRoute.Notice.routeName!!) {
            NoticeScreen(authViewModel = authViewModel, noticeViewModel = noticeViewModel, mainRouteAction)
        }

        composable(MainRoute.Payment.routeName!!) {
            PaymentScreen(onPurchaseButtonClicked = {
                MyApplication.instance.billingClientLifecycle.startBillingFlow(activity = activity, productDetails = it)
            })
        }
    }
}


@Composable
fun AuthNavHost(
    authNavController: NavHostController,
    startRouter: AuthRoute = AuthRoute.WELCOME,
    routeAction: AuthRouteAction,
    authViewModel: AuthViewModel,
    activity: Activity,
) {


    NavHost(
        navController = authNavController,
        startDestination = startRouter.routeName) {
        composable(AuthRoute.LOGIN.routeName) {
            LoginScreen(authViewModel = authViewModel, routeAction = routeAction, activity = activity)
        }

        composable(AuthRoute.REGISTER.routeName) {
            RegisterScreen(authViewModel = authViewModel, routeAction = routeAction)
        }

        composable(AuthRoute.WELCOME.routeName) {
            WelcomeScreen(routeAction = routeAction, authViewModel)
        }

    }
}

