package com.helisur.helisurapp.data.cloud.formatos.model.response

import com.google.gson.annotations.SerializedName

class ObtieneReportajesDataCloudResponse (
    @SerializedName("table") var table: List<ObtieneReportajesDataTableCloudResponse>
)
{

}