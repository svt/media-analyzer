// SPDX-FileCopyrightText: 2020 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.mediaanalyzer.mediainfo

import com.fasterxml.jackson.databind.ObjectMapper
import se.svt.oss.mediaanalyzer.util.ProcessUtil

class MediaInfoAnalyzer
@JvmOverloads constructor(private val objectMapper: ObjectMapper = ObjectMapper().findAndRegisterModules()) {

    fun analyze(file: String, disableImageSequenceDetection: Boolean): MediaInfo {
        val args = buildList {
            add("mediainfo")
            add("--Output=JSON")
            if (disableImageSequenceDetection) {
                add("--File_TestContinuousFileNames=0")
            }
            add(file)
        }
        val (exitCode, mediaInfo) = ProcessUtil.runAndParse<MediaInfo>(
            objectMapper,
            *args.toTypedArray()
        )
        if (exitCode != 0) {
            throw RuntimeException("mediainfo returned exit code: $exitCode")
        }
        return mediaInfo
    }
}
