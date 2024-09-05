// SPDX-FileCopyrightText: 2020 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.mediaanalyzer.mediainfo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
    include = JsonTypeInfo.As.PROPERTY,
    use = JsonTypeInfo.Id.NAME,
    property = "@type",
    defaultImpl = OtherTrack::class
)
@JsonSubTypes(
    JsonSubTypes.Type(
        value = GeneralTrack::class,
        name = "General"
    ),
    JsonSubTypes.Type(
        value = VideoTrack::class,
        name = "Video"
    ),
    JsonSubTypes.Type(
        value = AudioTrack::class,
        name = "Audio"
    ),
    JsonSubTypes.Type(
        value = ImageTrack::class,
        name = "Image"
    ),
    JsonSubTypes.Type(
        value = TextTrack::class,
        name = "Text"
    ),
    JsonSubTypes.Type(
        value = OtherTrack::class,
        name = "Other"
    )
)
@JsonIgnoreProperties(ignoreUnknown = true)
interface Track {
    @get:JsonProperty("Format")
    val format: String
    @get:JsonProperty("extra")
    val extra: Map<String, Any>
}
