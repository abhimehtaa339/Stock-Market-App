package com.ip.stockmarketapp.data.repository

import com.ip.stockmarketapp.data.csv.CSVParser
import com.ip.stockmarketapp.data.csv.CompanyListingParser
import com.ip.stockmarketapp.data.local.StockDatabse
import com.ip.stockmarketapp.data.mapper.toCompanyListing
import com.ip.stockmarketapp.data.mapper.toCompanyListingEntity
import com.ip.stockmarketapp.data.remote.StockApi
import com.ip.stockmarketapp.domain.model.CompanyListings
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
    private val api : StockApi,
    private val db : StockDatabse,
    private val companyListingParser: CSVParser<CompanyListings>
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
            }catch (e : IOException){
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data."))
                null
            }catch (e : HttpException){
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data."))
                null
            }

            remoteListings?.let {listings ->
                dao.clearCompanyListings()
                dao.insertCompanyListings(
                    listings.map{
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
}