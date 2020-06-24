package rs.raf.memorableplacesapp.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class LocationUI (
    val id: Long,
    var longitude: Double,
    var latitude: Double,
    var title: String,
    var note: String
//    var date: Date
) : Parcelable //    -- ako se secam, da moze da se posalje drugom activitiju preko Intenta