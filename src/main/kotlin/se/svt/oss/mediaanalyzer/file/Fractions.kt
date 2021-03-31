// SPDX-FileCopyrightText: 2020 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

@file:JvmName("Fractions")
package se.svt.oss.mediaanalyzer.file

import org.apache.commons.math3.fraction.Fraction

private val regex = Regex("((\\d+)[/:])?(\\d+)")

fun parse(value: String): Fraction =
    parseOrNull(value) ?: throw IllegalArgumentException("$value is not a valid fraction!")

fun parseOrNull(value: String): Fraction? {
    val matchResult = regex.matchEntire(value) ?: return null
    if (matchResult.groupValues[2].isBlank()) {
        return Fraction(matchResult.groupValues[3].toInt())
    }
    val numerator = matchResult.groupValues[2].toInt()
    val denominator = matchResult.groupValues[3].toInt()
    if (denominator == 0) return null
    return Fraction(numerator, denominator)
}

typealias FractionString = String

fun FractionString.toFraction() = parse(this)
fun FractionString.toFractionOrNull() = parseOrNull(this)
fun Int.toFraction() = Fraction(this, 1)

fun Fraction.stringValue(delimiter: String = "/") = "$numerator$delimiter$denominator"
operator fun Fraction.plus(other: Fraction): Fraction = this.add(other)
operator fun Fraction.minus(other: Fraction): Fraction = this.subtract(other)
operator fun Fraction.times(other: Fraction): Fraction = this.multiply(other)
operator fun Fraction.div(other: Fraction): Fraction = this.divide(other)
