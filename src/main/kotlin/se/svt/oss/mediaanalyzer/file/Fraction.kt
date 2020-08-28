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
    }

    val stringValue
        get() = "$numerator/$denominator"

    val isValid: Boolean
        get() = denominator != 0

    fun toDouble(): Double = numerator.toDouble() / denominator

    fun toDoubleSafe() = if (isValid) toDouble() else null
}

typealias FractionString = String

fun FractionString.toFraction() = Fraction.parse(this)
