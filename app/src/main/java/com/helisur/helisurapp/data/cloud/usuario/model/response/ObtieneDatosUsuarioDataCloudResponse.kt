package com.helisur.helisurapp.data.cloud.usuario.model.response

import com.google.gson.annotations.SerializedName

class ObtieneDatosUsuarioDataCloudResponse (
    @SerializedName("table") var table: List<ObtieneDatosUsuarioDataTableCloudResponse>
)
{

}