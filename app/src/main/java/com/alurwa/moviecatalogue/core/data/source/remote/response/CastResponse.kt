package com.alurwa.moviecatalogue.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class CastResponse(
        @field:SerializedName("name")
        val name: String,

        @field:SerializedName("profile_path")
        val profilePath: String?,

        @field:SerializedName("character")
        val character: String
)
