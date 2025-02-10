package com.helisur.helisurapp.data.database.entities.response

import com.google.gson.annotations.SerializedName

class DeleteModeloAeronaveListResponse (
@SerializedName("success") var success: Boolean,
@SerializedName("message") var message: String,
)
{
    constructor() : this(
    false,
    ""
    )
}

