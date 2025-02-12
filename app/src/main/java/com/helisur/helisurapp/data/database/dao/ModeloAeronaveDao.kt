package com.helisur.helisurapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.helisur.helisurapp.data.database.entities.ModeloAeronaveEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ModeloAeronaveDao {

    @Query("SELECT * FROM ModeloAeronave ORDER BY id DESC")
     fun getAll():List<ModeloAeronaveEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertList(modelosAeronave:List<ModeloAeronaveEntity>)

    @Query("DELETE FROM ModeloAeronave")
     fun deleteAll()

    @Query("DELETE FROM sqlite_sequence WHERE name = 'ModeloAeronave'")
     fun deleteIndexModeloAeronave()

}