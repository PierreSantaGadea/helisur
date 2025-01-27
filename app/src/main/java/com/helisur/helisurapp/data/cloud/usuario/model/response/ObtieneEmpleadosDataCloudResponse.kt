package com.helisur.helisurapp.data.cloud.usuario.model.response

import com.google.gson.annotations.SerializedName

class ObtieneEmpleadosDataCloudResponse (
    @SerializedName("table") var table: List<ObtieneEmpleadosDataTableCloudResponse>
)
{

}