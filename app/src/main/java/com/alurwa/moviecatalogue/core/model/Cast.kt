package com.alurwa.moviecatalogue.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cast(
        val name: String,
        val profilePath: String?
) : Parcelable
