package rs.raf.luka_krivacevic_rm_96_15.data.repository

import io.reactivex.Completable
import io.reactivex.Observable
import rs.raf.luka_krivacevic_rm_96_15.data.datasource.LocationDao
import rs.raf.luka_krivacevic_rm_96_15.data.models.LocationEntity
import rs.raf.luka_krivacevic_rm_96_15.data.models.LocationUI

class LocationRepositoryImpl (
    private val dataSource: LocationDao) : LocationRepository {

    override fun insert(location: LocationUI): Completable {
        val locationEntity = LocationEntity(location.id, location.longitude, location.latitude, location.title, location.note)
        return dataSource
            .insert(locationEntity)
    }

    override fun update(location: LocationUI): Completable {
        val locationEntity = LocationEntity(location.id, location.longitude, location.latitude, location.title, location.note)
        return dataSource
            .update(locationEntity)
    }

    override fun getAll(): Observable<List<LocationUI>> {
        return dataSource
            .getAll()
            .map {
                it.map {
                    LocationUI(it.id, it.longitude, it.latitude, it.title, it.note)
                }
            }
    }

    override fun getAllWithFilter(filter: String): Observable<List<LocationUI>> {
        return dataSource
            .getByFilter(filter)
            .map {
                it.map {
                    LocationUI(it.id, it.longitude, it.latitude, it.title, it.note)
                }
            }
    }


}