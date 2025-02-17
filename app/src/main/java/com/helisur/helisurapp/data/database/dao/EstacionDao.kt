package com.helisur.helisurapp.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.helisur.helisurapp.data.database.entities.AeronaveEntity
import com.helisur.helisurapp.data.database.entities.EstacionEntity
import com.helisur.helisurapp.data.database.entities.ModeloAeronaveEntity
import com.helisur.helisurapp.domain.model.ModeloAeronave

@Dao
interface EstacionDao {

    @Query("SELECT * FROM Estacion ORDER BY id DESC")
    fun getAll(): List<EstacionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(aeronaves: List<EstacionEntity>)

    @Query("DELETE FROM Estacion")
    fun deleteAll()
/*
    @Query("UPDATE Estacion SET  nombre = :nombre, siglas = :siglas, fechaRegistro = :fechaRegistro, fechaModificacion = :fechaModificacion, sync = :sync WHERE id_cloud = :idCloud")
    fun updateItem(idCloud: String,nombre:String,siglas:String,fechaRegistro:String,fechaModificacion:String, sync: Boolean): Int


 */
    @Query("DELETE FROM Estacion WHERE  id_cloud = :idCloud")
    fun deleteItem(idCloud: String)

    @Query("DELETE FROM sqlite_sequence WHERE name = 'Estacion'")
    fun deleteIndexEstacion()

    @Query("UPDATE Estacion SET sync = :sync WHERE id_cloud = :idCloud")
    fun updateItemSync(idCloud: String, sync: Boolean): Int

}