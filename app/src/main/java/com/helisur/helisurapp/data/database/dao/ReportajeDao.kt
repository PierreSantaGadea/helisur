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
import com.helisur.helisurapp.data.database.entities.ReportajeEntity
import com.helisur.helisurapp.domain.model.ModeloAeronave

@Dao
interface ReportajeDao {

    @Query("SELECT * FROM Reportaje ORDER BY id DESC")
    fun getAll(): List<ReportajeEntity>

    @Query("SELECT * FROM Reportaje WHERE  codigoTarea = :idTarea ORDER BY id DESC")
    fun getReportajesByTarea(idTarea: String): List<ReportajeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(reportajes: List<ReportajeEntity>)

    @Query("DELETE FROM Reportaje")
    fun deleteAll()


    @Query("UPDATE Reportaje SET  codigoTarea = :codigoTarea, nombreReportaje = :nombreReportaje, bloqueoFormato = :bloqueoFormato, defaultt = :defaultt, fechaRegistro = :fechaRegistro, fechaModificacion = :fechaModificacion, sync = :sync WHERE id_cloud = :idCloud")
    fun updateItem(idCloud: String,codigoTarea:String,nombreReportaje:String,bloqueoFormato:String,defaultt:String,fechaRegistro:String,fechaModificacion:String, sync: Boolean): Int


    @Query("DELETE FROM Reportaje WHERE  id_cloud = :idCloud")
    fun deleteItem(idCloud: String)

    @Query("DELETE FROM sqlite_sequence WHERE name = 'Reportaje'")
    fun deleteIndexReportaje()

    @Query("UPDATE Reportaje SET sync = :sync WHERE id_cloud = :idCloud")
    fun updateItemSync(idCloud: String, sync: Boolean): Int



}