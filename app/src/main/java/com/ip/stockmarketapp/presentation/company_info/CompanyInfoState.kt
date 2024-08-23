package com.ip.stockmarketapp.presentation.company_info

import com.ip.stockmarketapp.domain.model.CompanyInfo
import com.ip.stockmarketapp.domain.model.IntradayInfo

data class CompanyInfoState(
    val stockInfo : List<IntradayInfo> = emptyList(),
    val company : CompanyInfo? = null,
    val isLoading : Boolean = false,
    val error : String? = null
)
