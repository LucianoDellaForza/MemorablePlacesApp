package rs.raf.memorableplacesapp.presentation.view.recycler.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import rs.raf.memorableplacesapp.R
import rs.raf.memorableplacesapp.data.models.LocationUI
import rs.raf.memorableplacesapp.presentation.view.recycler.diff.LocationDiffCallback
import rs.raf.memorableplacesapp.presentation.view.recycler.viewholder.LocationViewHolder

class LocationAdapter (
    locationDiffItemCallback: LocationDiffCallback,
    private val onLocationClicked: (LocationUI) -> Unit) : ListAdapter<LocationUI, LocationViewHolder>(LocationDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.layout_item_location, parent, false)
        return LocationViewHolder(view) {
            val location = getItem(it)
            onLocationClicked.invoke(location)
        }
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}