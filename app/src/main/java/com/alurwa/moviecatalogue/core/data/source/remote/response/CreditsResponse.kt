package com.alurwa.moviecatalogue.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class CreditsResponse(
    @field:SerializedName("cast")
    val cast: List<CastResponse>
)