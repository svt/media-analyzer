// SPDX-FileCopyrightText: 2020 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.mediaanalyzer

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.unmockkAll
import org.assertj.core.groups.Tuple
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import se.svt.oss.mediaanalyzer.Assertions.assertThat
import se.svt.oss.mediaanalyzer.ffprobe.FfprobeAnalyzer
import se.svt.oss.mediaanalyzer.file.AudioFile
import se.svt.oss.mediaanalyzer.file.ImageFile
import se.svt.oss.mediaanalyzer.file.SubtitleFile
import se.svt.oss.mediaanalyzer.file.VideoFile
import se.svt.oss.mediaanalyzer.mediainfo.MediaInfoAnalyzer

internal class MediaAnalyzerTest {

    private val objectMapper = ObjectMapper().findAndRegisterModules()
    private val file = "/path/to/file"

    @BeforeEach
    internal fun setUp() {
        mockkConstructor(MediaInfoAnalyzer::class, FfprobeAnalyzer::class)
    }

    @AfterEach
    internal fun tearDown() {
        unmockkAll()
    }

    @Test
    fun test() {
        mockMediaInfo("/mediainfo-reg.json")
        mockFfprobe("/ffprobe-reg.json")

        val videoFile = MediaAnalyzer().analyze(file) as VideoFile
        assertThat(videoFile)
            .hasFile("/core/programs/processed/svt_hbg_2020-02-19_070459_070801.dv")
            .hasFileSize(1338048000)
            .hasFormat("DV")
            .hasOverallBitrate(57600000)
            .hasDuration(185.84)

        assertThat(videoFile.videoStreams).hasSize(1)
        assertThat(videoFile.videoStreams[0])
            .hasFormat("DV")
            .hasCodec("dvvideo")
            .hasWidth(720)
            .hasHeight(576)
            .hasSampleAspectRatio("16:15")
            .hasDisplayAspectRatio("4:3")
            .hasPixelFormat("yuv422p")
            .hasFrameRate("25/1")
            .hasDuration(185.84)
            .hasBitrate(25000000)
            .hasBitDepth(8)
            .hasNumFrames(4646)
            .isInterlaced
        assertThat(videoFile.audioStreams).hasSize(2)
        assertThat(videoFile.audioStreams[0])
            .hasFormat("PCM")
            .hasCodec("pcm_s16le")
            .hasDuration(185.84)
            .hasChannels(2)
            .hasSamplingRate(48000)
            .hasBitrate(1536000)
    }

    @Test
    fun testHls() {
        mockMediaInfo("/mediainfo-hls.json")
        mockFfprobe("/ffprobe-hls.json")
        val videoFile = MediaAnalyzer().analyze(file) as VideoFile

        assertThat(videoFile).hasFormat("HLS")
        assertThat(videoFile.audioStreams).hasSize(1)
        assertThat(videoFile.videoStreams).hasSize(5)
        assertThat(videoFile.videoStreams).allSatisfy { v ->
            assertThat(v)
                .hasFormat("AVC")
                .hasCodec("h264")
                .hasSampleAspectRatio("1:1")
                .hasDisplayAspectRatio("16:9")
                .hasPixelFormat("yuv420p")
                .hasFrameRate("25/1")
                .hasDuration(169.6)
                .hasBitDepth(8)
                .hasNumFrames(4240)
                .isNotInterlaced
        }
        assertThat(videoFile.videoStreams).extracting("width", "height")
            .containsExactly(
                Tuple(640, 360),
                Tuple(416, 234),
                Tuple(960, 540),
                Tuple(1280, 720),
                Tuple(1920, 1080)
            )
    }

    @Test
    fun testMediaInfoSource() {
        mockMediaInfo("/mediainfo-hls.json")
        val videoTrack = MediaInfoAnalyzer().analyze(file, true).videoTracks.firstOrNull()
        assertThat(videoTrack?.extra?.get("Source") as? String)
            .describedAs("extra.Source")
            .isEqualTo("hls-video1/hls-video1.m3u8")
    }

    @Test
    fun testMediaInfoNoAudio() {
        mockMediaInfo("/mediainfo-audio-corrupt.json")
        mockFfprobe("/ffprobe-audio-corrupt.json")

        val videoFile = MediaAnalyzer().analyze(file) as VideoFile
        assertThat(videoFile.audioStreams).hasSize(2)
    }

    @Test
    fun testVideoAndAudioStreamsDiffer() {
        mockMediaInfo("/mediainfo-hls-mod.json")
        mockFfprobe("/ffprobe-hls.json")

        val videoFile = MediaAnalyzer().analyze(file) as VideoFile
        assertThat(videoFile.videoStreams)
            .hasSize(5)
            .allSatisfy {
                assertThat(it)
                    .hasFormat(null)
                    .hasCodec("h264")
            }
    }

