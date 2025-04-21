package com.helisur.helisurapp.data.cloud.hojaruta.model.response

import com.google.gson.annotations.SerializedName

class ObtieneListaAcrividadesPorHojaRutaDataCloudResponse (
    @SerializedName("table") var table: List<ObtieneListaActividadesPorHojaRutaDataTableCloudResponse>
)
{

}