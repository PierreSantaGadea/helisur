package com.helisur.helisurapp.data.repository

import android.util.Log
import com.helisur.helisurapp.data.cloud.formatos.apis.FormatosService
import com.helisur.helisurapp.data.cloud.formatos.model.parameter.GuardaFormatoCloudParameter
import com.helisur.helisurapp.data.cloud.formatos.model.parameter.ObtieneFormatosRealizadosCloudParameter
import com.helisur.helisurapp.data.cloud.formatos.model.parameter.ObtieneReportajesFormatoParameter
import com.helisur.helisurapp.data.cloud.formatos.model.response.GrabaFormatoCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneFormatosCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneFormatosRealizadosCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneReportajesFormatoCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneSistemasCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneSistemasDataTableCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneTareasCloudResponse
import com.helisur.helisurapp.data.cloud.formatos.model.response.ObtieneTareasDataTableCloudResponse
import com.helisur.helisurapp.data.database.dao.FormatoDao
import com.helisur.helisurapp.data.database.dao.SistemaDao
import com.helisur.helisurapp.data.database.dao.TareaDao
import com.helisur.helisurapp.data.database.entities.AeronaveEntity
import com.helisur.helisurapp.data.database.entities.FormatoEntity
import com.helisur.helisurapp.data.database.entities.TareaEntity
import com.helisur.helisurapp.data.database.entities.response.SistemaEntity
import com.helisur.helisurapp.data.database.entities.response.toDB
import com.helisur.helisurapp.data.database.entities.toDB
import com.helisur.helisurapp.domain.model.Aeronave
import com.helisur.helisurapp.domain.model.Formato
import com.helisur.helisurapp.domain.model.Sistema
import com.helisur.helisurapp.domain.model.Tarea
import com.helisur.helisurapp.domain.model.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import javax.inject.Inject

