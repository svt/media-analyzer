package se.svt.oss.mediaanalyzer.file

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonTypeName

@JsonTypeName("VideoFile")
data class VideoFile(
    override val file: String,
    override val fileSize: Long,
    override val format: String,
    override val overallBitrate: Long,
    override val duration: Double,
    val videoStreams: List<VideoStream>,
    override val audioStreams: List<AudioStream> = emptyList()
) : MediaContainer {
    override val type: String
        get() = "VideoFile"
    val highestBitrateVideoStream: VideoStream
        @JsonIgnore
        get() = videoStreams.maxBy { it.bitrate ?: 0 }
            ?: throw IllegalStateException("No video streams in $file!")
}
