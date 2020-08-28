package se.svt.oss.mediaanalyzer.mediainfo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Media(
    @JsonProperty("@ref")
    val ref: String,
    @JsonProperty("track")
    val tracks: List<Track>
)
