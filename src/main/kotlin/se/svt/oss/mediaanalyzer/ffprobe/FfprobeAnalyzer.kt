// SPDX-FileCopyrightText: 2020 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.mediaanalyzer.ffprobe

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import se.svt.oss.mediaanalyzer.util.ProcessUtil

private val log = KotlinLogging.logger { }

class FfprobeAnalyzer
@JvmOverloads constructor(
    private val objectMapper: ObjectMapper = ObjectMapper().findAndRegisterModules(),
    private val filterValidParams: Boolean = true
) {

    private val validParams: Set<String> by lazy {
        getValidFfprobeParams()
    }

    private fun getValidFfprobeParams(): Set<String> {
        val process = ProcessBuilder("ffprobe", "-h")
            .redirectErrorStream(true)
            .start()
        val validParams = process.inputStream.bufferedReader().useLines { lines ->
            lines.map { it.trim() }
                .filter { it.startsWith("-") }
                .map { it.removePrefix("-").substringBefore(" ") }
                .toSet()
        }
        val exitCode = process.waitFor()
        if (exitCode != 0) {
            log.error { "Failed to get valid ffprobe parameters, ffprobe failed with exit code: $exitCode. Will not filter input params." }
            return emptySet()
        }
        return validParams
    }

    fun analyze(file: String, ffprobeInputParams: LinkedHashMap<String, String?>): ProbeResult {
        val inputParams = if (filterValidParams && ffprobeInputParams.isNotEmpty() && validParams.isNotEmpty()) {
            ffprobeInputParams.filterKeys { it in validParams }
        } else {
            ffprobeInputParams
        }
        val command = buildList {
            add("ffprobe")
            add("-v")
            add("quiet")
            add("-of")
            add("json")
            add("-show_streams")
            add("-show_format")
            add("-show_error")
            inputParams.forEach { (key, value) ->
                add("-$key")
                value?.takeIf { it.isNotBlank() }?.let { add(it) }
            }
            add(file)
        }
        val (exitCode, probeResult) = ProcessUtil.runAndParse<ProbeResult>(
            objectMapper,
            *command.toTypedArray()
        )
        if (exitCode != 0 || probeResult.error != null) {
            val message = probeResult.error?.string ?: "exitcode: $exitCode"
            throw RuntimeException("ffprobe failed for $file: $message")
        }
        return probeResult
    }

    @JvmOverloads
    fun isInterlaced(file: String, videoIndex: Int = 0, ffprobeInputParams: LinkedHashMap<String, String?>): Boolean {
        val command = buildList {
            add("ffprobe")
            add("-v")
            add("quiet")
            add("-of")
            add("json")
            add("-read_intervals")
            add("%+5")
            add("-select_streams")
            add("v:$videoIndex")
            add("-show_entries")
            add("frame=interlaced_frame")
            add("-show_error")
            ffprobeInputParams.forEach { (key, value) ->
                add("-$key")
                value?.takeIf { it.isNotBlank() }?.let { add(it) }
            }
            add(file)
        }
        val (exitCode, probeResult) = ProcessUtil.runAndParse<ProbeResult>(
            objectMapper,
            *command.toTypedArray()
        )
        if (exitCode != 0 || probeResult.error != null) {
            val message = probeResult.error?.string ?: "exitcode: $exitCode"
            throw RuntimeException("ffprobe failed for $file: $message")
        }
        if (probeResult.frames.isEmpty()) {
            throw RuntimeException("Error parsing ffprobe output!")
        }

        val (interlaced, noninterlaced) = probeResult.frames.partition { it.interlaced_frame }

        return interlaced.size > noninterlaced.size
    }
}
