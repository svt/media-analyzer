// SPDX-FileCopyrightText: 2020 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.mediaanalyzer

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import se.svt.oss.mediaanalyzer.Assertions.assertThat
import se.svt.oss.mediaanalyzer.Assertions.assertThatThrownBy
import se.svt.oss.mediaanalyzer.file.AudioFile
import se.svt.oss.mediaanalyzer.file.VideoFile

@Tag("integrationTest")
class MediaAnalyzerIntegrationTest {

    @Test
    fun testNonExistingFile() {
        val file = "path/to/nonexisting"
        assertThatThrownBy { MediaAnalyzer().analyze(file) }
            .hasMessage("ffprobe failed for $file: No such file or directory")
    }

    @Test
    fun testVideoFile() {
        val file = javaClass.getResource("/test.mp4").file
        val videoFile = MediaAnalyzer().analyze(file, true) as VideoFile
        assertThat(videoFile)
            .hasFormat("MPEG-4")
            .hasDuration(10.01)
            .hasOverallBitrate(2425568)
            .hasFileSize(3034992)

        assertThat(videoFile.file).endsWith("/test.mp4")

        assertThat(videoFile.videoStreams).hasSize(1)
        assertThat(videoFile.highestBitrateVideoStream)
            .isNotInterlaced
            .hasFormat("AVC")
            .hasCodec("h264")
            .hasProfile("High 4:2:2")
            .hasLevel("5.1")
            .hasWidth(1920)
            .hasHeight(1080)
            .hasSampleAspectRatio("1:1")
            .hasDisplayAspectRatio("16:9")
            .hasPixelFormat("yuv422p10le")
            .hasFrameRate("25/1")
            .hasDuration(10.0)
            .hasBitrate(2037046)
            .hasBitDepth(10)
            .hasNumFrames(250)
            .hasTransferCharacteristics(null)
            .hasCodecTagString("avc1")

        assertThat(videoFile.audioStreams).hasSize(1)
        assertThat(videoFile.audioStreams)
            .allSatisfy {
                assertThat(it)
                    .hasFormat("AC-3")
                    .hasCodec("ac3")
                    .hasDuration(10.01)
                    .hasSamplingRate(48000)
                    .hasChannels(6)
            }
        assertThat(videoFile.audioStreams)
            .extracting("bitrate")
            .containsExactly(
                384000L
            )
    }

    @Test
    fun testAudio() {
        val file = javaClass.getResource("/test_audio_file.mp4").file

        val audioFile = MediaAnalyzer()
            .analyze(file) as AudioFile

        assertThat(audioFile)
            .hasFormat("MPEG-4")
            .hasOverallBitrate(133124)
            .hasDuration(2.621)

        assertThat(audioFile.audioStreams).hasSize(1)
        assertThat(audioFile.audioStreams.first())
            .hasFormat("AAC")
            .hasCodec("aac")
            .hasChannels(2)
            .hasDuration(2.621)
            .hasSamplingRate(48000)
            .hasBitrate(128104)
            .hasProfile("LC")
    }

    @Test
    fun testAudioHeAac() {
        val file = javaClass.getResource("/he-aac_test.mp4").file

        val audioFile = MediaAnalyzer()
            .analyze(file) as AudioFile

        assertThat(audioFile)
            .hasFormat("MPEG-4")
            .hasOverallBitrate(67625)
            .hasDuration(5.000)

        assertThat(audioFile.audioStreams).hasSize(1)
        assertThat(audioFile.audioStreams.first())
            .hasFormat("AAC")
            .hasCodec("aac")
            .hasChannels(2)
            .hasDuration(5.000)
            .hasSamplingRate(48000)
            .hasBitrate(64183)
            .hasProfile("HE-AAC")
    }

    @Test
    fun ffprobeInputParams() {
        val file = javaClass.getResource("/rawaudio.tts").file

        val audioFile = MediaAnalyzer()
            .analyze(
                file,
                ffprobeInputParams = linkedMapOf(
                    "f" to "s16le",
                    "ac" to "1",
                    "ar" to "22050",
                )
            )
        assertThat(audioFile).isInstanceOf(AudioFile::class.java)
        assertThat(audioFile as AudioFile)
            .hasDuration(5.0)
    }
}
