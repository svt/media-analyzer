package se.svt.oss.mediaanalyzer.ffprobe

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class UnknownSideData(
    override val side_data_type: String?
) : SideData
