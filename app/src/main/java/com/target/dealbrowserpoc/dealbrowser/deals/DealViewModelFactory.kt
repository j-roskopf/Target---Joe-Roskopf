package com.target.dealbrowserpoc.dealbrowser.deals

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.target.dealbrowserpoc.dealbrowser.deals.network.DealService
import javax.inject.Inject

class FavoriteViewModelFactory @Inject constructor(
        private val dealService: DealService
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
            DealViewModel(dealService) as T
}