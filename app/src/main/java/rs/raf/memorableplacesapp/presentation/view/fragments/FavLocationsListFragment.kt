package rs.raf.memorableplacesapp.presentation.view.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import rs.raf.memorableplacesapp.R

public class FavLocationsListFragment : Fragment(R.layout.fragment_fav_locations_list) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        // initRecycler()
        initObservers()
    }

    private fun initObservers() {

    }

}