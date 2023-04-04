package de.haukesomm.sokoban.core

import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic

object Id {
    private const val defaultLength = 6
    private val chars = "123456789abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ".toCharArray()

    @JvmStatic
    @JvmOverloads
    fun next(length: Int = defaultLength) = buildString {
        for (i in 0 until length) {
            append(chars.random())
        }
    }
}