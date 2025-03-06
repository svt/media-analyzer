package se.svt.oss.mediaanalyzer.ffprobe

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
    include = JsonTypeInfo.As.PROPERTY,
    use = JsonTypeInfo.Id.NAME,
    visible = true,
    property = "side_data_type",
    defaultImpl = UnknownSideData::class
)
@JsonSubTypes(
    JsonSubTypes.Type(value = DisplayMatrix::class, name = "Display Matrix"),
)
interface SideData {
    val side_data_type: String?
}
