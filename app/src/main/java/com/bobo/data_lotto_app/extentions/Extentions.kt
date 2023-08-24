package com.bobo.data_lotto_app.extentions

import java.text.NumberFormat
import java.util.Locale

fun Float.toPer(): String {
//    val format: NumberFormat = NumberFormat.getCurrencyInstance()
    val format: NumberFormat = NumberFormat.getInstance()
    format.maximumFractionDigits = 2
    return format.format(this)
}


fun Long.toWon(): String {
    val won = NumberFormat.getInstance(Locale.KOREA)
    won.maximumFractionDigits = 0
    return won.format(this)
}