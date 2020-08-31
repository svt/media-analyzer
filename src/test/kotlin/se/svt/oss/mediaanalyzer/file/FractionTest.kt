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
        val fraction = "22/11".toFraction()
        assertThat(fraction)
            .hasNumerator(22)
            .hasDenominator(11)
            .hasStringValue("22/11")
        assertThat("22:11".toFraction()).isEqualTo(fraction)
    }

    @Test
    fun testParseInvalid() {
        assertThatThrownBy { Fraction.parse("apa") }
            .hasMessage("apa is not a valid fraction!")
    }

    @Test
    fun testToDouble() {
        assertThat(Fraction(1, 2).toDouble()).isEqualTo(0.5)
    }

    @Test
    fun testInvalid() {
        val fraction = Fraction(0, 0)
        assertThat(fraction).isNotValid
        assertThat(fraction.toDoubleSafe()).isNull()
    }
}
