package rs.raf.memorableplacesapp.modules

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import rs.raf.memorableplacesapp.data.datasource.DataBase
import rs.raf.memorableplacesapp.data.repository.LocationRepository
import rs.raf.memorableplacesapp.data.repository.LocationRepositoryImpl
import rs.raf.memorableplacesapp.presentation.viewmodel.LocationViewModel

val locationModule = module {

    viewModel { LocationViewModel ( locationRepository =  get()) }

    single<LocationRepository> { LocationRepositoryImpl(dataSource = get())}

    single { get<DataBase>().getLocationDao() }
}