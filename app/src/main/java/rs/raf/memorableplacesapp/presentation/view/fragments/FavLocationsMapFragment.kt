package rs.raf.memorableplacesapp.presentation.view.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import rs.raf.memorableplacesapp.R
import rs.raf.memorableplacesapp.data.models.LocationUI
import rs.raf.memorableplacesapp.presentation.contract.LocationContract
import rs.raf.memorableplacesapp.presentation.viewmodel.LocationViewModel

public class FavLocationsMapFragment : Fragment(R.layout.fragment_fav_locations_map),
    OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMapClickListener {

    private val locationViewModel: LocationContract.ViewModel by viewModel<LocationViewModel>()

    var mGoogleApiClient: GoogleApiClient? = null   //objekat za komunikaciju sa googlovim servisima
    var mLastLocation: Location? = null //objekat u koji cuvamo nasu trenutnu lokaciju koju updatujemo sa vremena na vreme
    var mCurrLocationMarker: Marker? = null //marker na mapi koji takodje azuriramo na osnovu novel okacije koja se prosledi
    var mLocationRequest: LocationRequest? = null   //objekat u okviru kog kazemo da hocemo prikaz da se azurira na mpai u odredjenim vremenskim intervalima

    private lateinit var mMap: GoogleMap    //mapa, koristicemo ga npr u onLocationChange metodi da bismo na mapi promenili marker na updatovanu lokaciju


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.mapInFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!

        mMap?.mapType = GoogleMap.MAP_TYPE_NORMAL   //vrsta mape (u ovom slucaju 2d prikaz sa ulicama, onaj uobicajen)
        mMap?.uiSettings?.isZoomControlsEnabled = true  //+/- komande za zoom
        mMap?.uiSettings?.isZoomGesturesEnabled = true  //prstima da se zumira
        mMap?.uiSettings?.isCompassEnabled = true    //prikaz kompasa na mapi

        buildGoogleApiClient()

        if (ActivityCompat.checkSelfPermission(
                this.requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this.requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        mMap?.isMyLocationEnabled = true

        initObservers()
        initListeners()
    }

    private fun initObservers() {
        //sta ce mi uopste observe kada prikazujem samo kada se fragment kreira i pozovem samo locationViewModel.getAllLocations()
        //da se vratim kasnije na ovo
        locationViewModel.locations.observe(viewLifecycleOwner, Observer {
            renderMarkersOnMap(it)
        })
        locationViewModel.getAllLocations()
    }

    private fun renderMarkersOnMap(locations: List<LocationUI>) {
        locations.forEach {
            val latLng = LatLng(it.latitude, it.longitude)
            val markerOptions = MarkerOptions()
            markerOptions.position(latLng)
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
            markerOptions.title(it.title)
            markerOptions.snippet(it.note)
            mMap.addMarker(markerOptions)
        }
    }


    private fun initListeners() {

    }

    @Synchronized
    protected fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this.requireActivity())
            .addConnectionCallbacks(this)   //this -> bas ova klasa (activity) implementira interfejs ConnectionCallbacks (metode njegove)
            .addApi(LocationServices.API)   //API guglov koji koristimo
            .build()
        mGoogleApiClient?.connect()
    }

    //ne treba mi ali neka bleji
    override fun onConnected(p0: Bundle?) {
        mLocationRequest = LocationRequest()
        mLocationRequest?.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)

        if (ContextCompat.checkSelfPermission(this.requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) === PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this)
        }
    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("Not yet implemented")
    }

    override fun onLocationChanged(location: Location?) {
        mLastLocation = location
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker?.remove()
        }
        //ne treba mi marker ovde
        val latLng = LatLng(location!!.latitude, location!!.longitude)
        mMap?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap?.animateCamera(CameraUpdateFactory.zoomTo(11f))
        if (mGoogleApiClient != null) { //ako je vec kreiran client, osvezicemo naseg clijenta o.o
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,
                this)
        }
    }

    override fun onMapClick(p0: LatLng?) {
        TODO("Not yet implemented")
    }


}