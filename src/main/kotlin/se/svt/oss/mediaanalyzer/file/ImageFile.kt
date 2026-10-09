// SPDX-FileCopyrightText: 2020 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.mediaanalyzer.file

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonTypeName

@JsonTypeName("ImageFile")
@JsonIgnoreProperties(ignoreUnknown = true)
data class ImageFile(
    override val file: String,
    override val fileSize: Long,
    override val format: String,
    val width: Int,
    val height: Int,
) : MediaFile {
    override val type: String
        get() = "ImageFile"
}
