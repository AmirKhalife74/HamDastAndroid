package com.example.hamdast.utils

import java.text.DecimalFormat
import java.text.NumberFormat

fun twoDigitConvertor(number: Int): String {
    return if (number < 10) {
        "0$number"
    } else {
        number.toString()
    }
}

fun moneySeperator(number: Long): String {
    var num = number
    if (num < 0) {
        num = num * (-1)
    }

    val formatter: NumberFormat = DecimalFormat("#,###")
    return formatter.format(num)
}