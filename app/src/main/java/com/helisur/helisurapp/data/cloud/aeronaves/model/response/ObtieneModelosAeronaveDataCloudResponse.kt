package com.helisur.helisurapp.data.cloud.aeronaves.model.response

import com.google.gson.annotations.SerializedName

class ObtieneModelosAeronaveDataCloudResponse (
    @SerializedName("table") var table: List<ObtieneModelosAeronaveDataTableCloudResponse>
)
{

}