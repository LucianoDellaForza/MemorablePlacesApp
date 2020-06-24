package rs.raf.luka_krivacevic_rm_96_15.modules

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import rs.raf.luka_krivacevic_rm_96_15.data.datasource.DataBase

val coreModule = module {

    single { Room.databaseBuilder(androidContext(), DataBase::class.java, "MyDataBase")
        .fallbackToDestructiveMigration()
        .build() }

}