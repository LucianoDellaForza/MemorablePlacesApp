package rs.raf.luka_krivacevic_rm_96_15.presentation.states

import rs.raf.luka_krivacevic_rm_96_15.data.models.LocationUI

sealed class LocationsState {
    data class Success(val locations: List<LocationUI>): LocationsState()
    data class Error(val message: String): LocationsState()
}