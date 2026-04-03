package com.tisto.kmp.helper

fun String.kmpHelper(): String{
    return "$this Hello KMP Helper"
}

fun mains() {
    val string = "Ini apa"
    println(string.kmpHelper())

}

