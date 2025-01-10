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

    private const val DATABASE_NAME = "helisur_db"

    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, HelisurDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideUserDao(db: HelisurDatabase) = db.getUserDao()

}