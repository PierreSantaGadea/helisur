package com.helisur.helisurapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.helisur.helisurapp.data.database.dao.ModeloAeronaveDao
import com.helisur.helisurapp.data.database.dao.UsuarioDao
import com.helisur.helisurapp.data.database.entities.ModeloAeronaveEntity
import com.helisur.helisurapp.data.database.entities.UsuarioEntity

@Database(entities = [UsuarioEntity::class,ModeloAeronaveEntity::class], version = 1)
abstract class HelisurDatabase : RoomDatabase()  {

    abstract fun getUserDao():UsuarioDao

    abstract fun getModeloAeronaveDao():ModeloAeronaveDao

}