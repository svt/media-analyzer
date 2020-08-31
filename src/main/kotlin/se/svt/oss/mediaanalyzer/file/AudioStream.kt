// SPDX-FileCopyrightText: 2020 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.mediaanalyzer.file

data class AudioStream(
    val format: String?,
    val codec: String?,
    val duration: Double?,
    val channels: Int,
    val samplingRate: Int?,
    val bitrate: Long?
)
