package se.svt.oss.mediaanalyzer.file

import com.fasterxml.jackson.annotation.JsonTypeName

@JsonTypeName("AudioFile")
data class AudioFile(
    override val file: String,
    override val fileSize: Long,
    override val format: String,
    override val overallBitrate: Long,
    override val duration: Double,
    override val audioStreams: List<AudioStream>
) : MediaContainer {
    override val type: String
        get() = "AudioFile"
}
