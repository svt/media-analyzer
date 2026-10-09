// SPDX-FileCopyrightText: 2020 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.mediaanalyzer.mediainfo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class VideoTrack(
    override val format: String,
    override val extra: Map<String, Any> = emptyMap(),
    @JsonProperty("Duration")
    val duration: Double,
    @JsonProperty("BitRate")
    val bitrate: String?,
    @JsonProperty("Width")
    val width: Int,
    @JsonProperty("Height")
    val height: Int,
    @JsonProperty("PixelAspectRatio")
    val pixelAspectRatio: Double?,
    @JsonProperty("DisplayAspectRatio")
    val displayAspectRatio: Double,
    @JsonProperty("Rotation")
    val rotation: Double?,
    @JsonProperty("FrameRate")
    val frameRate: Double,
    @JsonProperty("FrameRate_Num")
    val frameRateNum: Int?,
    @JsonProperty("FrameRate_Den")
    val frameRateDen: Int?,
    @JsonProperty("FrameCount")
    val frameCount: Int,
    @JsonProperty("ColorSpace")
    val colorSpace: String?,
    @JsonProperty("ChromaSubsampling")
    val chromaSubSampling: String?,
    @JsonProperty("BitDepth")
    val bitDepth: Int,
    @JsonProperty("ScanType")
    val scanType: String?,
    @JsonProperty("ScanOrder")
    val scanOrder: String?,
    @JsonProperty("Format_Profile")
    val formatProfile: String?,
    @JsonProperty("Format_Level")
    val formatLevel: String?,
    @JsonProperty("Format_Tier")
    val formatTier: String?,
    @JsonProperty("HDR_Format")
    val hdrFormat: String?,
    @JsonProperty("HDR_Format_Compatibility")
    val hdrFormatCompatibility: String?,
    @JsonProperty("colour_description_present")
    val colourDescriptionPresent: String?,
    @JsonProperty("colour_range")
    val colourRange: String?,
    @JsonProperty("colour_primaries")
    val colourPrimaries: String?,
    @JsonProperty("transfer_characteristics")
    val transferCharacteristics: String?,
    @JsonProperty("matrix_coefficients")
    val matrixCoefficients: String?,
    @JsonProperty("MasteringDisplay_ColorPrimaries")
    val masteringDisplayColourPrimaries: String?,
    @JsonProperty("MasteringDisplay_Luminance_Min")
    private val masteringDisplayLuminanceMin: String?,
    @JsonProperty("MasteringDisplay_Luminance_Max")
    private val masteringDisplayLuminanceMax: String?,
    @JsonProperty("MaxCLL")
    private val maxContentLightLevel: String?,
    @JsonProperty("MaxFALL")
    private val maxFrameAverageLightLevel: String?,
) : Track {
    val isInterlaced: Boolean?
        get() = scanType?.let { it != "Progressive" }

    val masteringPeakNits: Int?
        get() = masteringDisplayLuminanceMax?.toIntOrNull()

    val masteringMinNits: Double?
        get() = masteringDisplayLuminanceMin?.toDoubleOrNull()

    val maxCllNits: Int?
        get() = maxContentLightLevel?.toIntOrNull()

    val maxFallNits: Int?
        get() = maxFrameAverageLightLevel?.toIntOrNull()
}