    @Test
    fun testProbeInterlaced() {
        mockMediaInfo("/mediainfo-reg.json")
        mockFfprobe("/ffprobe-reg.json")
        mockFfprobeInterlaced(false)

        val videoFile = MediaAnalyzer().analyze(file, true) as VideoFile
        assertThat(videoFile.videoStreams).hasSize(1)
        assertThat(videoFile.videoStreams[0]).isNotInterlaced
    }

    @Test
    fun testAudioOnly() {
        mockMediaInfo("/mediainfo-audio.json")
        mockFfprobe("/ffprobe-audio.json")

        val audioFile = MediaAnalyzer()
            .analyze(file) as AudioFile

        assertThat(audioFile)
            .hasFile("/core/programs/completed/1120201-011A/1b456087-772b-484b-b027-5c0b98b670cc/PG-1120201-011A-ALFONSABERG1-02_STEREO.mp4")
            .hasFileSize(15011925)
            .hasFormat("MPEG-4")
            .hasOverallBitrate(193514)
            .hasDuration(620.603)

        assertThat(audioFile.audioStreams).hasSize(1)
        assertThat(audioFile.audioStreams[0])
            .hasFormat("AAC")
            .hasCodec("aac")
            .hasDuration(620.56)
            .hasChannels(2)
            .hasSamplingRate(48000)
            .hasBitrate(192001)
    }

    @Test
    fun testImage() {
        mockMediaInfo("/mediainfo-image.json")

        val imageFile = MediaAnalyzer().analyze(file) as ImageFile

        assertThat(imageFile)
            .hasFile("/core/programs/completed/1120201-011A/1b456087-772b-484b-b027-5c0b98b670cc/PG-1120201-011A-ALFONSABERG1-02_12x20_160x90_thumbnail_map.jpg")
            .hasFormat("JPEG")
            .hasFileSize(518430)
            .hasWidth(1920)
            .hasHeight(1800)
    }

    @Test
    fun testSubtitle() {
        mockMediaInfo("/mediainfo-subtitle.json")
        val subtitleFile = MediaAnalyzer().analyze(file) as SubtitleFile

        assertThat(subtitleFile)
            .hasFormat("WebVTT")
            .hasFile("http://svt-vod-1l.akamaized.net/d0/world/20200220/f44e0b42-bdfc-40cb-ab23-5852c474deb8/text/closed.vtt")
            .hasFileSize(7547)
    }

    @Test
    fun testMediaInfoFails() {
        mockMediaInfoFails()
        mockFfprobe("/ffprobe-reg.json")

        val videoFile = MediaAnalyzer().analyze(file) as VideoFile
        assertThat(videoFile)
            .hasFile("/core/programs/processed/svt_hbg_2020-02-19_070459_070801.dv")
            .hasFileSize(1338048000)
            .hasFormat("dv")
            .hasOverallBitrate(57600000)
            .hasDuration(185.84)

        assertThat(videoFile.videoStreams).hasSize(1)
        assertThat(videoFile.videoStreams[0])
            .hasFormat(null)
            .hasCodec("dvvideo")
            .hasWidth(720)
            .hasHeight(576)
            .hasSampleAspectRatio("16:15")
            .hasDisplayAspectRatio("4:3")
            .hasPixelFormat("yuv422p")
            .hasFrameRate("25/1")
            .hasDuration(185.84)
            .hasBitrate(25000000)
            .hasBitDepth(null)
            .hasNumFrames(4646)

        assertThat(videoFile.audioStreams).hasSize(2)
        assertThat(videoFile.audioStreams[0])
            .hasFormat(null)
            .hasCodec("pcm_s16le")
            .hasDuration(185.84)
            .hasChannels(2)
            .hasSamplingRate(48000)
            .hasBitrate(1536000)
    }

    private fun mockFfprobe(jsonPath: String) {
        every { anyConstructed<FfprobeAnalyzer>().analyze(file, any()) } returns parse(jsonPath)
    }

    private fun mockFfprobeInterlaced(interlaced: Boolean) {
        every { anyConstructed<FfprobeAnalyzer>().isInterlaced(any(), any(), any()) } returns interlaced
    }

    private fun mockMediaInfoFails() {
        every { anyConstructed<MediaInfoAnalyzer>().analyze(file, any()) } throws RuntimeException("mediainfo failed!")
    }

    private fun mockMediaInfo(jsonPath: String) {
        every { anyConstructed<MediaInfoAnalyzer>().analyze(file, any()) } returns parse(jsonPath)
    }

    private inline fun <reified T> parse(file: String): T = objectMapper.readValue<T>(javaClass.getResource(file))
}
