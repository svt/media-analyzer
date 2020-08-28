package se.svt.oss.mediaanalyzer

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import se.svt.oss.mediaanalyzer.ffprobe.FfprobeAnalyzer
import se.svt.oss.mediaanalyzer.ffprobe.ProbeResult
import se.svt.oss.mediaanalyzer.file.AudioFile
import se.svt.oss.mediaanalyzer.file.AudioStream
import se.svt.oss.mediaanalyzer.file.ImageFile
import se.svt.oss.mediaanalyzer.file.MediaFile
import se.svt.oss.mediaanalyzer.file.SubtitleFile
import se.svt.oss.mediaanalyzer.file.VideoFile
import se.svt.oss.mediaanalyzer.file.VideoStream
import se.svt.oss.mediaanalyzer.file.toFraction
import se.svt.oss.mediaanalyzer.mediainfo.ImageTrack
import se.svt.oss.mediaanalyzer.mediainfo.MediaInfo
import se.svt.oss.mediaanalyzer.mediainfo.MediaInfoAnalyzer
import se.svt.oss.mediaanalyzer.mediainfo.TextTrack

class MediaAnalyzer
@JvmOverloads constructor(objectMapper: ObjectMapper = ObjectMapper().findAndRegisterModules()) {

    private val ffprobeAnalyzer = FfprobeAnalyzer(objectMapper)
    private val mediaInfoAnalyzer = MediaInfoAnalyzer(objectMapper)

    private val log = KotlinLogging.logger { }

    fun analyze(file: String, probeInterlaced: Boolean = false): MediaFile {
        val mediaInfo = try {
            mediaInfoAnalyzer.analyze(file)
        } catch (e: Exception) {
            log.warn(e) { "Error running mediainfo on $file!" }
            null
        }
        if (mediaInfo?.isImage == true) {
            val imageTrack = mediaInfo.tracksOfType<ImageTrack>().first()
            val generalTrack = mediaInfo.generalTrack
            return ImageFile(
                file = mediaInfo.file,
                fileSize = generalTrack.fileSize,
                format = imageTrack.format,
                width = imageTrack.width,
                height = imageTrack.height
            )
        }
        if (mediaInfo?.isSubtitle == true) {
            val textTrack = mediaInfo.tracksOfType<TextTrack>().first()
            val generalTrack = mediaInfo.generalTrack
            return SubtitleFile(
                file = mediaInfo.file,
                fileSize = generalTrack.fileSize,
                format = textTrack.format
            )
        }

        val probeResult = ffprobeAnalyzer.analyze(file)
        return mergeResults(probeResult, mediaInfo, probeInterlaced)
    }

    private fun mergeResults(
        probeResult: ProbeResult,
        mediaInfo: MediaInfo?,
        probeInterlaced: Boolean
    ): MediaFile {
        val format = probeResult.format ?: throw IllegalStateException("No format detected in ffprobe result!")
        val formatName = mediaInfo?.generalTrack?.format ?: format.format_name
        val file = format.filename
        val fileSize = format.size
        val overallBitrate = format.bit_rate
        val duration = format.duration
        val videoStreams = probeResult.videoStreams
        val audioStreams = probeResult.audioStreams
        if (videoStreams.isEmpty() && audioStreams.isEmpty()) {
            throw RuntimeException("No video or audio streams detected in $file!")
        }
        if (videoStreams.isEmpty()) {
            return AudioFile(
                file = file,
                fileSize = fileSize,
                format = formatName,
                overallBitrate = overallBitrate,
                duration = duration,
                audioStreams = audioStreams(probeResult, mediaInfo)
            )
        }
        return VideoFile(
            file = file,
            fileSize = fileSize,
            format = formatName,
            overallBitrate = overallBitrate,
            duration = duration,
            videoStreams = videoStreams(probeResult, mediaInfo, probeInterlaced),
            audioStreams = audioStreams(probeResult, mediaInfo)
        )
    }

    private fun videoStreams(
        probeResult: ProbeResult,
        mediaInfo: MediaInfo?,
        probeInterlaced: Boolean
    ): List<VideoStream> {
        val videoStreams = probeResult.videoStreams
        val mediaInfoStreams = mediaInfo?.videoTracks?.let {
            if (it.size == videoStreams.size) it else {
                log.warn { "Number of video streams differ! ffprobe: ${videoStreams.size}, mediainfo: ${it.size}. Using only ffprobe values." }
                emptyList()
            }
        } ?: emptyList()

        return videoStreams.mapIndexed { index, ffVideoStream ->
            val videoTrack = mediaInfoStreams.getOrNull(index)
            val duration = ffVideoStream.duration ?: probeResult.format!!.duration
            val numFrames = ffVideoStream.nb_frames
                ?: (ffVideoStream.r_frame_rate.toFraction().toDouble() * duration).toInt()
            val interlaced = if (probeInterlaced) ffprobeAnalyzer.isInterlaced(
                probeResult.format!!.filename,
                index
            ) else videoTrack?.isInterlaced ?: false

            VideoStream(
                format = videoTrack?.format,
                codec = ffVideoStream.codec_name,
                profile = videoTrack?.formatProfile ?: ffVideoStream.profile,
                level = videoTrack?.formatLevel ?: ffVideoStream.level?.toString(),
                width = ffVideoStream.width,
                height = ffVideoStream.height,
                sampleAspectRatio = ffVideoStream.sample_aspect_ratio,
                displayAspectRatio = ffVideoStream.display_aspect_ratio,
                pixelFormat = ffVideoStream.pix_fmt,
                frameRate = ffVideoStream.r_frame_rate,
                duration = duration,
                bitrate = ffVideoStream.bit_rate ?: videoTrack?.bitrate?.toLongOrNull(),
                bitDepth = ffVideoStream.bits_per_raw_sample ?: videoTrack?.bitDepth,
                numFrames = numFrames,
                isInterlaced = interlaced,
                transferCharacteristics = videoTrack?.transferCharacteristics
            )
        }
    }

    private fun audioStreams(probeResult: ProbeResult, mediaInfo: MediaInfo?): List<AudioStream> {
        val audioStreams = probeResult.audioStreams
        val mediaInfoStreams = mediaInfo?.audioTracks?.let {
            if (it.size == audioStreams.size) it else {
                log.warn { "Number of audio streams differ! ffprobe: ${audioStreams.size}, mediainfo: ${it.size}. Using only ffprobe values." }
                emptyList()
            }
        } ?: emptyList()

        return audioStreams.mapIndexed { index, ffAudioStream ->
            val audioTrack = mediaInfoStreams.getOrNull(index)
            AudioStream(
                format = audioTrack?.format,
                codec = ffAudioStream.codec_name,
                duration = ffAudioStream.duration ?: audioTrack?.duration,
                channels = ffAudioStream.channels,
                samplingRate = ffAudioStream.sample_rate ?: audioTrack?.samplingRate,
                bitrate = ffAudioStream.bit_rate ?: audioTrack?.bitrate
            )
        }
    }
}
