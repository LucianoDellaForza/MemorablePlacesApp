package rs.raf.luka_krivacevic_rm_96_15.presentation.view.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_edit_location.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import rs.raf.luka_krivacevic_rm_96_15.R
import rs.raf.luka_krivacevic_rm_96_15.data.models.LocationUI
import rs.raf.luka_krivacevic_rm_96_15.presentation.contract.LocationContract
import rs.raf.luka_krivacevic_rm_96_15.presentation.viewmodel.LocationViewModel

class EditLocationActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private val locationViewModel: LocationContract.ViewModel by viewModel<LocationViewModel>()

    private var locationToEdit: LocationUI? = null
    var mCustomLocation: LatLng? = LatLng(44.7866, 20.4489) //postavim je na poziciju lokacije iz fragmenta (u parseIntent())

    var mGoogleApiClient: GoogleApiClient? = null
    private lateinit var mMap: GoogleMap



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_location)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapForEdit) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap?.mapType = GoogleMap.MAP_TYPE_NORMAL   //vrsta mape (u ovom slucaju 2d prikaz sa ulicama, onaj uobicajen)
        mMap?.uiSettings?.isZoomControlsEnabled = true  //+/- komande za zoom
        mMap?.uiSettings?.isZoomGesturesEnabled = true  //prstima da se zumira
        mMap?.uiSettings?.isCompassEnabled = true    //prikaz kompasa na mapi
        //Initialize Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,  Manifest.permission.ACCESS_FINE_LOCATION) === PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient()
            }
        } else {
            buildGoogleApiClient()
        }

        parseIntent()   //edit location od fragmenta koji mi stize
        initListeners()
    }

    private fun parseIntent() {
        intent?.let {
            locationToEdit = it.getParcelableExtra<LocationUI>("locationKey")
            if (locationToEdit != null) {
                editTitleEt.setText(locationToEdit?.title)
                editNoteEt.setText(locationToEdit?.note)
            }
            mCustomLocation = LatLng(locationToEdit!!.latitude, locationToEdit!!.longitude)
            val markerOptions = MarkerOptions()
            markerOptions.position(mCustomLocation!!)
            markerOptions.title(mCustomLocation!!.latitude.toString() + " : " + mCustomLocation!!.longitude.toString())
            //clear previos marker click position
            mMap.clear()
            //add marker on map
            mMap.addMarker(markerOptions);
            mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(mCustomLocation, 15F))
        }
    }

    private fun initListeners() {
        //set marker on map click
        mMap.setOnMapClickListener {
            val markerOptions = MarkerOptions()
            //set marker position
            markerOptions.position(it)
            //set longitutde and latitude on marker
            markerOptions.title(it.latitude.toString() + " : " + it.longitude.toString())
            //clear previos marker click position
            mMap.clear()
            //add marker on map
            mMap.addMarker(markerOptions);
            mCustomLocation = it
        }

        editCancelBtn.setOnClickListener {
            finish()
        }

        editSaveBtn.setOnClickListener {
            val editedTitle = editTitleEt.text.toString()
            val editedNote = editNoteEt.text.toString()

            locationViewModel.updateLocation(LocationUI(locationToEdit!!.id, mCustomLocation!!.longitude, mCustomLocation!!.latitude, editedTitle, editedNote))

            Toast.makeText(this, "Location updated", Toast.LENGTH_SHORT).show() //ovo bi trebalo u locationViewModel on success
            finish()
        }
    }

    @Synchronized
    protected fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addApi(LocationServices.API)   //API guglov koji koristimo
            .build()
        mGoogleApiClient?.connect()
    }

    override fun onMapClick(p0: LatLng?) {
        TODO("Not yet implemented")
    }
}