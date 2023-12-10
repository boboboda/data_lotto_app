package com.bobo.data_lotto_app.service

import com.google.firebase.firestore.QueryDocumentSnapshot
import com.squareup.moshi.Json

data class EmailResponse (
    val message: String? = null,
    @Json(name = "data")
    val email: Email? = null
)

data class Email (
    val email: String? = null,
    val token: String? = null,
)

data class EmailRequest(
    val email: String? = null,
)

data class User (
    var id: String? = null,
    var horseman: Int? = null,
    var deviceId: String? = null,
    var type: String? = null,
    var authState: Boolean? = null,
    var signupVerifyToken: String? = null,
    var username: String? = null,
    var email: String? = null,
    var payment: Boolean? = null,
    var paymentStartDate: String? = null,
    var paymentEndDate: String? = null,
    var allNumberSearchCount: Int? = null,
    var myNumberSearchCount: Int? = null,
    var numberLotteryCount: Int? = null,
    var createdAt: String? = null,
    var updatedAt: String? = null) {

    constructor(data: QueryDocumentSnapshot): this() {
        this.id = data.id
        this.horseman = data["horseman"] as Int? ?: 0
        this.deviceId = data["deviceId"] as String? ?: ""
        this.type = data["type"] as String? ?: ""
        this.authState = data["authState"] as Boolean? ?: false
        this.signupVerifyToken = data["signupVerifyToken"] as String? ?: ""
        this.username = data["username"] as String? ?: ""
        this.email = data["email"] as String? ?: ""
        this.payment = data["payment"] as Boolean? ?: false
        this.paymentStartDate = data["paymentStartDate"] as String? ?: ""
        this.paymentEndDate = data["paymentEndDate"] as String? ?: ""
        this.allNumberSearchCount = data["allNumberSearchCount"] as Int? ?: 0
        this.myNumberSearchCount = data["myNumberSearchCount"] as Int? ?: 0
        this.numberLotteryCount = data["numberLotteryCount"] as Int? ?: 0
        this.createdAt = data["createdAt"] as String? ?: ""
        this.updatedAt = data["updatedAt"] as String? ?: ""
    }

    fun asHasMap() : HashMap<String, Any?>{
        return hashMapOf(
            "id" to this.id,
            "horseman" to this.horseman,
            "deviceId" to this.deviceId,
            "type" to this.type,
            "authState" to this.authState,
            "signupVerifyToken" to this.signupVerifyToken,
            "username" to this.username,
            "email" to this.email,
            "payment" to this.payment,
            "paymentStartDate" to this.paymentStartDate,
            "paymentEndDate" to this.paymentEndDate,
            "allNumberSearchCount" to this.allNumberSearchCount,
            "myNumberSearchCount" to this.myNumberSearchCount,
            "numberLotteryCount" to this.numberLotteryCount,
            "createdAt" to this.createdAt,
            "updatedAt" to this.updatedAt
        )
    }
}