package rs.raf.luka_krivacevic_rm_96_15.presentation.view.recycler.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.layout_item_location.*
import rs.raf.luka_krivacevic_rm_96_15.data.models.LocationUI

class LocationViewHolder(
    override val containerView: View,
    onItemClicked: (Int) -> Unit) :RecyclerView.ViewHolder(containerView), LayoutContainer {

    init {
        containerView.setOnClickListener {
            onItemClicked.invoke(adapterPosition)
        }
    }

    fun bind(location: LocationUI) {
        titleTv.text = location.title
        noteTv.text = location.note
    }
}

