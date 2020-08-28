package se.svt.oss.mediaanalyzer.mediainfo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class OtherTrack(override val format: String) : Track
