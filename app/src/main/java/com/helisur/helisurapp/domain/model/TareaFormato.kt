package com.helisur.helisurapp.domain.model

import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneAeronavesDataTableCloudResponse
import com.helisur.helisurapp.data.cloud.aeronaves.model.response.ObtieneEstacionesDataTableCloudResponse
import com.helisur.helisurapp.data.database.entities.EstacionEntity
import com.helisur.helisurapp.data.database.entities.ModeloAeronaveEntity

data class TareaFormato(
    var idTareaFormato: String,
    var noAplica: Boolean,
    var rtv: Boolean,
    var danosMenores: Boolean,
    var melMds: Boolean,
    var motivo: String
)






