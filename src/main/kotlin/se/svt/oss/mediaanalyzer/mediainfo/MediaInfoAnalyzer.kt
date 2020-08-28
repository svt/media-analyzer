package se.svt.oss.mediaanalyzer.mediainfo

import com.fasterxml.jackson.databind.ObjectMapper
import se.svt.oss.mediaanalyzer.util.ProcessUtil

class MediaInfoAnalyzer
@JvmOverloads constructor(private val objectMapper: ObjectMapper = ObjectMapper().findAndRegisterModules()) {

    fun analyze(file: String): MediaInfo {
        val (exitCode, mediaInfo) = ProcessUtil.runAndParse<MediaInfo>(
            objectMapper,
            "mediainfo",
            "--Output=JSON",
            file
        )
        if (exitCode != 0) {
            throw RuntimeException("mediainfo returned exit code: $exitCode")
        }
        return mediaInfo
    }
}
