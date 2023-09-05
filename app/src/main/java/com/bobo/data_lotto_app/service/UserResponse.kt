package com.bobo.data_lotto_app.service

import com.squareup.moshi.Json

data class UserResponse (
    val message: String? = null,
    @Json(name = "data")
    val users: User? = null

)


data class User (
    val type: String? = null,
    val id: Long? = null,
    val authState: Boolean? = null,
    val signupVerifyToken: String? = null,
    val username: String? = null,
    val email: String? = null,
    val password: String? = null,
    val payment: Boolean? = null,
    val paymentStartDate: String? = null,
    val paymentEndDate: String? = null,
    val allNumberSearchCount: Int? = null,
    val myNumberSearchCount: Int? = null,
    val numberLotteryCount: Int? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null)

data class UserRequest(
    val type: String? = null,
    val authState: Boolean? = null,
    val username: String? = null,
    val email: String? = null,
    val password: String? = null,
)

data class UpdateUserRequest(
    val type: String? = null,
    val id: Long? = null,
    val username: String? = null,
    val email: String? = null,
    val payment: Boolean? = null,
    val paymentStartDate: String? = null,
    val paymentEndDate: String? = null,
    val allNumberSearchCount: Int? = null,
    val myNumberSearchCount: Int? = null,
    val numberLotteryCount: Int? = null,
)