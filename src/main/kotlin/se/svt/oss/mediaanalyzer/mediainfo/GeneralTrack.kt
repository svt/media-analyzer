// SPDX-FileCopyrightText: 2020 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.mediaanalyzer.mediainfo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class GeneralTrack(
    override val format: String = "UNKNOWN",
    override val extra: Map<String, Any> = emptyMap(),
    @JsonProperty("FileSize")
    val fileSize: Long,
    @JsonProperty("Duration")
    val duration: Double?,
    @JsonProperty("ImageCount")
    val imageCount: Int = 0,
    @JsonProperty("VideoCount")
    val videoCount: Int = 0,
    @JsonProperty("AudioCount")
    val audioCount: Int = 0,
    @JsonProperty("TextCount")
    val textCount: Int = 0,
    @JsonProperty("OverallBitRate")
    val overallBitrate: Long?
) : Track
