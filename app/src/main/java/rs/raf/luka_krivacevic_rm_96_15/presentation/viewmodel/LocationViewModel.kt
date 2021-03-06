package rs.raf.luka_krivacevic_rm_96_15.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import rs.raf.luka_krivacevic_rm_96_15.data.models.LocationUI
import rs.raf.luka_krivacevic_rm_96_15.data.repository.LocationRepository
import rs.raf.luka_krivacevic_rm_96_15.presentation.contract.LocationContract
import rs.raf.luka_krivacevic_rm_96_15.presentation.states.LocationsState
import timber.log.Timber
import java.util.concurrent.TimeUnit

class LocationViewModel (
    private val locationRepository: LocationRepository) : ViewModel(), LocationContract.ViewModel {

    private val subscriptions = CompositeDisposable()

    override val locationsState: MutableLiveData<LocationsState> = MutableLiveData()

    private val publishSubject: PublishSubject<String> = PublishSubject.create()

    init {
        val locationsDisposable = publishSubject
            .debounce(200, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .switchMap {
                locationRepository
                    .getAllWithFilter(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError {
                        Timber.e("Error in publish subject")
                        Timber.e(it)
                    }
            }
            .subscribe(
                {
                    locationsState.value = LocationsState.Success(it)
                },
                {
                    locationsState.value = LocationsState.Error("Error happened while fetching data from db")
                    Timber.e(it)
                }
            )
        subscriptions.add(locationsDisposable)
    }

    override fun insertLocation(location: LocationUI) {
        val subscription = locationRepository
            .insert(location)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Timber.e("Location inserted")
                },
                {
                    Timber.e("Error happened while inserting location")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun updateLocation(location: LocationUI) {
        val subscription = locationRepository
            .update(location)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Timber.e("Location updated")
                },
                {
                    Timber.e("Error happened while updating location")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun getAllLocations() {
        val subscription = locationRepository
            .getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Timber.e("Retrieved list of locations")
                    locationsState.value = LocationsState.Success(it)
                },
                {
                    locationsState.value = LocationsState.Error("Error happened while fetching data from db")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun getLocationsWithFilter(filter: String) {
        publishSubject.onNext(filter)
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.dispose()
    }
}