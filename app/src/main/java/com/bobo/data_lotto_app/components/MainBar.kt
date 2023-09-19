package com.bobo.data_lotto_app.components


import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import com.bobo.data_lotto_app.MainRoute
import com.bobo.data_lotto_app.MainRouteAction
import com.bobo.data_lotto_app.R
import com.bobo.data_lotto_app.ViewModel.MainViewModel
import com.bobo.data_lotto_app.ui.theme.TopAppBarColor


@Composable
fun MainTopBar() {

    TopAppBar(
        modifier = Modifier.height(40.dp),
        title = {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {

            Row(Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {

                Image(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(shape = CircleShape),
                    painter = painterResource(id = R.drawable.topbar_icon), contentDescription = "")

                Text(
                    text = "빅데이터 기반 로또번호 추천",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
//                fontFamily = fontFamily,
                    textAlign = TextAlign.Center
                )
            }

        }
    },
        backgroundColor = TopAppBarColor
    )
}


@SuppressLint("SuspiciousIndentation")
@Composable
fun MainBottomBar(
    mainRouteAction: MainRouteAction,
    mainRouteBackStack: NavBackStackEntry?,
    mainViewModel: MainViewModel
) {

    val nowBottomValue = mainViewModel.nowBottomCardValue.collectAsState()

    val snackBarHostState = remember { SnackbarHostState() }

    val coroutineScope = rememberCoroutineScope()



    BottomNavigation(
        modifier = Modifier.fillMaxWidth()
    ) {
        MainRoute.Main.let {
            BottomNavigationItem(
                modifier = Modifier.background(Color.White),
                label = { Text(text = it.title!!) },
                icon = {
                    it.iconResId?.let { iconId ->
                        Icon(painter = painterResource(iconId), contentDescription = it.title)
                    }
                },
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.LightGray,
                selected = (mainRouteBackStack?.destination?.route) == it.routeName,
                onClick = {
                    mainRouteAction.navTo(it)
                    mainViewModel.nowBottomCardValue.value = it.selectValue!!
                },
            )
        }

        MainRoute.Data.let {
            BottomNavigationItem(modifier = Modifier.background(Color.White),
                label = { Text(text = it.title!!) },
                icon = {
                    it.iconResId?.let { iconId ->
                        Icon(painter = painterResource(iconId), contentDescription = it.title)
                    }
                },
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.LightGray,
                selected = (mainRouteBackStack?.destination?.route) == it.routeName,
                onClick = {
                    mainRouteAction.navTo(it)
                    mainViewModel.nowBottomCardValue.value = it.selectValue!!
                }

            )
        }

        MainRoute.Draw.let {
            it.selectValue
            BottomNavigationItem(modifier = Modifier.background(Color.White),
                label = { Text(text = it.title!!) },
                icon = {
                    it.iconResId?.let { iconId ->
                        Icon(painter = painterResource(iconId), contentDescription = it.title)
                    }
                },
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.LightGray,
                selected = (mainRouteBackStack?.destination?.route) == it.routeName,
                onClick = {
                    mainRouteAction.navTo(it)
                    mainViewModel.nowBottomCardValue.value = it.selectValue!!
                }
            )
        }

//        SnackbarHost(hostState = snackBarHostState, modifier = Modifier)


    }
}