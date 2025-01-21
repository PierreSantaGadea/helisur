package com.helisur.helisurapp.data.cloud.aeronaves.model.response

import com.google.gson.annotations.SerializedName

class ObtieneAeronavesDataTableCloudResponse (
    @SerializedName("codigoModeloPuesto") var codigoModeloPuesto: String,
    @SerializedName("codigoTipoPuesto") var codigoTipoPuesto: String,
    @SerializedName("descripcion") var descripcion: String
)
