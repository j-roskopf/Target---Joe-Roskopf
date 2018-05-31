package com.target.dealbrowserpoc.dealbrowser.deals.model.network

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DealItem(
    @SerializedName("_id")
    var id: String? = null,
    @SerializedName("aisle")
    var aisle: String? = null,
    @SerializedName("description")
    var description: String? = null,
    @SerializedName("guid")
    var guid: String? = null,
    @SerializedName("image")
    var image: String? = null,
    @SerializedName("index")
    var index: Int? = null,
    @SerializedName("price")
    var price: String? = null,
    @SerializedName("salePrice")
    var salePrice: String? = null,
    @SerializedName("title")
    var title: String? = null
): Parcelable