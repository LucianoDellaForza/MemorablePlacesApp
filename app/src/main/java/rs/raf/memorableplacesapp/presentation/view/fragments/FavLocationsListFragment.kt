package rs.raf.memorableplacesapp.presentation.view.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_fav_locations_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import rs.raf.memorableplacesapp.R
import rs.raf.memorableplacesapp.presentation.contract.LocationContract
import rs.raf.memorableplacesapp.presentation.view.activities.EditLocationActivity
import rs.raf.memorableplacesapp.presentation.view.recycler.adapter.LocationAdapter
import rs.raf.memorableplacesapp.presentation.view.recycler.diff.LocationDiffCallback
import rs.raf.memorableplacesapp.presentation.viewmodel.LocationViewModel

public class FavLocationsListFragment : Fragment(R.layout.fragment_fav_locations_list) {

    private val locationViewModel: LocationContract.ViewModel by viewModel<LocationViewModel>()

    var fabToggle = true

    private lateinit var adapter: LocationAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        initRecycler()
        initObservers()
        initListeners()
    }

    private fun initRecycler() {
        listRvLocations.layoutManager = LinearLayoutManager(this.requireActivity())
        adapter = LocationAdapter(LocationDiffCallback()) {
            //start edit activity
            val intent = Intent(this.requireActivity(), EditLocationActivity::class.java)
            intent.putExtra("locationKey", it)
            startActivity(intent)
        }
        listRvLocations.adapter = adapter
    }

    private fun initObservers() {
        locationViewModel.locations.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
        locationViewModel.getAllLocations()
    }

    private fun initListeners() {
        inputFilterEt.doAfterTextChanged {
            val filter = it.toString()
            locationViewModel.getLocationsWithFilter(filter)
        }

        fab.setOnClickListener {
            fabToggle = !fabToggle
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if(fabToggle) {
                    fab.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24, context?.theme));
                } else {
                    fab.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_keyboard_arrow_down_24, context?.theme));
                }
            } else {
                if(fabToggle) {
                    fab.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24));
                } else {
                    fab.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_keyboard_arrow_down_24));
                }

            }
        }
    }


}