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

    val mainNoticeCardValue = MutableStateFlow(1)

    val announcementPost = MutableStateFlow<List<Post>>(emptyList())

    val bragPost = MutableStateFlow<List<Post>>(emptyList())

    init {
        getPosts()

    }


    fun getPosts() {
        val dummyList = listOf<Post>(
            Post(
                id = UUID.randomUUID(),
                title = "앱 사용 설명서",
                content = "",
                userNickname = "관리자",
                image = emptyList(),
                comments = emptyList(),
                createAt = LocalDateTime.now(),
                updateAt = LocalDateTime.now()
            ),
            Post(
                id = UUID.randomUUID(),
                title = "이벤트 실시",
                content = "",
                userNickname = "관리자",
                image = emptyList(),
                comments = emptyList(),
                createAt = LocalDateTime.now(),
                updateAt = LocalDateTime.now()
            ),
            Post(
                id = UUID.randomUUID(),
                title = "추가 업데이트 일정",
                content = "",
                userNickname = "관리자",
                image = emptyList(),
                comments = emptyList(),
                createAt = LocalDateTime.now(),
                updateAt = LocalDateTime.now()
            ),
            Post(
                id = UUID.randomUUID(),
                title = "버그 수정",
                content = "",
                userNickname = "관리자",
                image = emptyList(),
                comments = emptyList(),
                createAt = LocalDateTime.now(),
                updateAt = LocalDateTime.now()
            )
        )

        viewModelScope.launch {
            announcementPost.emit(dummyList)
            Log.d(TAG, "dummyList: ${announcementPost.value}")
        }
    }



}