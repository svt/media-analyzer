package se.svt.oss.mediaanalyzer.file

data class VideoStream(
    val format: String?,
    val codec: String,
    val profile: String?,
    val level: String?,
    val width: Int,
    val height: Int,
    val sampleAspectRatio: FractionString?,
    val displayAspectRatio: FractionString?,
    val pixelFormat: String?,
    val frameRate: FractionString,
    val duration: Double,
    val bitrate: Long?,
    val bitDepth: Int?,
    val numFrames: Int,
    val isInterlaced: Boolean,
    val transferCharacteristics: String?
)
