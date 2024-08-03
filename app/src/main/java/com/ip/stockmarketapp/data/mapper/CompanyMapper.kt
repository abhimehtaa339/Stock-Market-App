package com.ip.stockmarketapp.data.mapper

import com.ip.stockmarketapp.data.local.CompanyListingEntity
import com.ip.stockmarketapp.domain.model.CompanyListings

fun CompanyListingEntity.toCompanyListing(): CompanyListings {
    return CompanyListings(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}

fun CompanyListings.toCompanyListingEntity(): CompanyListingEntity {
    return CompanyListingEntity(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}