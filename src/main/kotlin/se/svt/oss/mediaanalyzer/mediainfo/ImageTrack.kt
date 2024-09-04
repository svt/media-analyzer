// SPDX-FileCopyrightText: 2020 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.mediaanalyzer.mediainfo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ImageTrack(
    override val format: String,
    override val extra: Map<String, Any> = emptyMap(),
    @JsonProperty("Width")
    val width: Int,
    @JsonProperty("Height")
    val height: Int
) : Track
