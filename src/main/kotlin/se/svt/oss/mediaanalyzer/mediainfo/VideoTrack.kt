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
    @JsonProperty("colour_description_present_Source")
    val colourDescriptionPresentSource: String?,
    @JsonProperty("colour_range")
    val colourRange: String?,
    @JsonProperty("colour_range_Source")
    val colourRangeSource: String?,
    @JsonProperty("colour_primaries")
    val colourPrimaries: String?,
    @JsonProperty("colour_primaries_Source")
    val colourPrimariesSource: String?,
    @JsonProperty("transfer_characteristics")
    val transferCharacteristics: String?,
    @JsonProperty("transfer_characteristics_Source")
    val transferCharacteristicsSource: String?,
    @JsonProperty("matrix_coefficients")
    val matrixCoefficients: String?,
    @JsonProperty("matrix_coefficients_Source")
    val matrixCoefficientsSource: String?,
    @JsonProperty("MasteringDisplay_ColorPrimaries")
    val masteringDisplayColourPrimaries: String?,
    @JsonProperty("MasteringDisplay_ColorPrimaries_Source")
    val masteringDisplayColourPrimariesSource: String?,
    @JsonProperty("MasteringDisplay_Luminance")
    val masteringDisplayLuminance: String?,
    @JsonProperty("MasteringDisplay_Luminance_Source")
    val masteringDisplayLuminanceSource: String?
) : Track {
    val isInterlaced: Boolean?
        get() = scanType?.let { it != "Progressive" }
}
