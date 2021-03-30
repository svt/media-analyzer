// SPDX-FileCopyrightText: 2020 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.mediaanalyzer.file

class Fraction(num: Int, den: Int) : Comparable<Fraction> {
    companion object {
        private val regex = Regex("(\\d+)[/:](\\d+)")

        @JvmStatic
        fun parse(value: String) = parseOrNull(value)
            ?: throw IllegalArgumentException("$value is not a valid fraction!")

        @JvmStatic
        fun parseOrNull(value: String): Fraction? {
            val matchResult =
                regex.matchEntire(value) ?: return null
            val numerator = matchResult.groupValues[1].toInt()
            val denominator = matchResult.groupValues[2].toInt()
            if (denominator == 0) return null
            return Fraction(numerator, denominator)
        }

        @JvmStatic
        fun gcd(a: Int, b: Int): Int {
            return if (b == 0) a else gcd(b, a % b)
        }
    }

    val numerator: Int
    val denominator: Int

    init {
        if (den == 0) throw IllegalArgumentException("denominator must not be 0!")
        val gcd = gcd(num, den)
        numerator = num / gcd
        denominator = den / gcd
    }

    operator fun plus(other: Fraction): Fraction {
        val num = (numerator * other.denominator) + (other.numerator * denominator)
        val den = denominator * other.denominator
        return Fraction(num, den)
    }

    operator fun minus(other: Fraction): Fraction {
        val num = (numerator * other.denominator) - (other.numerator * denominator)
        val den = denominator * other.denominator
        return Fraction(num, den)
    }

    operator fun times(other: Fraction) =
        Fraction(
            numerator * other.numerator,
            denominator * other.denominator
        )

    operator fun div(other: Fraction) =
        Fraction(
            numerator * other.denominator,
            denominator * other.numerator
        )

    @JvmOverloads
    fun stringValue(delimiter: String = "/") = "$numerator$delimiter$denominator"

    fun toDouble(): Double = numerator.toDouble() / denominator

    override fun toString() = "$numerator/$denominator"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Fraction

        if (numerator != other.numerator) return false
        if (denominator != other.denominator) return false

        return true
    }

    override fun hashCode(): Int {
        var result = numerator
        result = 31 * result + denominator
        return result
    }

    override fun compareTo(other: Fraction): Int {
        if (this == other) {
            return 0
        }
        return (numerator * other.denominator)
            .compareTo(denominator * other.numerator)
    }
}

typealias FractionString = String

fun FractionString.toFraction() = Fraction.parse(this)
fun FractionString.toFractionOrNull() = Fraction.parseOrNull(this)
fun Int.toFraction() = Fraction(this, 1)
