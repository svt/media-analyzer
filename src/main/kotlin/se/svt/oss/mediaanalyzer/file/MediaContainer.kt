package se.svt.oss.mediaanalyzer.file

interface MediaContainer : MediaFile {
    val overallBitrate: Long
    val duration: Double
    val audioStreams: List<AudioStream>
}
