package com.kerencev.mynasa.data.retrofit.entities.mars.roverphotos

import android.os.Parcelable

data class Rover(
    val id: Int,
    val landing_date: String,
    val launch_date: String,
    val name: String,
    val status: String
)