class FormatosRepository @Inject constructor(
    private val formatosCloudData: FormatosService,
    private val formatoslocalData: FormatoDao,
    private val sistemaslocalData: SistemaDao,
    private val tareasLocalData: TareaDao
) {

    var className = "FormatosRepository"

    suspend fun obtieneFormatos(
    ): ObtieneFormatosCloudResponse {
        val response: ObtieneFormatosCloudResponse = formatosCloudData.obtieneFormatos()
        return response
    }

    /*
    suspend fun obtieneSistemas(
        codigoFormato: String
    ): ObtieneSistemasCloudResponse {
        val response: ObtieneSistemasCloudResponse = api.obtieneSistemas(codigoFormato)
        return response
    }

     */


    suspend fun obtieneSistemas(
        codigoFormato: String
    ): ArrayList<Sistema> {
        val response: ObtieneSistemasCloudResponse = formatosCloudData.obtieneSistemas(codigoFormato)

        var lista:ArrayList<Sistema> = arrayListOf()

        for(item in response.data!!.table)
        {
            lista.add(item.toDomain())
        }
        return lista
    }

    suspend fun getobtieneSistemas(
        codigoFormato: String
    ): ArrayList<ObtieneSistemasDataTableCloudResponse> {
        val response: ObtieneSistemasCloudResponse = formatosCloudData.obtieneSistemas(codigoFormato)
        return ArrayList(response.data!!.table)
    }



/*
    suspend fun obtieneTareas(
        codigoSistema: String
    ): ObtieneTareasCloudResponse {
        val response: ObtieneTareasCloudResponse = api.obtieneTareas(codigoSistema)
        return response
    }

 */

    suspend fun obtieneTareas(
        obtieneTareas: String
    ): ArrayList<Tarea> {
        val response: ObtieneTareasCloudResponse = formatosCloudData.obtieneTareas(obtieneTareas)

        var lista:ArrayList<Tarea> = arrayListOf()

        for(item in response.data!!.table)
        {
            lista.add(item.toDomain())
        }
        return lista
    }



    suspend fun getobtieneTareas(
        obtieneTareas: String
    ): ArrayList<ObtieneTareasDataTableCloudResponse> {
        val response: ObtieneTareasCloudResponse = formatosCloudData.obtieneTareas(obtieneTareas)
        return ArrayList(response.data!!.table)
    }



    suspend fun grabaFormato(
        parameter: GuardaFormatoCloudParameter
    ): GrabaFormatoCloudResponse {
        val response: GrabaFormatoCloudResponse = formatosCloudData.grabaFormato(parameter)
        return response
    }

    suspend fun obtieneFormatosRealizados(
        codigoFormato:String,formatosHoy:String
    ): ObtieneFormatosRealizadosCloudResponse {
        var parameterBody = ObtieneFormatosRealizadosCloudParameter(codigoFormato,formatosHoy)
        val response: ObtieneFormatosRealizadosCloudResponse = formatosCloudData.obtieneFormatosRealizados(parameterBody)
        return response
    }


    suspend fun obtieneReportajesFormato(
        codigoFormato:String
    ): ObtieneReportajesFormatoCloudResponse {
        var parameterBody = ObtieneReportajesFormatoParameter(codigoFormato)
        val response: ObtieneReportajesFormatoCloudResponse = formatosCloudData.obtieneReportajesFormato(parameterBody)
        return response
    }




    // formatos

    suspend fun getFormatosListDB(): List<Formato> {
        try {
            return withContext(Dispatchers.IO) {
                val response: List<FormatoEntity> = formatoslocalData.getAll()
                response.map { it.toDomain() }
            }
        } catch (e: Exception) {
            val response: List<Formato> = arrayListOf()
            Log.e(className, e.toString())
            return response
        }
    }

    suspend fun insertFormatoListDB(formatos: List<Formato>) {
        try {
            return withContext(Dispatchers.IO) {
                var modeloAeronaveEntityList: ArrayList<FormatoEntity> = arrayListOf()
                for (modeloAeronave in formatos) {
                    modeloAeronaveEntityList.add(modeloAeronave.toDB())
                }
                formatoslocalData.insertList(modeloAeronaveEntityList)
                var error: Boolean = true
            }
        } catch (e: Exception) {
            Log.e(className, e.toString())
            return withContext(Dispatchers.IO) {
                var error: Boolean = false
            }
        }
    }


    suspend fun deleteTableFormatoDB() {
        try {
            return withContext(Dispatchers.IO) {
                formatoslocalData.deleteAll()
                formatoslocalData.deleteIndexFormato()
                var error: Boolean = true
            }
        } catch (e: Exception) {
            Log.e(className, e.toString())
            return withContext(Dispatchers.IO) {
                var error: Boolean = false
            }
        }

    }

/*
    suspend fun updateFormato(idCloud: String,codigoPuestoTecnico:String,codigoCliente:String,nombre:String,placa:String,comentario:String,html:String,fechaRegistro:String,fechaModificacion:String, sync: Boolean) {
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


 */

    suspend fun deleteFormato(idCloud: String) {
        try {
            return withContext(Dispatchers.IO) {
                formatoslocalData.deleteItem(idCloud)
                var error: Boolean = true
            }
        } catch (e: Exception) {
            Log.e(className, e.toString())
            return withContext(Dispatchers.IO) {
                var error: Boolean = false
            }
        }
    }

    suspend fun updateSyncFormato(idCloud: String, sync: Boolean) {
        try {
            return withContext(Dispatchers.IO) {
                formatoslocalData.updateItemSync(idCloud = idCloud, sync = sync)
                var error: Boolean = true
            }
        } catch (e: Exception) {
            Log.e(className, e.toString())
            return withContext(Dispatchers.IO) {
                var error: Boolean = false
            }
        }
    }


    //sistemas

    suspend fun getSistemasListDB(): List<Sistema> {
        try {
            return withContext(Dispatchers.IO) {
                val response: List<SistemaEntity> = sistemaslocalData.getAll()
                response.map { it.toDomain() }
            }
        } catch (e: Exception) {
            val response: List<Sistema> = arrayListOf()
            Log.e(className, e.toString())
            return response
        }
    }

    suspend fun insertSistemaListDB(sistemas: List<Sistema>) {
        try {
            return withContext(Dispatchers.IO) {
                var modeloAeronaveEntityList: ArrayList<SistemaEntity> = arrayListOf()
                for (modeloAeronave in sistemas) {
                    modeloAeronaveEntityList.add(modeloAeronave.toDB())
                }
                sistemaslocalData.insertList(modeloAeronaveEntityList)
                var error: Boolean = true
            }
        } catch (e: Exception) {
            Log.e(className, e.toString())
            return withContext(Dispatchers.IO) {
                var error: Boolean = false
            }
        }
    }


    suspend fun deleteTableSistemaDB() {
        try {
            return withContext(Dispatchers.IO) {
                sistemaslocalData.deleteAll()
                sistemaslocalData.deleteIndexSistema()
                var error: Boolean = true
            }
        } catch (e: Exception) {
            Log.e(className, e.toString())
            return withContext(Dispatchers.IO) {
                var error: Boolean = false
            }
        }

    }

    suspend fun deleteSistema(idCloud: String) {
        try {
            return withContext(Dispatchers.IO) {
                sistemaslocalData.deleteItem(idCloud)
                var error: Boolean = true
            }
        } catch (e: Exception) {
            Log.e(className, e.toString())
            return withContext(Dispatchers.IO) {
                var error: Boolean = false
            }
        }
    }

    suspend fun updateSyncSistema(idCloud: String, sync: Boolean) {
        try {
            return withContext(Dispatchers.IO) {
                sistemaslocalData.updateItemSync(idCloud = idCloud, sync = sync)
                var error: Boolean = true
            }
        } catch (e: Exception) {
            Log.e(className, e.toString())
            return withContext(Dispatchers.IO) {
                var error: Boolean = false
            }
        }
    }





    // tareas


    suspend fun getTareasListDB(): List<Tarea> {
        try {
            return withContext(Dispatchers.IO) {
                val response: List<TareaEntity> = tareasLocalData.getAll()
                response.map { it.toDomain() }
            }
        } catch (e: Exception) {
            val response: List<Tarea> = arrayListOf()
            Log.e(className, e.toString())
            return response
        }
    }

    suspend fun insertTareaListDB(tareas: List<Tarea>) {
        try {
            return withContext(Dispatchers.IO) {
                var modeloAeronaveEntityList: ArrayList<TareaEntity> = arrayListOf()
                for (modeloAeronave in tareas) {
                    modeloAeronaveEntityList.add(modeloAeronave.toDB())
                }
                tareasLocalData.insertList(modeloAeronaveEntityList)
                var error: Boolean = true
            }
        } catch (e: Exception) {
            Log.e(className, e.toString())
            return withContext(Dispatchers.IO) {
                var error: Boolean = false
            }
        }
    }


    suspend fun deleteTableTareaDB() {
        try {
            return withContext(Dispatchers.IO) {
                tareasLocalData.deleteAll()
                tareasLocalData.deleteIndexTarea()
                var error: Boolean = true
            }
        } catch (e: Exception) {
            Log.e(className, e.toString())
            return withContext(Dispatchers.IO) {
                var error: Boolean = false
            }
        }

    }

    suspend fun deleteTarea(idCloud: String) {
        try {
            return withContext(Dispatchers.IO) {
                tareasLocalData.deleteItem(idCloud)
                var error: Boolean = true
            }
        } catch (e: Exception) {
            Log.e(className, e.toString())
            return withContext(Dispatchers.IO) {
                var error: Boolean = false
            }
        }
    }

    suspend fun updateSyncTarea(idCloud: String, sync: Boolean) {
        try {
            return withContext(Dispatchers.IO) {
                tareasLocalData.updateItemSync(idCloud = idCloud, sync = sync)
                var error: Boolean = true
            }
        } catch (e: Exception) {
            Log.e(className, e.toString())
            return withContext(Dispatchers.IO) {
                var error: Boolean = false
            }
        }
    }



}