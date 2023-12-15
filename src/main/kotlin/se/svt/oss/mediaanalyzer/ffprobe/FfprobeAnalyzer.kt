// SPDX-FileCopyrightText: 2020 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.mediaanalyzer.ffprobe

import com.fasterxml.jackson.databind.ObjectMapper
import se.svt.oss.mediaanalyzer.util.ProcessUtil

class FfprobeAnalyzer
@JvmOverloads constructor(private val objectMapper: ObjectMapper = ObjectMapper().findAndRegisterModules()) {

    fun analyze(file: String, ffprobeInputParams: LinkedHashMap<String, String?>): ProbeResult {
        val command = buildList<String> {
            add("ffprobe")
            add("-v")
            add("quiet")
            add("-of")
            add("json")
            add("-show_streams")
            add("-show_format")
            add("-show_error")
            ffprobeInputParams.forEach { (key, value) ->
                add("-$key")
                value?.let { add(it) }
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
        val command = buildList<String> {
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
                value?.let { add(it) }
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
