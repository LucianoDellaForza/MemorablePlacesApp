package rs.raf.memorableplacesapp.data.models

import java.util.*

//@Parselize
data class LocationUI (
    val id: Long,
    var longitude: Double,
    var latitude: Double,
    var title: String,
    var note: String
//    var date: Date
) //: Parselable    -- ako se secam, da moze da se posalje drugom activitiju preko Intenta