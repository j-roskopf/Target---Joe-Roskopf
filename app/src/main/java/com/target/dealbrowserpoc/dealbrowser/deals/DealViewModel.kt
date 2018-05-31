package com.target.dealbrowserpoc.dealbrowser.deals

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.target.dealbrowserpoc.dealbrowser.deals.model.network.DealResponse
import com.target.dealbrowserpoc.dealbrowser.deals.network.DealService
import kotlinx.coroutines.experimental.async


class DealViewModel(private val dealService: DealService) : ViewModel() {

    /**
     * The container for the response we've received one from the API
     */
    private var dealResponse: MutableLiveData<DealResponse>? = null

    /**
     * The function to call to get the deals. If we've previously fetched them, we will return that.
     *
     * If our container for the response is null, we will do a network call to get the deals.
     *
     * If you need to make a fresh API call, first null out the [DealResponse] contained in this ViewModel
     */
    fun getDealResponse(): LiveData<DealResponse>{
        return if(dealResponse?.value != null) {
            dealResponse!!
        } else {
            fetchDeals()
        }
    }

    /**
     * Makes network call to fetch deals!
     *
     * @return DealResponse - the response containing the deals
     */
    private fun fetchDeals(): LiveData<DealResponse> {
        dealResponse = MutableLiveData()

        async {
            dealService.getDeals().subscribe({
                dealResponse?.postValue(it)
            }, {
                dealResponse?.postValue(null)
            })
        }

        return dealResponse as MutableLiveData<DealResponse>
    }

}
