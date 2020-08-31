// SPDX-FileCopyrightText: 2020 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.mediaanalyzer.file

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    use = JsonTypeInfo.Id.NAME,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = ImageFile::class, name = "ImageFile"),
    JsonSubTypes.Type(value = VideoFile::class, name = "VideoFile"),
    JsonSubTypes.Type(value = AudioFile::class, name = "AudioFile"),
    JsonSubTypes.Type(value = SubtitleFile::class, name = "SubtitleFile")
)
interface MediaFile {
    val type: String
    val file: String
    val fileSize: Long
    val format: String
}
