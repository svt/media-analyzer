// SPDX-FileCopyrightText: 2020 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.mediaanalyzer.ffprobe

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Format(
    val filename: String,
    val format_name: String,
    val format_long_name: String?,
    val duration: Double,
    val size: Long,
    val bit_rate: Long
)
