package com.helisur.helisurapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.helisur.helisurapp.data.database.dao.AeronaveDao
import com.helisur.helisurapp.data.database.dao.DetalleFormatoRegistroDao
import com.helisur.helisurapp.data.database.dao.EmpleadoDao
import com.helisur.helisurapp.data.database.dao.EstacionDao
import com.helisur.helisurapp.data.database.dao.FormatoDao
import com.helisur.helisurapp.data.database.dao.FormatoRegistroDao
import com.helisur.helisurapp.data.database.dao.ModeloAeronaveDao
import com.helisur.helisurapp.data.database.dao.ReportajeDao
import com.helisur.helisurapp.data.database.dao.SistemaDao
import com.helisur.helisurapp.data.database.dao.TareaDao
import com.helisur.helisurapp.data.database.dao.UsuarioDao
import com.helisur.helisurapp.data.database.entities.AeronaveEntity
import com.helisur.helisurapp.data.database.entities.DetalleFormatoRegistroEntity
import com.helisur.helisurapp.data.database.entities.EmpleadoEntity
import com.helisur.helisurapp.data.database.entities.EstacionEntity
import com.helisur.helisurapp.data.database.entities.FormatoEntity
import com.helisur.helisurapp.data.database.entities.FormatoRegistroEntity
import com.helisur.helisurapp.data.database.entities.ModeloAeronaveEntity
import com.helisur.helisurapp.data.database.entities.ReportajeEntity
import com.helisur.helisurapp.data.database.entities.TareaEntity
import com.helisur.helisurapp.data.database.entities.UsuarioEntity
import com.helisur.helisurapp.data.database.entities.response.SistemaEntity

@Database(
    entities = [UsuarioEntity::class,
    ModeloAeronaveEntity::class,
    AeronaveEntity::class,
    EstacionEntity::class,
    FormatoEntity::class,
    SistemaEntity::class,
    TareaEntity::class,
    ReportajeEntity::class,
    EmpleadoEntity::class,
    FormatoRegistroEntity::class,
    DetalleFormatoRegistroEntity::class],
    version = 1)

abstract class HelisurDatabase : RoomDatabase()  {

    abstract fun getUserDao():UsuarioDao

    abstract fun getModeloAeronaveDao():ModeloAeronaveDao

    abstract fun getAeronaveDao():AeronaveDao

    abstract fun getEstacionDao():EstacionDao

    abstract fun getFormatoDao():FormatoDao

    abstract fun getSistemaDao():SistemaDao

    abstract fun getTareaDao():TareaDao

    abstract fun getEmpleadoDao():EmpleadoDao

    abstract fun getReportajeDao():ReportajeDao

    abstract fun getFormatoRegistroDao():FormatoRegistroDao

    abstract fun getDetalleFormatoRegistroDao():DetalleFormatoRegistroDao



}