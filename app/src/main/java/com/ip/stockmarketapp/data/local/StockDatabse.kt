package com.ip.stockmarketapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CompanyListingEntity::class],
    version = 1
)
abstract class StockDatabse : RoomDatabase() {
    abstract val dao : StockDao
}