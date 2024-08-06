package com.ip.stockmarketapp.presentation.company_listings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ip.stockmarketapp.domain.repository.StockRepository
import com.ip.stockmarketapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyListingsViewmodel @Inject constructor(
    private val repo: StockRepository
) : ViewModel() {
    var state by mutableStateOf(CompanyListingState())
    private var searchJob : Job? = null


    init {
        getCompanyListings()
    }

    fun onEvent(events: CompanyListingEvents){
        when(events){
            is CompanyListingEvents.Refresh -> {
                getCompanyListings(fetchFromRemote = true)
            }
            is CompanyListingEvents.OnSearchQueryChange ->{
                state = state.copy(searchQuery = events.query)
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500L)
                    getCompanyListings()
                }
            }
        }
    }

    private fun getCompanyListings(
        query : String = state.searchQuery.lowercase(),
        fetchFromRemote : Boolean = false
    ){
        viewModelScope.launch {
            repo
                .getCompanyListings(query = query , fetchFromRemote = fetchFromRemote)
                .collect{result ->
                    when(result){
                        is Resource.Success -> {
                            result.data?.let {listings ->
                                state = state.copy(
                                    companies = listings
                                )
                            }
                        }
                        is Resource.Loading -> {
                            state = state.copy(isLoading = result.isLoading)
                        }
                        is Resource.Error -> {
                            Unit
                        }
                    }


                }

        }
    }
}