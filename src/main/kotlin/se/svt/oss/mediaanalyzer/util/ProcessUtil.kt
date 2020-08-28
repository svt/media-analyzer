package se.svt.oss.mediaanalyzer.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import mu.KotlinLogging

internal object ProcessUtil {

    private val log = KotlinLogging.logger { }

    internal inline fun <reified T> runAndParse(objectMapper: ObjectMapper, vararg command: String): Pair<Int, T> {
        log.debug { "Running '${command.joinToString(" ")}'" }
        val process = ProcessBuilder(command.toList()).start()
        val output = process.inputStream.bufferedReader().use { it.readText() }
        val exitCode = process.waitFor()
        log.trace { "${command.firstOrNull()} finished with exit code: $exitCode" }
        val parsed = try {
            objectMapper.readValue<T>(output)
        } catch (e: Exception) {
            throw RuntimeException("Error parsing ${T::class.simpleName} from output: '$output'", e)
        }
        return Pair(exitCode, parsed)
    }
}
