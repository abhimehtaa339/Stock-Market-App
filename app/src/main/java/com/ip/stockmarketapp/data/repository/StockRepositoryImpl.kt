package com.ip.stockmarketapp.data.repository

import com.ip.stockmarketapp.data.csv.CSVParser
import com.ip.stockmarketapp.data.csv.CompanyListingParser
import com.ip.stockmarketapp.data.csv.IntradayInfoParser
import com.ip.stockmarketapp.data.local.StockDatabse
import com.ip.stockmarketapp.data.mapper.toCompanyInfo
import com.ip.stockmarketapp.data.mapper.toCompanyListing
import com.ip.stockmarketapp.data.mapper.toCompanyListingEntity
import com.ip.stockmarketapp.data.remote.StockApi
import com.ip.stockmarketapp.data.remote.dto.CompanyInfoDto
import com.ip.stockmarketapp.domain.model.CompanyInfo
import com.ip.stockmarketapp.domain.model.CompanyListings
import com.ip.stockmarketapp.domain.model.IntradayInfo
import com.ip.stockmarketapp.domain.repository.StockRepository
import com.ip.stockmarketapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    private val api: StockApi,
    private val db: StockDatabse,
    private val companyListingParser: CSVParser<CompanyListings>,
    private val intradayInfoParser: IntradayInfoParser
) : StockRepository {

    private val dao = db.dao
    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListings>>> {
        return flow {
            emit(Resource.Loading(true))
            val localListings = dao.searchCompanyListing(query)
            emit(Resource.Success(
                data = localListings.map {
                    it.toCompanyListing()
                }
            ))
            val isDbEmpty = localListings.isEmpty() && query.isBlank()
            val shouldJustLoadFromCache = !isDbEmpty && !fetchFromRemote

            if (shouldJustLoadFromCache) {
                emit(Resource.Loading(false))
                return@flow
            }

            val remoteListings = try {
                val response = api.getListings()
                companyListingParser.parse(response.byteStream())
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data."))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data."))
                null
            }

            remoteListings?.let { listings ->
                dao.clearCompanyListings()
                dao.insertCompanyListings(
                    listings.map {
                        it.toCompanyListingEntity()
                    }
                )
                emit(Resource.Success(
                    data = dao.searchCompanyListing("")
                        .map {
                            it.toCompanyListing()
                        }
                ))
                emit(Resource.Loading(false))
            }
        }

    }

    override suspend fun getIntradayInfo(
        symbol: String
    ): Resource<List<IntradayInfo>> {
        return try {
            val response = api.getIntradayInfo(symbol)
            val result = intradayInfoParser.parse(response.byteStream())
            Resource.Success(result)
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load intraday info"
            )
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load intraday info"
            )
        }
    }

    override suspend fun getCompanyInfo(
        symbol: String
    ): Resource<CompanyInfo> {
        return try {
            val response = api.getCompanyInfo(symbol)
            Resource.Success(response.toCompanyInfo())
        }catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load company info"
            )
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load company info"
            )
        }
    }
}