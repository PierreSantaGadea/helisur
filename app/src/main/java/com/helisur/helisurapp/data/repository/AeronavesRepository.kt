package com.helisur.helisurapp.data.repository

import android.util.Log
import com.helisur.helisurapp.data.cloud.aeronaves.apis.AeronavesService
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneAeronavesCloudResponse
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneEstacionesCloudResponse
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneModelosAeronaveCloudResponse
import com.helisur.helisurapp.data.database.dao.AeronaveDao
import com.helisur.helisurapp.data.database.dao.EstacionDao
import com.helisur.helisurapp.data.database.dao.ModeloAeronaveDao
import com.helisur.helisurapp.data.database.entities.AeronaveEntity
import com.helisur.helisurapp.data.database.entities.EstacionEntity
import com.helisur.helisurapp.data.database.entities.ModeloAeronaveEntity
import com.helisur.helisurapp.data.database.entities.toDB
import com.helisur.helisurapp.domain.model.Aeronave
import com.helisur.helisurapp.domain.model.Estacion
import com.helisur.helisurapp.domain.model.ModeloAeronave
import com.helisur.helisurapp.domain.model.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import javax.inject.Inject

class AeronavesRepository @Inject constructor(
    private val aeronavescloudData: AeronavesService,
    private val modeloAeronavelocalData: ModeloAeronaveDao,
    private val aeronaveslocalData: AeronaveDao,
    private val estacionLocalData: EstacionDao,
) {

    var className = "AeronavesRepository"

    suspend fun getModeloAeronaveListCloud(
    ): ObtieneAeronavesCloudResponse {
        val response: ObtieneAeronavesCloudResponse = aeronavescloudData.getModeloAeronaveList()
        return response
    }

    suspend fun getAeromaveListCloud(
        aeronave: String
    ): ObtieneModelosAeronaveCloudResponse {
        val response: ObtieneModelosAeronaveCloudResponse =
            aeronavescloudData.getAeromaveList(aeronave)
        return response
    }

    suspend fun getEstacionesListCloud(
    ): ObtieneEstacionesCloudResponse {
        val response: ObtieneEstacionesCloudResponse = aeronavescloudData.getEstacionesList()
        return response
    }


    // modelo aeronave

    suspend fun getModelosAeronavesListDB(): List<ModeloAeronave> {
        try {
            return withContext(Dispatchers.IO) {
                val response: List<ModeloAeronaveEntity> = modeloAeronavelocalData.getAll()
                response.map { it.toDomain() }
            }
        } catch (e: Exception) {
            val response: List<ModeloAeronave> = arrayListOf()
            Log.e(className, e.toString())
            return response
        }
    }

    suspend fun insertModeloAeronaveListDB(modelosAeronave: List<ModeloAeronave>) {
        try {
            return withContext(Dispatchers.IO) {
                var modeloAeronaveEntityList: ArrayList<ModeloAeronaveEntity> = arrayListOf()
                for (modeloAeronave in modelosAeronave) {
                    modeloAeronaveEntityList.add(modeloAeronave.toDB())
                }
                modeloAeronavelocalData.insertList(modeloAeronaveEntityList)
                var error: Boolean = true
            }
        } catch (e: Exception) {
            Log.e(className, e.toString())
            return withContext(Dispatchers.IO) {
                var error: Boolean = false
            }
        }
    }


    suspend fun deleteTableModeloAeronaveDB() {
        try {
            return withContext(Dispatchers.IO) {
                modeloAeronavelocalData.deleteAll()
                modeloAeronavelocalData.deleteIndexModeloAeronave()
                var error: Boolean = true
            }
        } catch (e: Exception) {
            Log.e(className, e.toString())
            return withContext(Dispatchers.IO) {
                var error: Boolean = false
            }
        }

    }


    suspend fun updateModeloAeronave(idCloud: String,nombre:String,fechaRegistro:String,fechaModificacion:String, sync: Boolean) {
        try {
            return withContext(Dispatchers.IO) {
             //   modeloAeronavelocalData.updateItem(idCloud,nombre,fechaRegistro,fechaModificacion, sync)
                var error: Boolean = true
            }
        } catch (e: Exception) {
            Log.e(className, e.toString())
            return withContext(Dispatchers.IO) {
                var error: Boolean = false
            }
        }
    }


    suspend fun deleteModeloAeronave(idCloud: String) {
        try {
            return withContext(Dispatchers.IO) {
                modeloAeronavelocalData.deleteItem(idCloud)
                var error: Boolean = true
            }
        } catch (e: Exception) {
            Log.e(className, e.toString())
            return withContext(Dispatchers.IO) {
                var error: Boolean = false
            }
        }
    }

    suspend fun updateSyncModeloAeronave(idCloud: String, sync: Boolean) {
        try {
            return withContext(Dispatchers.IO) {
                modeloAeronavelocalData.updateItemSync(idCloud = idCloud, sync = sync)
                var error: Boolean = true
            }
        } catch (e: Exception) {
            Log.e(className, e.toString())
            return withContext(Dispatchers.IO) {
                var error: Boolean = false
            }
        }
    }


    // aeronave

    suspend fun getAeronavesListDB(): List<Aeronave> {
        try {
            return withContext(Dispatchers.IO) {
                val response: List<AeronaveEntity> = aeronaveslocalData.getAll()
                response.map { it.toDomain() }
            }
        } catch (e: Exception) {
            val response: List<Aeronave> = arrayListOf()
            Log.e(className, e.toString())
            return response
        }
    }



    suspend fun getAeronavesByModeloDB(idModelo:String): List<Aeronave> {
        try {
            return withContext(Dispatchers.IO) {
                val response: List<AeronaveEntity> = aeronaveslocalData.getAeronavesByModelo(idModelo)
                response.map { it.toDomain() }
            }
        } catch (e: Exception) {
            val response: List<Aeronave> = arrayListOf()
            Log.e(className, e.toString())
            return response
        }
    }

    suspend fun insertAeronaveListDB(aeronaves: List<Aeronave>) {
        try {
            return withContext(Dispatchers.IO) {
                var modeloAeronaveEntityList: ArrayList<AeronaveEntity> = arrayListOf()
                for (modeloAeronave in aeronaves) {
                    modeloAeronaveEntityList.add(modeloAeronave.toDB())
                }
                aeronaveslocalData.insertList(modeloAeronaveEntityList)
                var error: Boolean = true
            }
        } catch (e: Exception) {
            Log.e(className, e.toString())
            return withContext(Dispatchers.IO) {
                var error: Boolean = false
            }
        }
    }


    suspend fun deleteTableAeronaveDB() {
        try {
            return withContext(Dispatchers.IO) {
                aeronaveslocalData.deleteAll()
                aeronaveslocalData.deleteIndexAeronave()
                var error: Boolean = true
            }
        } catch (e: Exception) {
            Log.e(className, e.toString())
            return withContext(Dispatchers.IO) {
                var error: Boolean = false
            }
        }

    }


    suspend fun updateAeronave(idCloud: String,codigoPuestoTecnico:String,codigoCliente:String,nombre:String,placa:String,comentario:String,html:String,fechaRegistro:String,fechaModificacion:String, sync: Boolean) {
        try {
            return withContext(Dispatchers.IO) {
           //     aeronaveslocalData.updateItem(idCloud,codigoPuestoTecnico,codigoCliente,nombre,placa,comentario,html,fechaRegistro,fechaModificacion, sync)
                var error: Boolean = true
            }
        } catch (e: Exception) {
            Log.e(className, e.toString())
            return withContext(Dispatchers.IO) {
                var error: Boolean = false
            }
        }
    }


    suspend fun deleteAeronave(idCloud: String) {
        try {
            return withContext(Dispatchers.IO) {
                aeronaveslocalData.deleteItem(idCloud)
                var error: Boolean = true
            }
        } catch (e: Exception) {
            Log.e(className, e.toString())
            return withContext(Dispatchers.IO) {
                var error: Boolean = false
            }
        }
    }

    suspend fun updateSyncAeronave(idCloud: String, sync: Boolean) {
        try {
            return withContext(Dispatchers.IO) {
                aeronaveslocalData.updateItemSync(idCloud = idCloud, sync = sync)
                var error: Boolean = true
            }
        } catch (e: Exception) {
            Log.e(className, e.toString())
            return withContext(Dispatchers.IO) {
                var error: Boolean = false
            }
        }
    }


    // estacion


    suspend fun getEstacionesListDB(): List<Estacion> {
        try {
            return withContext(Dispatchers.IO) {
                val response: List<EstacionEntity> = estacionLocalData.getAll()
                response.map { it.toDomain() }
            }
        } catch (e: Exception) {
            val response: List<Estacion> = arrayListOf()
            Log.e(className, e.toString())
            return response
        }
    }

    suspend fun insertEstacionListDB(estaciones: List<Estacion>) {
        try {
            return withContext(Dispatchers.IO) {
                var modeloAeronaveEntityList: ArrayList<EstacionEntity> = arrayListOf()
                for (modeloAeronave in estaciones) {
                    modeloAeronaveEntityList.add(modeloAeronave.toDB())
                }
                estacionLocalData.insertList(modeloAeronaveEntityList)
                var error: Boolean = true
            }
        } catch (e: Exception) {
            Log.e(className, e.toString())
            return withContext(Dispatchers.IO) {
                var error: Boolean = false
            }
        }
    }


    suspend fun deleteTableEstacionDB() {
        try {
            return withContext(Dispatchers.IO) {
                estacionLocalData.deleteAll()
                estacionLocalData.deleteIndexEstacion()
                var error: Boolean = true
            }
        } catch (e: Exception) {
            Log.e(className, e.toString())
            return withContext(Dispatchers.IO) {
                var error: Boolean = false
            }
        }

    }


    suspend fun updateEstacion(idCloud: String,codigoPuestoTecnico:String,codigoCliente:String,nombre:String,placa:String,comentario:String,html:String,fechaRegistro:String,fechaModificacion:String, sync: Boolean) {
        try {
            return withContext(Dispatchers.IO) {
              //  aeronaveslocalData.updateItem(idCloud,codigoPuestoTecnico,codigoCliente,nombre,placa,comentario,html,fechaRegistro,fechaModificacion, sync)
                var error: Boolean = true
            }
        } catch (e: Exception) {
            Log.e(className, e.toString())
            return withContext(Dispatchers.IO) {
                var error: Boolean = false
            }
        }
    }


    suspend fun deleteEstacion(idCloud: String) {
        try {
            return withContext(Dispatchers.IO) {
                estacionLocalData.deleteItem(idCloud)
                var error: Boolean = true
            }
        } catch (e: Exception) {
            Log.e(className, e.toString())
            return withContext(Dispatchers.IO) {
                var error: Boolean = false
            }
        }
    }

    suspend fun updateSyncEstacion(idCloud: String, sync: Boolean) {
        try {
            return withContext(Dispatchers.IO) {
                estacionLocalData.updateItemSync(idCloud = idCloud, sync = sync)
                var error: Boolean = true
            }
        } catch (e: Exception) {
            Log.e(className, e.toString())
            return withContext(Dispatchers.IO) {
                var error: Boolean = false
            }
        }
    }



    // formato registro cant

    suspend fun getCountDetallessByAeronave(idAeronave: String):Int {
        try {
            return withContext(Dispatchers.IO) {
                aeronaveslocalData.getCountDetallessByAeronave(idAeronave)
            }
        } catch (e: Exception) {
            Log.e(className, e.toString())
            return withContext(Dispatchers.IO) {
                 0
            }
        }
    }

}