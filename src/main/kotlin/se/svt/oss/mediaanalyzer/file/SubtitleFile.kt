// SPDX-FileCopyrightText: 2020 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.mediaanalyzer.file

import com.fasterxml.jackson.annotation.JsonTypeName

@JsonTypeName("SubtitleFile")
data class SubtitleFile(
    override val file: String,
    override val fileSize: Long,
    override val format: String
) : MediaFile {
    override val type: String
        get() = "SubtitleFile"
}
