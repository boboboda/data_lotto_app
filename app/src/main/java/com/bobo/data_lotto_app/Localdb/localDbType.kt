package com.bobo.data_lotto_app.Localdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.QueryDocumentSnapshot
import java.util.*



@Entity(tableName = "NormalModeNumber_table")
data class NormalModeNumber(

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: UUID = UUID.randomUUID(),

    @ColumnInfo(name = "firstNumber")
    val firstNumber: Int? = null,

    @ColumnInfo(name = "secondNumber")
    val secondNumber: Int? = null,

    @ColumnInfo(name = "thirdNumber")
    val thirdNumber: Int? = null,

    @ColumnInfo(name = "fourthNumber")
    val fourthNumber: Int? = null,

    @ColumnInfo(name = "fifthNumber")
    val fifthNumber: Int? = null,

    @ColumnInfo(name = "sixthNumber")
    val sixthNumber: Int? = null
)



@Entity(tableName = "BigDataModeNumber_table")
data class BigDataModeNumber(

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: UUID = UUID.randomUUID(),

    @ColumnInfo(name = "firstNumber")
    val firstNumber: Int? = null,

    @ColumnInfo(name = "secondNumber")
    val secondNumber: Int? = null,

    @ColumnInfo(name = "thirdNumber")
    val thirdNumber: Int? = null,

    @ColumnInfo(name = "fourthNumber")
    val fourthNumber: Int? = null,

    @ColumnInfo(name = "fifthNumber")
    val fifthNumber: Int? = null,

    @ColumnInfo(name = "sixthNumber")
    val sixthNumber: Int? = null
)


@Entity(tableName = "LocalUserData_table")
data class LocalUserData(

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: UUID = UUID.randomUUID(),

    @ColumnInfo(name = "allNumber_Search_Count")
    val allNumberSearchCount: Int? = null,

    @ColumnInfo(name = "myNumber_search_Count")
    val myNumberSearchCount: Int? = null,

    @ColumnInfo(name = "number_Lottery_Count")
    val numberLotteryCount: Int? = null,
)

@Entity(tableName = "AllLottoNumber_table")
data class Lotto(

    @PrimaryKey
    @ColumnInfo(name = "drwNo")
    var drwNo: Long? = null,

    @ColumnInfo(name = "totSellamnt")
    var totSellamnt: Long? = null,

    @ColumnInfo(name = "returnValue")
    var returnValue: String? = null,

    @ColumnInfo(name = "drwNoDate")
    var drwNoDate: String? = null,

    @ColumnInfo(name = "firstWinamnt")
    var firstWinamnt: Long? = null,

    @ColumnInfo(name = "firstPrzwnerCo")
    var firstPrzwnerCo: Long? = null,

    @ColumnInfo(name = "firstAccumamnt")
    var firstAccumamnt: Long? = null,

    @ColumnInfo(name = "drwtNo1")
    var drwtNo1: Long? = null,

    @ColumnInfo(name = "drwtNo2")
    var drwtNo2: Long? = null,

    @ColumnInfo(name = "drwtNo3")
    var drwtNo3: Long? = null,

    @ColumnInfo(name = "drwtNo4")
    var drwtNo4: Long? = null,

    @ColumnInfo(name = "drwtNo5")
    var drwtNo5: Long? = null,

    @ColumnInfo(name = "drwtNo6")
    var drwtNo6: Long? = null,

    @ColumnInfo(name = "bnusNo")
    var bnusNo: Long? = null
) {


    constructor(data: QueryDocumentSnapshot): this() {
        this.totSellamnt = data["totSellamnt"] as Long? ?: 1
        this.returnValue = data["returnValue"] as String? ?: ""
        this.drwNoDate = data["drwNoDate"] as String? ?: ""
        this.firstWinamnt = data["firstWinamnt"] as Long? ?: 1
        this.drwtNo6 = data["drwtNo6"] as Long? ?: 1
        this.drwtNo4 = data["drwtNo4"] as Long? ?: 1
        this.firstPrzwnerCo = data["firstPrzwnerCo"] as Long? ?: 1
        this.drwtNo5 = data["drwtNo5"] as Long? ?: 1
        this.bnusNo = data["bnusNo"] as Long? ?: 1
        this.firstAccumamnt = data["firstAccumamnt"] as Long? ?: 1
        this.drwNo = data["drwNo"] as Long? ?: 1
        this.drwtNo2 = data["drwtNo2"] as Long? ?: 1
        this.drwtNo3  = data["drwtNo3"] as Long? ?: 1
        this.drwtNo1  = data["drwtNo1"] as Long? ?: 1
    }

    fun asHasMap(): HashMap<String, Any?> {
        return hashMapOf(
            "totSellamnt" to this.totSellamnt,
            "returnValue" to this.returnValue,
            "drwNoDate" to this.drwNoDate,
            "firstWinamnt" to this.firstWinamnt,
            "drwtNo6" to this.drwtNo6,
            "drwtNo4" to this.drwtNo4,
            "firstPrzwnerCo" to this.firstPrzwnerCo,
            "drwtNo5" to this.drwtNo5,
            "bnusNo" to this.bnusNo,
            "firstAccumamnt" to this.firstAccumamnt,
            "drwNo" to this.drwNo,
            "drwtNo2" to this.drwtNo2,
            "drwtNo3" to this.drwtNo3,
            "drwtNo1" to this.drwtNo1,
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




//data class Lotto (
//    var totSellamnt: Long? = null,
//    var returnValue: String? = null,
//    var drwNoDate: String? = null,
//    var firstWinamnt: Long? = null,
//    var drwtNo6: Long? = null,
//    var drwtNo4: Long? = null,
//    var firstPrzwnerCo: Long? = null,
//    var drwtNo5: Long? = null,
//    var bnusNo: Long? = null,
//    var firstAccumamnt: Long? = null,
//    var drwNo: Long? = null,
//    var drwtNo2: Long? = null,
//    var drwtNo3: Long? = null,
//    var drwtNo1: Long? = null)