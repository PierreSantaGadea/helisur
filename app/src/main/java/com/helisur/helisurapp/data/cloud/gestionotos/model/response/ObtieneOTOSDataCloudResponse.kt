package com.helisur.helisurapp.data.cloud.gestionotos.model.response

import com.google.gson.annotations.SerializedName

class ObtieneOTOSDataCloudResponse (
    @SerializedName("table") var table: List<ObtieneOTOSDataTableCloudResponse>
)
{

}