package com.ip.stockmarketapp.data.mapper

import com.ip.stockmarketapp.data.local.CompanyListingEntity
import com.ip.stockmarketapp.data.remote.dto.CompanyInfoDto
import com.ip.stockmarketapp.domain.model.CompanyInfo
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

fun CompanyInfoDto.toCompanyInfo() : CompanyInfo{
    return CompanyInfo(
        symbol = symbol ?: "",
        description = description ?: "",
        name = name ?: "",
        country = country ?: "",
        industry = industry ?: ""
    )

}