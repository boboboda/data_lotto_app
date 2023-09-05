package com.bobo.data_lotto_app.service

import com.squareup.moshi.Json

data class LottoListResponse (
    val message: String? = null,
    val statusCode: Long? = null,
    @Json(name = "data")
    val lottos: List<Lotto>? = null
)


data class Lotto (
    val id: Long? = null,
    val totSellamnt: String? = null,
    val returnValue: String? = null,
    val drwNoDate: String? = null,
    val firstWinamnt: String? = null,
    val drwtNo6: Long? = null,
    val drwtNo4: Long? = null,
    val firstPrzwnerCo: Long? = null,
    val drwtNo5: Long? = null,
    val bnusNo: Long? = null,
    val firstAccumamnt: String? = null,
    val drwNo: Long? = null,
    val drwtNo2: Long? = null,
    val drwtNo3: Long? = null,
    val drwtNo1: Long? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null) {


    fun hasNumber(checkNumber: String): Boolean {

        val checkNumber = checkNumber.toLong()

        if (drwtNo1 == checkNumber) {
            return true
        } else if (drwtNo2 == checkNumber) {
            return true
        } else if (drwtNo3 == checkNumber) {
            return true
        } else if (drwtNo4 == checkNumber) {
            return true
        } else if (drwtNo5 == checkNumber) {
            return true
        } else if (drwtNo6 == checkNumber) {
            return true
        } else {
            return false
        }
    }
}
