package com.bobo.data_lotto_app.Localdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
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


//@Entity(tableName = "LocalUserData_table")
//data class LocalUserData(
//
//    @PrimaryKey
//    @ColumnInfo(name = "id")
//    var id: UUID = UUID.randomUUID(),
//
//    @ColumnInfo(name = "")
//    val : Int? = null,
//)



