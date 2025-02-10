package com.helisur.helisurapp.data.database.entities.response

import com.google.gson.annotations.SerializedName
import com.helisur.helisurapp.domain.model.ModeloAeronave

class ListModeloAeronaveResponse (
    @SerializedName("success") var success: Boolean,
    @SerializedName("data") var data: List<ModeloAeronave>?,
    @SerializedName("message") var message: String,
)
{
    constructor() : this(
    false,
        null,
    ""
    )
}

