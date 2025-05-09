package com.helisur.helisurapp.data.cloud.hojaruta.model.response

import com.google.gson.annotations.SerializedName

class ObtieneListaResponsableHojasRutaDataCloudResponse (
    @SerializedName("table") var table: List<ObtieneListaResponsableHojasRutaDataTableCloudResponse>
)
{

}