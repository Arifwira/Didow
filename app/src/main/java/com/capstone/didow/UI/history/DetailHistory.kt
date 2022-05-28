package com.capstone.didow.UI.history

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class DetailHistory(
    var numberSoal: Int,
    var description: String,
    var visibility: Boolean = false
):Parcelable