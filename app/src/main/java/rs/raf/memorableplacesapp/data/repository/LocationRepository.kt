package rs.raf.memorableplacesapp.data.repository

import io.reactivex.Completable
import io.reactivex.Observable
import rs.raf.memorableplacesapp.data.models.LocationUI

interface LocationRepository {

    //insert new location
    fun insert(location: LocationUI): Completable

    //update location
    fun update(location: LocationUI): Completable

    //get all locations
    fun getAll(): Observable<List<LocationUI>>

    //get all locations with filter
//    fun getAllWithFilter(filter: String): Observable<List<NoteUI>>

}