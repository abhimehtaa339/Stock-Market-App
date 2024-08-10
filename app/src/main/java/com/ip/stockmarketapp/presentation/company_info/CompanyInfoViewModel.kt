package com.ip.stockmarketapp.presentation.company_info

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.ip.stockmarketapp.domain.repository.StockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CompanyInfoViewModel @Inject constructor(
    private val repo: StockRepository,
    private val saveStateHandle : SavedStateHandle
    ) : ViewModel() {
}