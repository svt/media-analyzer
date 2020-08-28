package se.svt.oss.mediaanalyzer.mediainfo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class TextTrack(override val format: String) : Track
