package rs.raf.memorableplacesapp.modules

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import rs.raf.memorableplacesapp.data.datasource.DataBase

val coreModule = module {

    single { Room.databaseBuilder(androidContext(), DataBase::class.java, "MyDataBase")
        .fallbackToDestructiveMigration()
        .build() }

}