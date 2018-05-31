package com.target.dealbrowserpoc.dealbrowser.deals.network

import com.target.dealbrowserpoc.dealbrowser.deals.model.network.DealResponse
import io.reactivex.Maybe
import retrofit2.http.GET

interface DealService {
    /**
     * Interface for interacting with our base deals API
     */

    /**
     * Get all of the deals!
     *
     * @return - a [DealResponse] wrapped in a [Maybe]
     */
    @GET("deals")
    fun getDeals(): Maybe<DealResponse>
}