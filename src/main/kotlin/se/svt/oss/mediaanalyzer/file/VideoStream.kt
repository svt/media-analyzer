// SPDX-FileCopyrightText: 2020 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.mediaanalyzer.file

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class VideoStream(
    val format: String?,
    val codec: String?,
    val profile: String?,
    val level: String?,
    val width: Int,
    val height: Int,
    val sampleAspectRatio: FractionString?,
    val displayAspectRatio: FractionString?,
    // Rotation instruction anti-clockwise
    val rotation: Int?,
    val pixelFormat: String?,
    val frameRate: FractionString,
    val duration: Double,
    val bitrate: Long?,
    val bitDepth: Int?,
    val numFrames: Int,
    val isInterlaced: Boolean,
    val transferCharacteristics: String?,
    /**
     * "full" or "limited" when recognized. Unrecognized ffprobe values pass through
     * unchanged (forwardable to ffmpeg's -color_range); treat any other value as
     * unknown. Null means no source declared a range.
     */
    val colorRange: String?,
    val colorSpace: String?,
    val colorTransfer: String?,
    val colorPrimaries: String?,
    val codecTagString: String?,
    val masteringPeakNits: Int? = null,
    val masteringMinNits: Double? = null,
    val maxCllNits: Int? = null,
    val maxFallNits: Int? = null,
)
