package se.svt.oss.mediaanalyzer.mediainfo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ImageTrack(
    override val format: String,
    @JsonProperty("Width")
    val width: Int,
    @JsonProperty("Height")
    val height: Int
) : Track
