// SPDX-FileCopyrightText: 2020 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.mediaanalyzer.ffprobe

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import se.svt.oss.mediaanalyzer.file.FractionString

@JsonIgnoreProperties(ignoreUnknown = true)
data class UnknownStream(
    override val index: Int,
    override val codec_name: String?,
    override val codec_long_name: String?,
    override val profile: String?,
    override val codec_type: String?,
    override val codec_time_base: FractionString?,
    override val codec_tag: String,
    override val codec_tag_string: String,
    override val r_frame_rate: FractionString,
    override val avg_frame_rate: FractionString,
    override val time_base: FractionString,
    override val start_pts: Long?,
    override val start_time: Double?,
    override val duration_ts: Long?,
    override val duration: Double?,
    override val bit_rate: Long?,
    override val max_bitrate: Long?,
    override val bits_per_raw_sample: Int?,
    override val nb_frames: Int?
) : Stream
