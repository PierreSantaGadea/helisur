package com.helisur.helisurapp.data.cloud.aeronaves.model.response

import com.google.gson.annotations.SerializedName

class ObtieneEstacionesDataTableCloudResponse (
    @SerializedName("id") var id: String,
    @SerializedName("nombre") var nombre: String,
    @SerializedName("siglas") var siglas: String
)
