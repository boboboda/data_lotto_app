package com.bobo.data_lotto_app.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.DrawerValue
import androidx.compose.material.ModalDrawer
import androidx.compose.material.TextButton
import androidx.compose.material.rememberDrawerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.bobo.data_lotto_app.components.NoticeBar
import com.bobo.data_lotto_app.R
import com.bobo.data_lotto_app.ViewModel.DataViewModel
import com.bobo.data_lotto_app.ViewModel.MainViewModel
import com.bobo.data_lotto_app.extentions.toWon
import com.bobo.data_lotto_app.ui.theme.MainFirstBackgroundColor
import com.bobo.data_lotto_app.ui.theme.MainMenuBarColor
import kotlinx.coroutines.launch


@Composable
fun MainScreen(
    mainViewModel: MainViewModel,
    dataViewModel: DataViewModel) {

    val drawerState = rememberDrawerState(DrawerValue.Closed)

    val coroutineScope = rememberCoroutineScope()

    val resentLottoNumber = dataViewModel.resentLottoNumber.collectAsState()

    ModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerCustom()
        }) {
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
                    painter = painterResource(id = R.drawable.user_circle_icon), contentDescription = ""
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
            NoticeBar(mainViewModel = mainViewModel, modifier = Modifier.weight(0.2f))

            Spacer(modifier = Modifier.height(5.dp))

            NoticeContent(
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
    ballValue: Int
) {

    Box(
        modifier = Modifier
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
    modifier: Modifier
) {
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
            Row(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(10.dp))
                    .height(40.dp)
                    .fillMaxWidth(0.9f)
                    .background(MainMenuBarColor),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = "리스트 내용물 1"
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(10.dp))
                    .height(40.dp)
                    .fillMaxWidth(0.9f)
                    .background(MainMenuBarColor),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = "리스트 내용물 2"
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(10.dp))
                    .height(40.dp)
                    .fillMaxWidth(0.9f)
                    .background(MainMenuBarColor),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = "리스트 내용물 3"
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(10.dp))
                    .height(40.dp)
                    .fillMaxWidth(0.9f)
                    .background(MainMenuBarColor),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = "리스트 내용물 4"
                )
            }

        }

    }


}


@Composable
fun DrawerCustom() {
    Column(
        modifier = Modifier
            .fillMaxSize()
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


            androidx.compose.material.Text(text = "email: kju9038@Naver.com")

            Spacer(modifier = Modifier.weight(1f))

            TextButton(onClick = {


            }) {

                androidx.compose.material.Text(text = "로그아웃")
            }
        }


        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        )

        Column(horizontalAlignment = Alignment.Start) {


            Button(
                modifier = Modifier
                    .wrapContentSize(),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                elevation = ButtonDefaults.elevation(0.dp),
                onClick = {

                }) {
                androidx.compose.material.Text(text = "내 글 보기")
            }

            Button(
                modifier = Modifier
                    .wrapContentSize(),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                elevation = ButtonDefaults.elevation(0.dp),
                onClick = {

                }) {
                androidx.compose.material.Text(text = "광고 삭제하기")
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            androidx.compose.material.Text(text = "고객센터")
            androidx.compose.material.Text("개발자 이메일: kju9038@gmail.com")
            androidx.compose.material.Text("개발자 유튜브: ")
            androidx.compose.material.Text("문의: 000-0000-0000")
        }
    }
}