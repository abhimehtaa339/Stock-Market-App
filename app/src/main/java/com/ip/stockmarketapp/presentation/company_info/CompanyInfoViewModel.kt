package com.ip.stockmarketapp.presentation.company_info

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ip.stockmarketapp.domain.repository.StockRepository
import com.ip.stockmarketapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyInfoViewModel @Inject constructor(
    private val repo: StockRepository,
    private val saveStateHandle : SavedStateHandle
    ) : ViewModel() {

        var state by mutableStateOf(CompanyInfoState())

        init {
            viewModelScope.launch {
                val symbol = saveStateHandle.get<String>("symbol") ?: return@launch
                state = state.copy(isLoading = true)
                val companyInfoResult = async { repo.getCompanyInfo(symbol)}
                val intradayInfoResult = async{repo.getIntradayInfo(symbol)}
                when(val result = companyInfoResult.await()){
                    is Resource.Success -> {
                        state = state.copy(
                            company = result.data,
                            isLoading = false,
                            error = null
                        )
                    }
                    is Resource.Error -> {
                        state = state.copy(
                            company = null,
                            error = result.message,
                            isLoading = false
                        )
                    }
                    else -> Unit

                }
                when(val result = intradayInfoResult.await()){
                    is Resource.Success -> {
                        state = state.copy(
                            stockInfo = result.data ?: emptyList(),
                            isLoading = false,
                            error = null
                        )
                    }
                    is Resource.Error -> {
                        state = state.copy(
                            company = null,
                            error = result.message,
                            isLoading = false
                        )
                    }
                    else -> Unit

                }
            }
        }
}