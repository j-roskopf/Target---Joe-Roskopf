package com.target.dealbrowserpoc.dealbrowser.deals.model.network

import com.google.gson.annotations.SerializedName

data class DealResponse(
        @SerializedName("_id")
    var id: String? = null,
        @SerializedName("data")
    var data: List<DealItem>? = null,
        @SerializedName("type")
    var type: String? = null
)