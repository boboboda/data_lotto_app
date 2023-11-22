package com.bobo.data_lotto_app.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.DrawerValue
import androidx.compose.material.ModalDrawer
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.TextButton
import androidx.compose.material.rememberDrawerState
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bobo.data_lotto_app.MainRoute
import com.bobo.data_lotto_app.MainRouteAction
import com.bobo.data_lotto_app.R
import com.bobo.data_lotto_app.ViewModel.AuthViewModel
import com.bobo.data_lotto_app.ViewModel.DataViewModel
import com.bobo.data_lotto_app.ViewModel.MainViewModel
import com.bobo.data_lotto_app.ViewModel.NoticeViewModel
import com.bobo.data_lotto_app.components.BaseButton
import com.bobo.data_lotto_app.components.CustomButton
import com.bobo.data_lotto_app.components.MainNoticeTopTitleButton
import com.bobo.data_lotto_app.components.fontFamily
import com.bobo.data_lotto_app.extentions.toWon
import com.bobo.data_lotto_app.service.Post
import com.bobo.data_lotto_app.ui.theme.MainFirstBackgroundColor
import com.bobo.data_lotto_app.ui.theme.MainMenuBarColor
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch



@Composable
fun MainScreen(
    mainViewModel: MainViewModel,
    dataViewModel: DataViewModel,
    authViewModel: AuthViewModel,
    noticeViewModel: NoticeViewModel,
    mainRouteAction: MainRouteAction) {

    val drawerState = rememberDrawerState(DrawerValue.Closed)

    val coroutineScope = rememberCoroutineScope()

    val resentLottoNumber = dataViewModel.resentLottoNumber.collectAsState()

    val lastWeekLottoNumber = dataViewModel.lastWeekLottoNumber.collectAsState()

    val noticeCardState = noticeViewModel.mainNoticeCardValue.collectAsState()

    val announcementPost = noticeViewModel.announcementPost.collectAsState()

    val bragPost = noticeViewModel.bragPost.collectAsState()

    val failLogin = authViewModel.failedLogIn.collectAsState()

    val snackBarHostState = remember { SnackbarHostState() }

    ModalDrawer(
        drawerState = drawerState,
        drawerShape = customShape(),
        drawerContent = {
            DrawerCustom(authViewModel, mainRouteAction)
                        },

        ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray)
        ) {

            Spacer(modifier = Modifier.height(5.dp))

            // 마이페이지
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Image(
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            coroutineScope.launch {
                                drawerState.open()
                            }
                        },
                    painter = painterResource(id = R.drawable.list_icon), contentDescription = ""
                )
            }

            Spacer(modifier = Modifier.height(5.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "이번주 당첨번호  ${resentLottoNumber.value.drwNo}회",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            LottoRowView(
                oneBall = resentLottoNumber.value.drwtNo1!!.toInt(),
                twoBall = resentLottoNumber.value.drwtNo2!!.toInt(),
                threeBall = resentLottoNumber.value.drwtNo3!!.toInt(),
                fourBall = resentLottoNumber.value.drwtNo4!!.toInt(),
                fiveBall = resentLottoNumber.value.drwtNo5!!.toInt(),
                sixBall = resentLottoNumber.value.drwtNo6!!.toInt(),
                bonusBall = resentLottoNumber.value.bnusNo!!.toInt(),
                totalMoney = resentLottoNumber.value.totSellamnt!!,
                firstWinamnt = resentLottoNumber.value.firstWinamnt!!,
                firstPrzwnerCo = resentLottoNumber.value.firstPrzwnerCo!!.toInt(),
                modifier = Modifier.weight(1f)

            )

            // 게시판 뷰
//            MainNoticeTopTitleButton(noticeViewModel = noticeViewModel)
//
//            Spacer(modifier = Modifier.height(15.dp))
//
//            when(noticeCardState.value) {
//                1 -> {
//                    NoticeContent(
//                        modifier = Modifier.weight(1f),
//                        announcementPost.value,
//                        clicked = {
//
//                        }
//                    )
//                }
//                2 -> {
//                    NoticeContent(
//                        modifier = Modifier.weight(1f),
//                        bragPost.value,
//                        clicked = {
//
//                        }
//                    )
//                }
//            }

            // 게시판 뷰 보류


            Spacer(modifier = Modifier.height(5.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "저번주 당첨번호  ${lastWeekLottoNumber.value.drwNo}회",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            LottoRowView(
                oneBall = lastWeekLottoNumber.value.drwtNo1!!.toInt(),
                twoBall = lastWeekLottoNumber.value.drwtNo2!!.toInt(),
                threeBall = lastWeekLottoNumber.value.drwtNo3!!.toInt(),
                fourBall = lastWeekLottoNumber.value.drwtNo4!!.toInt(),
                fiveBall = lastWeekLottoNumber.value.drwtNo5!!.toInt(),
                sixBall = lastWeekLottoNumber.value.drwtNo6!!.toInt(),
                bonusBall = lastWeekLottoNumber.value.bnusNo!!.toInt(),
                totalMoney = lastWeekLottoNumber.value.totSellamnt!!,
                firstWinamnt = lastWeekLottoNumber.value.firstWinamnt!!,
                firstPrzwnerCo = lastWeekLottoNumber.value.firstPrzwnerCo!!.toInt(),
                modifier = Modifier.weight(1f)

            )
        }
    }




}


@Composable
fun LottoRowView(
    oneBall: Int,
    twoBall: Int,
    threeBall: Int,
    fourBall: Int,
    fiveBall: Int,
    sixBall: Int,
    bonusBall: Int,
    totalMoney: String,
    firstWinamnt: String,
    firstPrzwnerCo: Int,
    modifier: Modifier


) {

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(start = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            BallDraw(ballOder = "첫번째 볼", ballValue = oneBall)

            BallDraw(ballOder = "두번째 볼", ballValue = twoBall)

            BallDraw(ballOder = "세번째 볼", ballValue = threeBall)

            BallDraw(ballOder = "네번째 볼", ballValue = fourBall)

            BallDraw(ballOder = "다섯번째 볼", ballValue = fiveBall)

            BallDraw(ballOder = "여섯번째 볼", ballValue = sixBall)

            Image(
                modifier = Modifier
                    .size(20.dp)
                    .padding(bottom = 3.dp),
                painter = painterResource(id = R.drawable.plus_icon), contentDescription = ""
            )

            Spacer(modifier = Modifier.width(13.dp))

            BallDraw(ballOder = "보너스 볼", ballValue = bonusBall)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            Modifier
                .clip(shape = RoundedCornerShape(10.dp))
                .background(MainFirstBackgroundColor)
                .fillMaxWidth(0.9f)
                .fillMaxSize(0.9f)
                .padding(top = 20.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(10.dp))
                    .fillMaxWidth(0.9f)
                    .weight(1f)
                    .background(MainMenuBarColor),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = "누적금액: ${totalMoney.toLong().toWon()}원 "
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(10.dp))
                    .fillMaxWidth(0.9f)
                    .weight(1f)
                    .background(MainMenuBarColor),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = "1등 당첨금액: ${firstWinamnt.toLong().toWon()}원"
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(10.dp))
                    .fillMaxWidth(0.9f)
                    .weight(1f)
                    .background(MainMenuBarColor),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = "1등 당첨 인원: ${firstPrzwnerCo} 명"
                )
            }

        }


    }


}


