package com.ip.stockmarketapp.domain.repository

import com.ip.stockmarketapp.domain.model.CompanyInfo
import com.ip.stockmarketapp.domain.model.CompanyListings
import com.ip.stockmarketapp.domain.model.IntradayInfo
import com.ip.stockmarketapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface StockRepository {

    suspend fun getCompanyListings(
        fetchFromRemote : Boolean,
        query : String
    ) : Flow<Resource<List<CompanyListings>>>

    suspend fun getIntradayInfo(
        symbol : String
    ) : Resource<List<IntradayInfo>>

    suspend fun getCompanyInfo(
        symbol: String
    ) : Resource<CompanyInfo>
}