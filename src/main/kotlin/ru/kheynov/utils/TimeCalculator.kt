package ru.kheynov.utils


fun getExpirationTime(): Long {
    return 1658955600 + 86400 * ((unixTime() - 1658955600) / 86400 + 1)
}

fun unixTime(): Long = System.currentTimeMillis() / 1000