@Composable
fun BallDraw(
    ballOder: String,
    ballValue: Int,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .size(45.dp),
        contentAlignment = Alignment.Center
    ) {

        val ballImage =

            when (ballValue) {

                in 1..9 -> {
                    painterResource(id = R.drawable.yellow_ball)
                }

                in 10..19 -> {
                    painterResource(id = R.drawable.blue_ball)
                }

                in 20..29 -> {
                    painterResource(id = R.drawable.red_ball)
                }

                in 30..39 -> {
                    painterResource(id = R.drawable.gray_ball)
                }

                in 40..45 -> {
                    painterResource(id = R.drawable.green_ball)
                }

                else -> {
                    null
                }
            }


        Image(
            modifier = Modifier.size(40.dp),
            painter = ballImage!!,
            contentDescription = "image description",
//                    contentScale = ContentScale.None
        )

        Text(
            modifier = Modifier.padding(bottom = 5.dp, end = 6.dp),
            text = ballValue.toString(),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }

}




@Composable
fun NoticeContent(
    modifier: Modifier,
    posts: List<Post>,
    clicked: () -> Unit
) {
    val lazyState = rememberLazyListState()

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            Modifier
                .clip(shape = RoundedCornerShape(10.dp))
                .background(MainFirstBackgroundColor)
                .fillMaxWidth(0.9f)
                .wrapContentHeight()
                .padding(top = 20.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 리스트 값으로 수정
            LazyColumn(
                state = lazyState,
                modifier = Modifier.wrapContentSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ){
                items(posts, {item -> item.id}) { posts ->

                    NoticeArray(title = posts.title,
                        clicked = {clicked.invoke()})
                }
            }
        }

    }


}

