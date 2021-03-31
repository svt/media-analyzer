// SPDX-FileCopyrightText: 2020 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.mediaanalyzer.file

import org.apache.commons.math3.fraction.Fraction
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
        assertThat("3".toFraction()).isEqualTo(Fraction(3))
    }

    @Test
    fun testParseInvalid() {
        listOf("10/0", "xxx", "10:p").forEach {
            assertThatThrownBy { parse(it) }
                .hasMessage("$it is not a valid fraction!")
            assertThatThrownBy { it.toFraction() }
                .hasMessage("$it is not a valid fraction!")
            assertThat(parseOrNull(it)).isNull()
            assertThat(it.toFractionOrNull()).isNull()
        }
    }

    @Test
    fun testStringValue() {
        assertThat("4:3".toFraction().stringValue()).isEqualTo("4/3")
        assertThat("4:3".toFraction().stringValue(":")).isEqualTo("4:3")
    }

    @Test
    fun testIntToFraction() {
        assertThat(5.toFraction()).isEqualTo(Fraction(5, 1))
    }

    @Test
    fun testOperatorFunctions() {
        assertThat(Fraction(7, 4) + Fraction(2, 7)).`as`("7/4 + 2/7").isEqualTo(Fraction(57, 28))
        assertThat(Fraction(7, 4) - Fraction(2, 7)).`as`("7/4 - 2/7").isEqualTo(Fraction(41, 28))
        assertThat(Fraction(7, 4) / Fraction(2, 7)).`as`("7/4 / 2/7").isEqualTo(Fraction(49, 8))
        assertThat(Fraction(7, 4) * Fraction(2, 7)).`as`("7/4 * 2/7").isEqualTo(Fraction(1, 2))
    }
}
