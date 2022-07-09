package com.kerencev.mynasa.data.retrofit.entities.mars.roverphotos

data class Rover(
    val id: Int,
    val landing_date: String,
    val launch_date: String,
    val name: String,
    val status: String
)