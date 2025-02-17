package com.helisur.helisurapp.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.helisur.helisurapp.data.database.entities.AeronaveEntity
import com.helisur.helisurapp.data.database.entities.ModeloAeronaveEntity
import com.helisur.helisurapp.domain.model.ModeloAeronave

@Dao
interface AeronaveDao {

    @Query("SELECT * FROM Aeronave ORDER BY id DESC")
    fun getAll(): List<AeronaveEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(aeronaves: List<AeronaveEntity>)

    @Query("DELETE FROM Aeronave")
    fun deleteAll()
/*
    @Query("UPDATE Aeronave SET codigoPuestoTecnico = :codigoPuestoTecnico, codigoCliente = :codigoCliente, nombre = :nombre, placa = :placa, comentario = :comentario, html = :html, sync = :sync")
    fun updateItem(idCloud: String,codigoPuestoTecnico:String,codigoCliente:String,nombre:String,placa:String,comentario:String,html:String,fechaRegistro:String,fechaModificacion:String, sync: Boolean): Int


 */
    @Query("DELETE FROM Aeronave WHERE  id_cloud = :idCloud")
    fun deleteItem(idCloud: String)

    @Query("DELETE FROM sqlite_sequence WHERE name = 'Aeronave'")
    fun deleteIndexAeronave()

    @Query("UPDATE Aeronave SET sync = :sync WHERE id_cloud = :idCloud")
    fun updateItemSync(idCloud: String, sync: Boolean): Int

}