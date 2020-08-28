package se.svt.oss.mediaanalyzer.ffprobe

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import se.svt.oss.mediaanalyzer.file.FractionString

// https://github.com/FFmpeg/FFmpeg/blob/master/doc/ffprobe.xsd
/*
case AVMEDIA_TYPE_VIDEO:      return "video";
    case AVMEDIA_TYPE_AUDIO:      return "audio";
    case AVMEDIA_TYPE_DATA:       return "data";
    case AVMEDIA_TYPE_SUBTITLE:   return "subtitle";
    case AVMEDIA_TYPE_ATTACHMENT: return "attachment";
 */
@JsonTypeInfo(
    include = JsonTypeInfo.As.PROPERTY,
    use = JsonTypeInfo.Id.NAME,
    visible = true,
    property = "codec_type",
    defaultImpl = UnknownStream::class
)
@JsonSubTypes(
    JsonSubTypes.Type(value = FfVideoStream::class, name = "video"),
    JsonSubTypes.Type(value = FfAudioStream::class, name = "audio")
)
@JsonIgnoreProperties(ignoreUnknown = true)
interface Stream {
    val index: Int
    val codec_name: String?
    val codec_long_name: String?
    val profile: String?
    val codec_type: String?
    val codec_time_base: FractionString?
    val codec_tag: String
    val codec_tag_string: String
    val r_frame_rate: FractionString
    val avg_frame_rate: FractionString
    val time_base: FractionString
    val start_pts: Long?
    val start_time: Double?
    val duration_ts: Long?
    val duration: Double?
    val bit_rate: Long?
    val max_bitrate: Long?
    val bits_per_raw_sample: Int?
    val nb_frames: Int?
}
