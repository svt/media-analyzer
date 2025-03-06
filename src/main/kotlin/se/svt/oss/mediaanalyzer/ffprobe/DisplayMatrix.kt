// SPDX-FileCopyrightText: 2020 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.mediaanalyzer.ffprobe

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class DisplayMatrix(
    override val side_data_type: String?,
    val rotation: Int?,
) : SideData
