package se.svt.oss.mediaanalyzer.file

import com.fasterxml.jackson.annotation.JsonTypeName

@JsonTypeName("ImageFile")
data class ImageFile(
    override val file: String,
    override val fileSize: Long,
    override val format: String,
    val width: Int,
    val height: Int
) : MediaFile {
    override val type: String
        get() = "ImageFile"
}
