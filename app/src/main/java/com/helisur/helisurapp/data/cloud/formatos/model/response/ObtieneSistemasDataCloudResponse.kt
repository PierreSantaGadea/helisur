package com.helisur.helisurapp.data.cloud.formatos.model.response

import com.google.gson.annotations.SerializedName

class ObtieneSistemasDataCloudResponse (
    @SerializedName("table") var table: List<ObtieneSistemasDataTableCloudResponse>
)
{

}