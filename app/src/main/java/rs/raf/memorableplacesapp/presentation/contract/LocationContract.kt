package rs.raf.memorableplacesapp.presentation.contract

import androidx.lifecycle.LiveData
import rs.raf.memorableplacesapp.data.models.LocationUI

interface LocationContract {
    interface ViewModel {
        val locations: LiveData<List<LocationUI>>

        fun insertLocation(location: LocationUI)
        fun updateLocation(location: LocationUI)
        fun getAllLocations()

        //fun getLocationsWithFilter(filter: String)
    }
}