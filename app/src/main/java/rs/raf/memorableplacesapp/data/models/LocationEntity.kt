package rs.raf.memorableplacesapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "locations")
data class LocationEntity (
    @PrimaryKey(autoGenerate = true) var id: Long,
    var longitude: Double,
    var latitude: Double,
    var title: String,
    var note: String
//    var date: Date
)