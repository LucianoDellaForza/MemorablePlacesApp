package rs.raf.luka_krivacevic_rm_96_15.modules

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import rs.raf.luka_krivacevic_rm_96_15.data.datasource.DataBase
import rs.raf.luka_krivacevic_rm_96_15.data.repository.LocationRepository
import rs.raf.luka_krivacevic_rm_96_15.data.repository.LocationRepositoryImpl
import rs.raf.luka_krivacevic_rm_96_15.presentation.viewmodel.LocationViewModel

val locationModule = module {

    viewModel { LocationViewModel ( locationRepository =  get()) }

    single<LocationRepository> { LocationRepositoryImpl(dataSource = get())}

    single { get<DataBase>().getLocationDao() }
}