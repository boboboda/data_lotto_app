package com.bobo.data_lotto_app.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bobo.data_lotto_app.ViewModel.DataViewModel
import com.bobo.data_lotto_app.ViewModel.MainViewModel
import com.bobo.data_lotto_app.ui.theme.MainSelectButtonColor
import com.bobo.data_lotto_app.ui.theme.MainUnSelectButtonColor
import androidx.compose.ui.graphics.Color
import com.bobo.data_lotto_app.MainActivity.Companion.TAG
import com.bobo.data_lotto_app.ui.theme.DataSelectFirstColor
import com.bobo.data_lotto_app.ui.theme.DataSelectSecondColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoticeBar(mainViewModel: MainViewModel, modifier: Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {

        val currentId = mainViewModel.mainNoticeCardValue.collectAsState()


        CardButtonCustom(
            title = "공지사항",
            id = 1,
            selectedValue = currentId.value,
            modifier = Modifier.width(130.dp),
            clickedAction = {
                mainViewModel.mainNoticeCardValue.value = it
            },
            textModifier = Modifier.padding(vertical = 3.dp),
            firstButtonColor = MainSelectButtonColor,
            secondButtonColor = MainUnSelectButtonColor
        )


        Spacer(modifier = Modifier.width(10.dp))

        CardButtonCustom(
            title = "자랑 글",
            id = 2,
            selectedValue = currentId.value,
            modifier = Modifier.width(130.dp),
            clickedAction = {
                mainViewModel.mainNoticeCardValue.value = it
            },
            textModifier = Modifier.padding(vertical = 3.dp),
            firstButtonColor = MainSelectButtonColor,
            secondButtonColor = MainUnSelectButtonColor
        )
    }
}


@Composable
fun SelectCard(dataViewModel: DataViewModel) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center
    ) {

        val currentId = dataViewModel.dataCardId.collectAsState()


        CardButtonCustom(
            title = "빅데이터 조회",
            id = 1,
            selectedValue = currentId.value,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp),
            clickedAction = {
                dataViewModel.dataCardId.value = it
            },
            fontSize = 20.sp,
            textModifier = Modifier.padding(vertical = 5.dp),
            firstButtonColor = DataSelectFirstColor,
            secondButtonColor = DataSelectSecondColor
        )


        Spacer(modifier = Modifier.width(10.dp))

        CardButtonCustom(
            title = "내 번호 조회",
            id = 2,
            selectedValue = currentId.value,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp),
            clickedAction = {
                dataViewModel.dataCardId.value = it
            },
            fontSize = 20.sp,
            textModifier = Modifier.padding(vertical = 5.dp),
            firstButtonColor = DataSelectFirstColor,
            secondButtonColor = DataSelectSecondColor
        )
    }
}


@Composable
fun LottoSelectCard(dataViewModel: DataViewModel) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center
    ) {

        val currentId = dataViewModel.lottoCardId.collectAsState()


        CardButtonCustom(
            title = "일반 모드",
            id = 1,
            selectedValue = currentId.value,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp),
            clickedAction = {
                Log.d(TAG, "dataCard: $it")
                dataViewModel.lottoCardId.value = it
            },
            fontSize = 20.sp,
            textModifier = Modifier.padding(vertical = 5.dp),
            firstButtonColor = DataSelectFirstColor,
            secondButtonColor = DataSelectSecondColor
        )


        Spacer(modifier = Modifier.width(10.dp))

        CardButtonCustom(
            title = "빅데이터 모드",
            id = 2,
            selectedValue = currentId.value,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp),
            clickedAction = {
                Log.d(TAG, "dataCard: $it")

                dataViewModel.lottoCardId.value = it
            },
            fontSize = 20.sp,
            textModifier = Modifier.padding(vertical = 5.dp),
            firstButtonColor = DataSelectFirstColor,
            secondButtonColor = DataSelectSecondColor
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardButtonCustom(
    title: String,
    id: Int,
    modifier: Modifier,
    selectedValue: Int,
    fontSize: TextUnit = 25.sp,
    textModifier: Modifier,
    firstButtonColor: Color,
    secondButtonColor: Color,
    clickedAction: (Int) -> Unit
) {


    val currentCardId: Int = id

    val buttonColor =
        if (selectedValue == currentCardId) firstButtonColor else secondButtonColor

    Card(
        onClick = { clickedAction(currentCardId) },
        colors = CardDefaults.cardColors(buttonColor),
        modifier = modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center){
            Text(
                modifier = textModifier
                    .padding(top = 3.dp, bottom = 3.dp)
                    .wrapContentSize(),
                text = title,
                fontSize = fontSize,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }


    }
}