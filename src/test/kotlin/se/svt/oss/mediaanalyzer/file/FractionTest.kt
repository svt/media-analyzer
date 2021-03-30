// SPDX-FileCopyrightText: 2020 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.mediaanalyzer.file

import org.junit.jupiter.api.Test
import se.svt.oss.mediaanalyzer.Assertions.assertThat
import se.svt.oss.mediaanalyzer.Assertions.assertThatThrownBy

internal class FractionTest {

    @Test
    fun testParse() {
        val fraction = "720/576".toFraction()
        assertThat(fraction)
            .hasNumerator(5)
            .hasDenominator(4)
        assertThat("720:576".toFraction()).isEqualTo(fraction)
        assertThat("720:576".toFractionOrNull()).isEqualTo(fraction)
    }

    @Test
    fun testInvalid() {
        listOf("10/0", "xxx", "10:p").forEach {
            assertThatThrownBy { Fraction.parse(it) }
                .hasMessage("$it is not a valid fraction!")
            assertThatThrownBy { it.toFraction() }
                .hasMessage("$it is not a valid fraction!")
            assertThat(Fraction.parseOrNull(it)).isNull()
            assertThat(it.toFractionOrNull()).isNull()
        }
        assertThatThrownBy { Fraction(1, 0) }
            .hasMessage("denominator must not be 0!")
    }

    @Test
    fun testToDouble() {
        assertThat(Fraction(1, 2).toDouble()).isEqualTo(0.5)
    }

    @Test
    fun testIntToFraction() {
        assertThat(5.toFraction()).isEqualTo(Fraction(5, 1))
    }

    @Test
    fun testHashCode() {
        assertThat(Fraction(7, 8)).hasSameHashCodeAs(31 * 7 + 8)
    }

    @Test
    fun testSimplified() {
        assertThat(Fraction(0, 4)).isEqualTo(Fraction(0, 1))
        assertThat(Fraction(4, 1)).isEqualTo(Fraction(4, 1))
        assertThat(Fraction(1080, 1920)).isEqualTo(Fraction(9, 16))
    }

    @Test
    fun testStringValue() {
        assertThat(Fraction(16, 9).stringValue()).isEqualTo("16/9")
        assertThat(Fraction(16, 9)).hasToString("16/9")
        assertThat(Fraction(16, 9).stringValue(":")).isEqualTo("16:9")
        assertThat(Fraction(16, 9).stringValue("/")).isEqualTo("16/9")
    }

    @Test
    fun testCompareTo() {
        listOf(
            Pair("16:9", "4:3"),
            Pair("50/1", "25/1"),
            Pair("4:3", "1:1")
        ).map { Pair(it.first.toFraction(), it.second.toFraction()) }
            .forEach {
                assertThat(it.first > it.second).`as`("${it.first} > ${it.second}").isTrue()
                assertThat(it.second < it.first).`as`("${it.second} < ${it.first}").isTrue()
            }
        assertThat(Fraction(16, 9).compareTo(Fraction(1920, 1080))).isEqualTo(0)
    }

    @Test
    fun testArithmetic() {
        assertThat(Fraction(7, 4) + Fraction(2, 7)).`as`("7/4 + 2/7").isEqualTo(Fraction(57, 28))
        assertThat(Fraction(7, 4) - Fraction(2, 7)).`as`("7/4 - 2/7").isEqualTo(Fraction(41, 28))
        assertThat(Fraction(7, 4) / Fraction(2, 7)).`as`("7/4 / 2/7").isEqualTo(Fraction(49, 8))
        assertThat(Fraction(7, 4) * Fraction(2, 7)).`as`("7/4 * 2/7").isEqualTo(Fraction(1, 2))
    }
}
