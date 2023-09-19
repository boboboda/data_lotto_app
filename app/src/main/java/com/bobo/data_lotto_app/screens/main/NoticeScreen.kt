package com.bobo.data_lotto_app.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.DrawerValue
import androidx.compose.material.ModalDrawer
import androidx.compose.material.rememberDrawerState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bobo.data_lotto_app.MainRoute
import com.bobo.data_lotto_app.MainRouteAction
import com.bobo.data_lotto_app.R
import com.bobo.data_lotto_app.ViewModel.AuthViewModel
import com.bobo.data_lotto_app.ViewModel.NoticeViewModel
import kotlinx.coroutines.launch

@Composable
fun NoticeScreen(authViewModel: AuthViewModel,
                 noticeViewModel: NoticeViewModel,
                 mainRouteAction: MainRouteAction) {

    val drawerState = rememberDrawerState(DrawerValue.Closed)

    val coroutineScope = rememberCoroutineScope()

    val noticeCardState = noticeViewModel.mainNoticeCardValue.collectAsState()

    val noticeTitle: String = when(noticeCardState.value) {
        1-> { "공지사항" }
        2-> { "자랑글" }
        else -> {""}
    }


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
                    .padding(start = 8.dp, end = 8.dp),
            ) {
                Button(
                    modifier = Modifier.size(40.dp),
                    colors = androidx.compose.material.ButtonDefaults.buttonColors(
                        backgroundColor = Color.White
                    ),
                    onClick = {
                    mainRouteAction.navTo(MainRoute.Main)
                }) {
                    Image(
                        modifier = Modifier,
                        contentScale = ContentScale.FillBounds,
                        painter = painterResource(id = R.drawable.close_icon),
                        contentDescription = ""
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Image(
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            coroutineScope.launch {
                                drawerState.open()
                            }
                        },
                    painter = painterResource(id = R.drawable.list_icon),
                    contentDescription = ""
                )
            }

            Spacer(modifier = Modifier.height(5.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${noticeTitle}",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

    }


}

// 공지사항 리스트 뷰
//// 본문 읽는 부분 댓글 대댓글 구현

// 자랑글 리스트 뷰
// 글쓰기 뷰
// 수정하기 뷰