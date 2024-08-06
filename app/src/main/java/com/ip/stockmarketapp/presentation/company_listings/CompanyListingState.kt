package com.ip.stockmarketapp.presentation.company_listings

import com.ip.stockmarketapp.domain.model.CompanyListings

data class CompanyListingState(
    val companies : List<CompanyListings> = emptyList(),
    val isLoading : Boolean = false,
    val isRefreshing : Boolean = false,
    val searchQuery :String = ""
)
