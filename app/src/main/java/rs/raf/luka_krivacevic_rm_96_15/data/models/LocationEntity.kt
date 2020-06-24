package rs.raf.luka_krivacevic_rm_96_15.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class LocationEntity (
    @PrimaryKey(autoGenerate = true) var id: Long,
    var longitude: Double,
    var latitude: Double,
    var title: String,
    var note: String
//    var date: Date
)