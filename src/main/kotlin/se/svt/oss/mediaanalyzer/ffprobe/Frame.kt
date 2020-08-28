package se.svt.oss.mediaanalyzer.ffprobe

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Frame(
    val interlaced_frame: Boolean
)
