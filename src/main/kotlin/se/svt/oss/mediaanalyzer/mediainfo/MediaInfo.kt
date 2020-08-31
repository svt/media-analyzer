// SPDX-FileCopyrightText: 2020 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.mediaanalyzer.mediainfo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class MediaInfo(val media: Media) {
    val generalTrack by lazy {
        tracksOfType<GeneralTrack>().firstOrNull() ?: throw RuntimeException("No general track")
    }

    inline fun <reified T : Track> tracksOfType() = media.tracks.filterIsInstance<T>()

    val videoTracks: List<VideoTrack> by lazy { tracksOfType<VideoTrack>() }

    val audioTracks: List<AudioTrack> by lazy { tracksOfType<AudioTrack>() }

    val file: String
        get() = media.ref

    val isImage: Boolean
        get() = generalTrack.imageCount == 1 && !hasVideo && !hasAudio && generalTrack.textCount == 0

    val isSubtitle: Boolean
        get() = generalTrack.textCount == 1 && !isImage && !hasAudio && !hasVideo

    val hasAudio: Boolean
        get() = generalTrack.audioCount > 0

    val hasVideo: Boolean
        get() = generalTrack.videoCount > 0
}
