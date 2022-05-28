package com.capstone.didow.UI.history

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class History(
    var date: String,
    var averageSukuKata: String,
    var tipe: String
):Parcelable