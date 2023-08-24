package com.bobo.data_lotto_app.ViewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel: ViewModel() {

    val nowBottomCardValue = MutableStateFlow(1)

    val mainNoticeCardValue = MutableStateFlow(1)


}