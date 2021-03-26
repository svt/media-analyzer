// SPDX-FileCopyrightText: 2020 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.mediaanalyzer.file

data class Fraction(val numerator: Int, val denominator: Int) {
    companion object {
        private val regex = Regex("(\\d+)[/:](\\d+)")

        @JvmStatic
        fun parse(value: String): Fraction {
            val matchResult =
                regex.matchEntire(value) ?: throw IllegalArgumentException("$value is not a valid fraction!")
            val nominator = matchResult.groupValues[1].toInt()
            val denominator = matchResult.groupValues[2].toInt()
            return Fraction(nominator, denominator)
        }

        private fun gcd(a: Int, b: Int): Int {
            return if (b == 0) a else gcd(b, a % b)
        }
    }

    fun simplified(): Fraction {
        val gcd = gcd(numerator, denominator)
        return Fraction(numerator / gcd, denominator / gcd)
    }

    @JvmOverloads
    fun stringValue(delimiter: String = "/") = "$numerator$delimiter$denominator"

    val isValid: Boolean
        get() = denominator != 0

    fun toDouble(): Double = numerator.toDouble() / denominator

    fun toDoubleSafe() = if (isValid) toDouble() else null
}

typealias FractionString = String

fun FractionString.toFraction() = Fraction.parse(this)
