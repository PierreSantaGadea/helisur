package com.helisur.helisurapp.data.di

import android.content.Context
import androidx.room.Room
import com.helisur.helisurapp.data.database.HelisurDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    private const val DATABASE_NAME = "HELISUR_DATABASE"

    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, HelisurDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideUserDao(db: HelisurDatabase) = db.getUserDao()


    @Singleton
    @Provides
    fun provideModeloAeronaveDao(db: HelisurDatabase) = db.getModeloAeronaveDao()

    @Singleton
    @Provides
    fun provideAeronaveDao(db: HelisurDatabase) = db.getAeronaveDao()

    @Singleton
    @Provides
    fun provideEstacionDao(db: HelisurDatabase) = db.getEstacionDao()


    @Singleton
    @Provides
    fun provideFormatoDao(db: HelisurDatabase) = db.getFormatoDao()


    @Singleton
    @Provides
    fun provideSistemaDao(db: HelisurDatabase) = db.getSistemaDao()


    @Singleton
    @Provides
    fun provideTareaDao(db: HelisurDatabase) = db.getTareaDao()


    @Singleton
    @Provides
    fun provideEmpleadoDao(db: HelisurDatabase) = db.getEmpleadoDao()


    @Singleton
    @Provides
    fun provideReportajeDao(db: HelisurDatabase) = db.getReportajeDao()


    @Singleton
    @Provides
    fun provideFormatoRegistroDao(db: HelisurDatabase) = db.getFormatoRegistroDao()


    @Singleton
    @Provides
    fun provideDetalleFormatoRegistroDao(db: HelisurDatabase) = db.getDetalleFormatoRegistroDao()

}