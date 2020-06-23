package rs.raf.memorableplacesapp.presentation.view.activities

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import kotlinx.android.synthetic.main.activity_maps.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import rs.raf.memorableplacesapp.R
import rs.raf.memorableplacesapp.data.models.LocationUI
import rs.raf.memorableplacesapp.presentation.contract.LocationContract
import rs.raf.memorableplacesapp.presentation.viewmodel.LocationViewModel

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMapClickListener {

    private val locationViewModel: LocationContract.ViewModel by viewModel<LocationViewModel>()

    var mGoogleApiClient: GoogleApiClient? = null   //objekat za komunikaciju sa googlovim servisima
    var mLastLocation: Location? = null //objekat u koji cuvamo nasu trenutnu lokaciju koju updatujemo sa vremena na vreme
    var mCurrLocationMarker: Marker? = null //marker na mapi koji takodje azuriramo na osnovu novel okacije koja se prosledi
    var mLocationRequest: LocationRequest? = null   //objekat u okviru kog kazemo da hocemo prikaz da se azurira na mpai u odredjenim vremenskim intervalima

    var mCustomLocation: LatLng? = null //koristim za kad korisnik klikne na mapu da postavi marker za custom lokaciju

    private lateinit var mMap: GoogleMap    //mapa, koristicemo ga npr u onLocationChange metodi da bismo na mapi promenili marker na updatovanu lokaciju

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {   //ako je android verzija veca od 23, moramo EKSPLICITNO da trazimo dozvolu
            checkLocationPermission()
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
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
                mMap?.isMyLocationEnabled = true    //GPS dugme za trenutnu lokaciju
            }
        } else {
            buildGoogleApiClient()
            mMap?.isMyLocationEnabled = true
        }

        initListeners() //for onMapClickListener and buttons
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

        cancelBtn.setOnClickListener {
            finish()
        }

        saveBtn.setOnClickListener {
            val title = titleEt.text.toString()
            val note = noteEt.text.toString()

            if (mCustomLocation == null)    //ako korisnik nije stavio custom marker negde na mapu
                locationViewModel.insertLocation(LocationUI(0, mLastLocation?.longitude!!, mLastLocation?.latitude!!, title, note))
            else
                locationViewModel.insertLocation(LocationUI(0, mCustomLocation!!.longitude, mCustomLocation!!.latitude, title, note))

            Toast.makeText(this, "Location added in favourites", Toast.LENGTH_SHORT).show() //ovo bi trebalo u locationViewModel on success
            finish()
        }
    }

    //kreira (instancira) objekat koji koristimo mi kao client za pracanje dogadjaja kao sto su prekid konekcije/uspesna konekcija/promena lokacije...
    @Synchronized
    protected fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)   //this -> bas ova klasa (activity) implementira interfejs ConnectionCallbacks (metode njegove)
            .addOnConnectionFailedListener(this)    //isto
            .addApi(LocationServices.API)   //API guglov koji koristimo
            .build()
        mGoogleApiClient?.connect()
    }

    //metod koji je pozvan kada se povezemo sa google servisima tj sve je proslo u redu
    override fun onConnected(p0: Bundle?) {
        mLocationRequest = LocationRequest()
        mLocationRequest?.setInterval(3000) //svakih 5 sekundi da se updatuje nas polozaj na mapi i svakih sekundi ce se pozivati metoda onLocationChanged() (ako je lokacija drugacija od prethodne)
        mLocationRequest?.setFastestInterval(1000)
        mLocationRequest?.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) === PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this)
        }
    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        // Add a marker in Belgrade and move the camera
        val belgrade = LatLng(44.7866, 20.4489)
        mMap?.addMarker(MarkerOptions().position(belgrade).title("Marker in Belgrade"))
        mMap?.moveCamera(CameraUpdateFactory.newLatLng(belgrade))
    }

    //funkcija koja proverava permisije
    fun checkLocationPermission(): Boolean {
        return if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !== PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            }
            false
        } else {
            true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                        === PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient()
                        }
                        mMap?.isMyLocationEnabled = true
                    }
                } else {
                    Toast.makeText(this, "permission denied",
                        Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }



    //poziva se svaki put kada se promeni lokacija (kada nova nije ista kao trenutna lokacija)
    override fun onLocationChanged(location: Location) {
        mLastLocation = location
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker?.remove()
        }
        //Showing Current Location Marker on Map
        val latLng = LatLng(location.latitude, location.longitude)  //uzimamo geo duzinu i sirinu
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)

//        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        val provider = locationManager.getBestProvider(Criteria(), true)
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !== PackageManager.PERMISSION_GRANTED &&
//            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !== PackageManager.PERMISSION_GRANTED) {
//            return
//        }
//        val locations = locationManager.getLastKnownLocation(provider!!)
//        val providerList = locationManager.allProviders

        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        mCurrLocationMarker = mMap?.addMarker(markerOptions)    //stavljamo marker na mapu
        mMap?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap?.animateCamera(CameraUpdateFactory.zoomTo(11f))
        if (mGoogleApiClient != null) { //ako je vec kreiran client, osvezicemo naseg clijenta o.o
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,
                this)
        }

    }

    //ne radi?
    override fun onMapClick(p0: LatLng?) {
//        val longitude = p0?.longitude
//        val latitude = p0?.latitude
//
//        locationViewModel.insertLocation(LocationUI(0, longitude!!, latitude!!, "title", "note" ))
    }

    companion object {
        const val MY_PERMISSIONS_REQUEST_LOCATION = 99
    }
}