package com.target.dealbrowserpoc.dealbrowser.deals.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

@Deprecated("Previously used in hard coding in conjunction with [DealContent]!", replaceWith = ReplaceWith("deals.model.network.DealItem"))
class DealItem(var index: Int, var id: String, var title: String, var description: String, var originalPrice: String, var salePrice: String, var image: Int, var aisle: String) {

    fun getProductImage(context: Context): Bitmap {
        return BitmapFactory.decodeResource(context.resources, image)
    }

    override fun toString(): String {
        return title
    }
}