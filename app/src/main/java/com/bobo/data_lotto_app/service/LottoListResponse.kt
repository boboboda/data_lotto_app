package com.bobo.data_lotto_app.service

import com.google.firebase.firestore.QueryDocumentSnapshot
import com.squareup.moshi.Json

data class LottoListResponse (
    val message: String? = null,
    val statusCode: Long? = null,
    @Json(name = "data")
    val lottos: List<Lotto>? = null
)

data class FirebaseLottoListResponse (
    var id: String? = null,
    var createAt: String? = null,
    var lottos: List<Lotto>? = null
) {
    constructor(data: QueryDocumentSnapshot): this() {
        this.id = data.id
        this.createAt = data["createAt"] as String? ?: ""
        this.lottos = data["lottos"] as List<Lotto>? ?: listOf(Lotto())
    }

    fun asHasMap(): HashMap<String, Any?> {
        return hashMapOf(
            "id" to this.id,
            "createAt" to this.createAt,
            "lottos" to this.lottos
        )
    }

}

data class Lotto (
    var id: String? = null,
    var totSellamnt: String? = null,
    var returnValue: String? = null,
    var drwNoDate: String? = null,
    var firstWinamnt: String? = null,
    var drwtNo6: Long? = null,
    var drwtNo4: Long? = null,
    var firstPrzwnerCo: Long? = null,
    var drwtNo5: Long? = null,
    var bnusNo: Long? = null,
    var firstAccumamnt: String? = null,
    var drwNo: Long? = null,
    var drwtNo2: Long? = null,
    var drwtNo3: Long? = null,
    var drwtNo1: Long? = null,
    var createdAt: String? = null,
    var updatedAt: String? = null) {


    constructor(data: QueryDocumentSnapshot): this() {
        this.id = data.id
        this.totSellamnt = data["totSellamnt"] as String? ?: ""
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
        val updatedAt: String? = null
    }

    fun asHasMap(): HashMap<String, Any?> {
        return hashMapOf(
            "id" to this.id,
            "createAt" to this.createAt,
            "lottos" to this.lottos
        )
    }



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


