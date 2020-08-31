// SPDX-FileCopyrightText: 2020 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.mediaanalyzer.file

interface MediaContainer : MediaFile {
    val overallBitrate: Long
    val duration: Double
    val audioStreams: List<AudioStream>
}
