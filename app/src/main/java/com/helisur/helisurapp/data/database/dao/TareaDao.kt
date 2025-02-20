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
import com.helisur.helisurapp.data.database.entities.TareaEntity
import com.helisur.helisurapp.data.database.entities.response.SistemaEntity
import com.helisur.helisurapp.domain.model.ModeloAeronave

@Dao
interface TareaDao {

    @Query("SELECT * FROM Tarea ORDER BY id DESC")
    fun getAll(): List<TareaEntity>

    @Query("SELECT * FROM Tarea WHERE  codigoSistema = :idSistema ORDER BY id DESC")
    fun getTareasBySistema(idSistema: String): List<TareaEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(aeronaves: List<TareaEntity>)

    @Query("DELETE FROM Tarea")
    fun deleteAll()
/*
    @Query("UPDATE Tarea SET  codigoSistema = :codigoSistema, nombreTarea = :nombreTarea, fechaRegistro = :fechaRegistro, fechaModificacion = :fechaModificacion, sync = :sync WHERE id_cloud = :idCloud")
    fun updateItem(idCloud: String,codigoSistema:String,nombreTarea:String,fechaRegistro:String,fechaModificacion:String, sync: Boolean): Int


 */
    @Query("DELETE FROM Aeronave WHERE  id_cloud = :idCloud")
    fun deleteItem(idCloud: String)

    @Query("DELETE FROM sqlite_sequence WHERE name = 'Tarea'")
    fun deleteIndexTarea()

    @Query("UPDATE Tarea SET sync = :sync WHERE id_cloud = :idCloud")
    fun updateItemSync(idCloud: String, sync: Boolean): Int

}