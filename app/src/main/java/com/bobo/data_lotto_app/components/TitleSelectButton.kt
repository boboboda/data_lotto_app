package com.bobo.data_lotto_app.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bobo.data_lotto_app.MainActivity.Companion.TAG
import com.bobo.data_lotto_app.ViewModel.DataViewModel
import com.bobo.data_lotto_app.ViewModel.MainViewModel
import com.bobo.data_lotto_app.ViewModel.NoticeViewModel
import com.bobo.data_lotto_app.ui.theme.MainSelectButtonColor
import com.bobo.data_lotto_app.ui.theme.MainUnSelectButtonColor
import com.bobo.data_lotto_app.ui.theme.TopButtonColor
import com.bobo.data_lotto_app.ui.theme.TopButtonInColor

@Composable
fun TopTitleButton(dataViewModel: DataViewModel) {

    val cardState = dataViewModel.dataCardId.collectAsState()

        Row(Modifier.padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            TopTitleView(
                "빅데이터 조회",
                modifier = Modifier.weight(1f),
                1,
                selectedId = cardState.value,
                selectAction = {
                    dataViewModel.dataCardId.value = it
                })

            TopTitleView(
                "내 번호 조회",
                modifier = Modifier.weight(1f),
                2,
                selectedId = cardState.value,
                selectAction = {
                    dataViewModel.dataCardId.value = it
                })
        }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopTitleView(mainText: String,
                 modifier: Modifier,
                 id: Int,
                 selectedId: Int,
                 selectAction: (Int) -> Unit) {

    var currentCardId : Int = id

    var color = if (selectedId == currentCardId) TopButtonInColor else TopButtonColor

    Card(
        colors = CardDefaults.cardColors(color),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = modifier,
        onClick = {
            Log.d(TAG, "클릭되었습니다.")
            selectAction(currentCardId) }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AutoSizeText(
                value = "$mainText",
                modifier = Modifier,
                fontSize = 18.sp,
                maxLines = 2,
                minFontSize = 10.sp,
                color = Color.Black)
        }
    }
}

@Composable
fun LotteryTopTitleButton(dataViewModel: DataViewModel) {

    val cardState = dataViewModel.lottoCardId.collectAsState()

    Row(Modifier.padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)) {
        TopTitleView(
            "일반 모드",
            modifier = Modifier.weight(1f),
            1,
            selectedId = cardState.value,
            selectAction = {
                dataViewModel.lottoCardId.value = it
            })

        TopTitleView(
            "빅데이터 모드",
            modifier = Modifier.weight(1f),
            2,
            selectedId = cardState.value,
            selectAction = {
                dataViewModel.lottoCardId.value = it
            })
    }
}


@Composable
fun MainNoticeTopTitleButton(noticeViewModel: NoticeViewModel) {

    val cardState = noticeViewModel.mainNoticeCardValue.collectAsState()
    Column(
        modifier = Modifier
    ) {
        LazyVerticalGrid(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp),
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            content = {

                item(span = {
                    GridItemSpan(1)
                }
                ) {
                    TopTitleView(
                        "일반 모드",
                        modifier = Modifier.weight(1f),
                        1,
                        selectedId = cardState.value,
                        selectAction = {
                            noticeViewModel.mainNoticeCardValue.value = it
                        })
                }

                item(span = {
                    GridItemSpan(1)
                }
                ) {
                    TopTitleView(
                        "빅데이터 모드",
                        modifier = Modifier.weight(1f),
                        2,
                        selectedId = cardState.value,
                        selectAction = {
                            noticeViewModel.mainNoticeCardValue.value = it
                        })
                }
            })
    }
}
