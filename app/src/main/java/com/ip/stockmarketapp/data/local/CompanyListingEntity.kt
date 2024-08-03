package com.ip.stockmarketapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import okhttp3.internal.connection.Exchange

@Entity
data class CompanyListingEntity(
    val name: String,
    val symbol: String,
    val exchange: String,
    @PrimaryKey(autoGenerate = true)
    val id : Int? = null
)