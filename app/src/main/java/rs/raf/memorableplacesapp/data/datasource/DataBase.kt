package rs.raf.memorableplacesapp.data.datasource

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import rs.raf.memorableplacesapp.data.models.LocationEntity

@Database(
    entities = [LocationEntity::class],
    version = 1,
    exportSchema = false)
//@TypeConverters(DateConverter::class) - ako ubacim datum
abstract class DataBase : RoomDatabase() {
    //Getteri za sve DAO-e
    abstract fun getLocationDao(): LocationDao
}