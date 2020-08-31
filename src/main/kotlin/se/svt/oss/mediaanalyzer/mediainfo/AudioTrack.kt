// SPDX-FileCopyrightText: 2020 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.mediaanalyzer.mediainfo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class AudioTrack(
    override val format: String,
    @JsonProperty("Duration")
    val duration: Double,
    @JsonProperty("BitRate")
    val bitrate: Long,
    @JsonProperty("Channels")
    val channels: Int,
    @JsonProperty("SamplingRate")
    val samplingRate: Int,
    @JsonProperty("BitDepth")
    val bitDepth: Int
) : Track
