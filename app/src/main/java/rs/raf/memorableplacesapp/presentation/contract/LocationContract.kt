package rs.raf.memorableplacesapp.presentation.contract

import androidx.lifecycle.LiveData
import rs.raf.memorableplacesapp.data.models.LocationUI
import rs.raf.memorableplacesapp.presentation.states.LocationsState

interface LocationContract {
    interface ViewModel {
        val locationsState: LiveData<LocationsState>

        fun insertLocation(location: LocationUI)
        fun updateLocation(location: LocationUI)
        fun getAllLocations()
        fun getLocationsWithFilter(filter: String)
    }
}