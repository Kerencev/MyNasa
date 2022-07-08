package com.kerencev.mynasa.data.retrofit.entities.mars

data class PhotoManifest(
    val landing_date: String,
    val launch_date: String,
    val max_date: String,
    val max_sol: Int,
    val name: String,
    val photos: List<Photo>,
    val status: String,
    val total_photos: Int
)