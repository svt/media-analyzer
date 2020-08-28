package se.svt.oss.mediaanalyzer.file

data class AudioStream(
    val format: String?,
    val codec: String?,
    val duration: Double?,
    val channels: Int,
    val samplingRate: Int?,
    val bitrate: Long?
)
