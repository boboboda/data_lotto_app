package com.bobo.data_lotto_app.service

import android.provider.ContactsContract.CommonDataKinds.Nickname
import androidx.compose.ui.graphics.vector.ImageVector
import com.squareup.moshi.Json
import java.time.LocalDateTime
import java.util.UUID

//data class PostResponse (
//    val message: String? = null,
//    @Json(name = "data")
//    val posts: Post? = null
//
//)

data class Post(
    val id: UUID,
    val title: String,
    val content: String,
    val userNickname: String,
    val image: List<ImageVector> = emptyList(),
    val comments: List<Comment> = emptyList(),
    val createAt: LocalDateTime,
    val updateAt: LocalDateTime
) {
    constructor() : this(
        UUID.randomUUID(),
        "",
        "",
        "",
        emptyList(),
        emptyList(),
        LocalDateTime.now(),
        LocalDateTime.now())
}

data class Comment(
    val id: UUID,
    val nickname: String,
    val content: String,
    val parentCommentId: Int?,
    val parentUserNickname: String,
    val isReply: Boolean = false,
    val createAt: LocalDateTime,
    val updateAt: LocalDateTime
) {
    constructor() : this(
        UUID.randomUUID(),
        "",
        "",
        0,
        "",
        false,
        LocalDateTime.now(),
        LocalDateTime.now())
}