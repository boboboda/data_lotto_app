package com.bobo.data_lotto_app.ViewModel

import android.util.Log
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bobo.data_lotto_app.MainActivity.Companion.TAG
import com.bobo.data_lotto_app.service.Comment
import com.bobo.data_lotto_app.service.Post
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID

class MainViewModel: ViewModel() {


    val nowBottomCardValue = MutableStateFlow(1)



}