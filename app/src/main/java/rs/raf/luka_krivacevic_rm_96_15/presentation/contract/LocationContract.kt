package rs.raf.luka_krivacevic_rm_96_15.presentation.contract

import androidx.lifecycle.LiveData
import rs.raf.luka_krivacevic_rm_96_15.data.models.LocationUI
import rs.raf.luka_krivacevic_rm_96_15.presentation.states.LocationsState

interface LocationContract {
    interface ViewModel {
        val locationsState: LiveData<LocationsState>

        fun insertLocation(location: LocationUI)
        fun updateLocation(location: LocationUI)
        fun getAllLocations()
        fun getLocationsWithFilter(filter: String)
    }
}