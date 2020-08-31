// SPDX-FileCopyrightText: 2020 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.mediaanalyzer.ffprobe

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class ProbeResult(
    val format: Format?,
    val streams: List<Stream> = emptyList(),
    val frames: List<Frame> = emptyList(),
    val error: FfError?
) {
    inline fun <reified T : Stream> streamsOfType() = streams.filterIsInstance<T>()

    val videoStreams: List<FfVideoStream> by lazy { streamsOfType<FfVideoStream>() }

    val audioStreams: List<FfAudioStream> by lazy { streamsOfType<FfAudioStream>() }
}
