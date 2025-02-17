package com.helisur.helisurapp.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.helisur.helisurapp.data.database.entities.ModeloAeronaveEntity
import com.helisur.helisurapp.domain.model.ModeloAeronave

@Dao
interface ModeloAeronaveDao {

    @Query("SELECT * FROM ModeloAeronave ORDER BY id DESC")
    fun getAll(): List<ModeloAeronaveEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(modelosAeronave: List<ModeloAeronaveEntity>)

    @Query("DELETE FROM ModeloAeronave")
    fun deleteAll()

    @Query("UPDATE ModeloAeronave SET sync = :sync, nombre = :nombre, fechaRegistro = :fechaRegistro, fechaModificacion = :fechaModificacion WHERE id_cloud = :idCloud")
    fun updateItem(idCloud: String,nombre:String,fechaRegistro:String,fechaModificacion:String, sync: Boolean): Int

    @Query("DELETE FROM ModeloAeronave WHERE  id_cloud = :idCloud")
    fun deleteItem(idCloud: String)

    @Query("DELETE FROM sqlite_sequence WHERE name = 'ModeloAeronave'")
    fun deleteIndexModeloAeronave()

    @Query("UPDATE ModeloAeronave SET sync = :sync WHERE id_cloud = :idCloud")
    fun updateItemSync(idCloud: String, sync: Boolean): Int

}