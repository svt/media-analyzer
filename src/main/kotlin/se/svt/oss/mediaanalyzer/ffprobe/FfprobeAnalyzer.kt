// SPDX-FileCopyrightText: 2020 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.mediaanalyzer.ffprobe

import com.fasterxml.jackson.databind.ObjectMapper
import se.svt.oss.mediaanalyzer.util.ProcessUtil

class FfprobeAnalyzer
@JvmOverloads constructor(private val objectMapper: ObjectMapper = ObjectMapper().findAndRegisterModules()) {

    fun analyze(file: String): ProbeResult {
        val (exitCode, probeResult) = ProcessUtil.runAndParse<ProbeResult>(
            objectMapper,
            "ffprobe",
            "-v",
            "quiet",
            "-of",
            "json",
            "-show_streams",
            "-show_format",
            "-show_error",
            file
        )
        if (exitCode != 0 || probeResult.error != null) {
            val message = probeResult.error?.string ?: "exitcode: $exitCode"
            throw RuntimeException("ffprobe failed for $file: $message")
        }
        return probeResult
    }

    @JvmOverloads
    fun isInterlaced(file: String, videoIndex: Int = 0): Boolean {
        val (exitCode, probeResult) = ProcessUtil.runAndParse<ProbeResult>(
            objectMapper,
            "ffprobe",
            "-v",
            "quiet",
            "-of",
            "json",
            "-read_intervals", "%+5",
            "-select_streams", "v:$videoIndex",
            "-show_entries", "frame=interlaced_frame",
            "-show_error",
            file
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
