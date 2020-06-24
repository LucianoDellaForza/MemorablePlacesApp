package rs.raf.memorableplacesapp.presentation.states

import rs.raf.memorableplacesapp.data.models.LocationUI

sealed class LocationsState {
    data class Success(val locations: List<LocationUI>): LocationsState()
    data class Error(val message: String): LocationsState()
}