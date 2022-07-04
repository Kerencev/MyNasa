package com.kerencev.mynasa.data.retrofit.entities.photo

data class EarthPhotoDataResponseItem(
    val attitude_quaternions: AttitudeQuaternions,
    val caption: String,
    val centroid_coordinates: CentroidCoordinates,
    val date: String,
    val dscovr_j2000_position: DscovrJ2000PositionX,
    val identifier: String,
    val image: String,
    val lunar_j2000_position: LunarJ2000PositionX,
    val sun_j2000_position: SunJ2000PositionX,
    val version: String
)