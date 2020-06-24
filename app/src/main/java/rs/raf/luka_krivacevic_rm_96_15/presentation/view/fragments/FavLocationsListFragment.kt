package rs.raf.luka_krivacevic_rm_96_15.presentation.view.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_fav_locations_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import rs.raf.luka_krivacevic_rm_96_15.R
import rs.raf.luka_krivacevic_rm_96_15.presentation.contract.LocationContract
import rs.raf.luka_krivacevic_rm_96_15.presentation.states.LocationsState
import rs.raf.luka_krivacevic_rm_96_15.presentation.view.activities.EditLocationActivity
import rs.raf.luka_krivacevic_rm_96_15.presentation.view.recycler.adapter.LocationAdapter
import rs.raf.luka_krivacevic_rm_96_15.presentation.view.recycler.diff.LocationDiffCallback
import rs.raf.luka_krivacevic_rm_96_15.presentation.viewmodel.LocationViewModel

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
        locationViewModel.locationsState.observe(viewLifecycleOwner, Observer {
            renderState(it)
        })
        locationViewModel.getAllLocations()
    }

    private fun renderState(state: LocationsState) {
        when (state) {
            is LocationsState.Success -> {
                adapter.submitList(state.locations)
            }
            is LocationsState.Error -> {
                Toast.makeText(this.requireActivity(), state.message, Toast.LENGTH_SHORT).show()
            }
        }
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