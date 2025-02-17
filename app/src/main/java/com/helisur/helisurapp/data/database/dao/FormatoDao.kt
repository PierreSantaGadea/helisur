package com.helisur.helisurapp.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.helisur.helisurapp.data.database.entities.AeronaveEntity
import com.helisur.helisurapp.data.database.entities.FormatoEntity
import com.helisur.helisurapp.data.database.entities.ModeloAeronaveEntity
import com.helisur.helisurapp.domain.model.ModeloAeronave

@Dao
interface FormatoDao {

    @Query("SELECT * FROM Formato ORDER BY id DESC")
    fun getAll(): List<FormatoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(formatos: List<FormatoEntity>)

    @Query("DELETE FROM Formato")
    fun deleteAll()
/*
    @Query("UPDATE Formato SET nombreFormato = :nombreFormato , codigoModeloAeronave = :codigoModeloAeronave, fechaRegistro = :fechaRegistro, fechaModificacion = :fechaModificacion, sync = :sync WHERE id_cloud = :idCloud")
    fun updateItem(idCloud: String,codigoModeloAeronave:String,nombreFormato:String, fechaRegistro:String,fechaModificacion:String, sync: Boolean): Int


 */
    @Query("DELETE FROM Formato WHERE  id_cloud = :idCloud")
    fun deleteItem(idCloud: String)

    @Query("DELETE FROM sqlite_sequence WHERE name = 'Formato'")
    fun deleteIndexFormato()

    @Query("UPDATE Formato SET sync = :sync WHERE id_cloud = :idCloud")
    fun updateItemSync(idCloud: String, sync: Boolean): Int

}