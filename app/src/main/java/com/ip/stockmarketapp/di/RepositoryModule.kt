package com.ip.stockmarketapp.di

import com.ip.stockmarketapp.data.csv.CSVParser
import com.ip.stockmarketapp.data.csv.CompanyListingParser
import com.ip.stockmarketapp.data.repository.StockRepositoryImpl
import com.ip.stockmarketapp.domain.model.CompanyListings
import com.ip.stockmarketapp.domain.repository.StockRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindStockRepository(
        stockRepositoryImpl: StockRepositoryImpl
    ): StockRepository

    @Binds
    @Singleton
    abstract fun bindCompanyListingsParser(
        companyListingsParser: CompanyListingParser
    ): CSVParser<CompanyListings>
}