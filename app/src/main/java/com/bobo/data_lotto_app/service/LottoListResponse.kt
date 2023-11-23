package com.bobo.data_lotto_app.service

import com.bobo.data_lotto_app.Localdb.Lotto
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.squareup.moshi.Json

//data class LottoListResponse (
//    val message: String? = null,
//    val statusCode: Long? = null,
//    @Json(name = "data")
//    val lottos: List<Lotto>? = null
//)

data class FirebaseLottoListResponse (
    var id: String? = null,
    var createAt: String? = null,
    var lotto: Lotto? = null
) {
    constructor(data: QueryDocumentSnapshot): this() {
        this.id = data.id
        this.createAt = data["createAt"] as String? ?: ""
        this.lotto = data["lottos"] as Lotto? ?: Lotto()
    }

    fun asHasMap(): HashMap<String, Any?> {
        return hashMapOf(
            "id" to this.id,
            "createAt" to this.createAt,
            "lottos" to this.lotto
        )
    }

}




