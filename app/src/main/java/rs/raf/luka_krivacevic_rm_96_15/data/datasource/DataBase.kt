package rs.raf.luka_krivacevic_rm_96_15.data.datasource

import androidx.room.Database
import androidx.room.RoomDatabase
import rs.raf.luka_krivacevic_rm_96_15.data.models.LocationEntity

@Database(
    entities = [LocationEntity::class],
    version = 1,
    exportSchema = false)
//@TypeConverters(DateConverter::class) - ako ubacim datum
abstract class DataBase : RoomDatabase() {
    //Getteri za sve DAO-e
    abstract fun getLocationDao(): LocationDao
}