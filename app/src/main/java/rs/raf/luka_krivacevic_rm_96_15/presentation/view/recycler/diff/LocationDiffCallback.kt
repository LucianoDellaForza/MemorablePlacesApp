package rs.raf.luka_krivacevic_rm_96_15.presentation.view.recycler.diff

import androidx.recyclerview.widget.DiffUtil
import rs.raf.luka_krivacevic_rm_96_15.data.models.LocationUI

class LocationDiffCallback : DiffUtil.ItemCallback<LocationUI>() {

    override fun areItemsTheSame(oldItem: LocationUI, newItem: LocationUI): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: LocationUI, newItem: LocationUI): Boolean {
        return oldItem.title == newItem.title && oldItem.note == newItem.note
    }


}