@Composable
fun NoticeArray(title: String,
                clicked: () -> Unit) {
    Row(
        modifier = Modifier
            .clickable { clicked.invoke() }
            .clip(shape = RoundedCornerShape(10.dp))
            .height(40.dp)
            .fillMaxWidth(0.9f)
            .background(MainMenuBarColor),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(start = 10.dp),
            text = title
        )
    }
}


@Composable
fun DrawerCustom(authViewModel: AuthViewModel, mainRouteAction: MainRouteAction) {

    val isSignIn = authViewModel.isLoggedIn.collectAsState()

    val userdata = authViewModel.receiveUserDataFlow.collectAsState()

    val needAuth = authViewModel.needAuthContext.collectAsState()

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .requiredWidth(250.dp)
            .padding(start = 5.dp)
    ) {
        Spacer(
            modifier = Modifier
                .height(10.dp)
        )

        Row(
            modifier = Modifier
                .wrapContentSize()
                .padding(start = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            if(isSignIn.value) {

                Text(text = "${userdata.value.email}")

                Spacer(modifier = Modifier.weight(1f))

                TextButton(onClick = {
                    scope.launch {
                        authViewModel.isLoggedIn.emit(false)
                        authViewModel.needAuthContext.emit(false)
                    }


                }) {
                    androidx.compose.material.Text(text = "로그아웃")
                }

            } else {

                Row(modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 10.dp)) {

                    CustomButton(onClick = {authViewModel.needAuthContext.value = false},
                        composable = {
                            Text(
                                text = "LOTTO",
                                fontSize = 23.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = fontFamily,
                                color = Color.Black)

                            Spacer(modifier = Modifier.width(10.dp))

                            Text(
                                text = "로그인",
                                fontSize = 18.sp,
                                fontFamily = fontFamily,
                                color = Color.Black)
                        }
                    )
                }
            }
        }

        if(isSignIn.value) {
            Column(modifier = Modifier
                .wrapContentSize()
                .padding(start = 10.dp)) {

                Text(text = "범위 검색 횟수: ${userdata.value.allNumberSearchCount}")

                Text(text = "나의 번호 조회 횟수: ${userdata.value.myNumberSearchCount}")

                Text(text = "빅데이터 로또번호 추첨 횟수: ${userdata.value.numberLotteryCount}")

            }
        } else {
            Column(modifier = Modifier.wrapContentSize()) {

            }
        }


        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        )

        Column(horizontalAlignment = Alignment.Start) {


//            Text(modifier = Modifier
//                .padding(start = 8.dp, bottom = 5.dp)
//                .clickable {
//
//                }, text = "내 글 보기")

//            Spacer(modifier = Modifier.height(5.dp))
//
//            Text(modifier = Modifier
//                .padding(start = 8.dp, bottom = 5.dp)
//                .clickable {
//
//                }, text = "광고 삭제하기")

            TextButton(onClick = {
                if(isSignIn.value) {
                    mainRouteAction.navTo.invoke(MainRoute.Payment)
                } else {
                    authViewModel.needAuthContext.value = false
                }

            }) {
                Text(text = "광고 삭제")
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
            )

//            Spacer(modifier = Modifier.height(2.dp))
//
//            Text(modifier = Modifier.padding(start = 10.dp),fontSize = 20.sp,text = "게시판")
//
//            Spacer(modifier = Modifier.height(5.dp))
//
//            Text(modifier = Modifier
//                .padding(start = 20.dp, bottom = 5.dp)
//                .clickable {
//                    mainRouteAction.navTo(MainRoute.Notice)
//                }, text = "공지사항")
//
//            Spacer(modifier = Modifier.height(2.dp))
//
//            Text(modifier = Modifier
//                .padding(start = 20.dp, bottom = 5.dp)
//                .clickable {
//
//                }, text = "자랑글")


            Spacer(modifier = Modifier.weight(1f))

            androidx.compose.material.Text(text = "고객센터")
            androidx.compose.material.Text("개발자 이메일: kju9038@gmail.com")
            androidx.compose.material.Text("개발자 유튜브: ")
            androidx.compose.material.Text("문의: 000-0000-0000")
        }
    }
}

@Composable
fun customShape() = object : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Rectangle(
            Rect(
                left = 0f,
                top = 0f,
                right = size.width * 2.3f / 3,
                bottom = size.height
            )
        )
    }
}

