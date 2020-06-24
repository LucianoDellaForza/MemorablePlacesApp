package rs.raf.luka_krivacevic_rm_96_15.data.datasource

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Observable
import rs.raf.luka_krivacevic_rm_96_15.data.models.LocationEntity

@Dao
abstract class LocationDao {

    //add new location
    @Insert( onConflict = OnConflictStrategy.REPLACE )
    abstract fun insert(entity: LocationEntity): Completable

    //update location
    @Update
    abstract fun update(entity: LocationEntity): Completable

    //get all locations
    @Query("SELECT * FROM locations")
    abstract fun getAll(): Observable<List<LocationEntity>>

    //get with filter
    @Query("SELECT * FROM locations WHERE title LIKE :filter || '%' OR note LIKE :filter || '%'")
    abstract fun getByFilter(filter: String): Observable<List<LocationEntity>>
}