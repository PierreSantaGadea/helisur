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
import com.helisur.helisurapp.data.database.entities.response.SistemaEntity
import com.helisur.helisurapp.domain.model.ModeloAeronave

@Dao
interface SistemaDao {

    @Query("SELECT * FROM Sistema ORDER BY id DESC")
    fun getAll(): List<SistemaEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(sistemas: List<SistemaEntity>)

    @Query("DELETE FROM Sistema")
    fun deleteAll()
/*
    @Query("UPDATE Aeronave SET codigoPuestoTecnico = :codigoPuestoTecnico, codigoCliente = :codigoCliente, nombre = :nombre, placa = :placa, comentario = :comentario, html = :html, sync = :sync")
    fun updateItem(idCloud: String,codigoPuestoTecnico:String,codigoCliente:String,nombre:String,placa:String,comentario:String,html:String,fechaRegistro:String,fechaModificacion:String, sync: Boolean): Int


 */
    @Query("DELETE FROM Sistema WHERE  id_cloud = :idCloud")
    fun deleteItem(idCloud: String)

    @Query("DELETE FROM sqlite_sequence WHERE name = 'Sistema'")
    fun deleteIndexSistema()

    @Query("UPDATE Sistema SET sync = :sync WHERE id_cloud = :idCloud")
    fun updateItemSync(idCloud: String, sync: Boolean): Int

}