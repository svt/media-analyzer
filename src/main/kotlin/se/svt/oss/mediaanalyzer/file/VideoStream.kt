// SPDX-FileCopyrightText: 2020 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.mediaanalyzer.file

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
    val colorRange: String?,
    val colorSpace: String?,
    val colorTransfer: String?,
    val colorPrimaries: String?,
    val codecTagString: String?,
    // Parsed from MediaInfo MasteringDisplay_Luminance/MaxCLL/MaxFALL. Null when the
    // container declares none (common for DoVi mezzanines) — consumers must treat null
    // as "unknown", not as a value.
    val masteringPeakNits: Int? = null,
    val masteringMinNits: Double? = null,
    val maxCllNits: Int? = null,
    val maxFallNits: Int? = null,
)
