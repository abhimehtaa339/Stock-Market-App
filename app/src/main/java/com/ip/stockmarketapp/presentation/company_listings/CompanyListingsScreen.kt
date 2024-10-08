package com.ip.stockmarketapp.presentation.company_listings

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ip.stockmarketapp.presentation.destinations.CompanyInfoScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RootNavGraph(start = true)
@Destination()
@Composable
fun CompanyListingsScreen(
    navigator: DestinationsNavigator,
    viewmodel: CompanyListingsViewmodel = hiltViewModel()
) {
    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = viewmodel.state.isRefreshing
    )

    val state = viewmodel.state

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = {
                viewmodel.onEvent(
                    CompanyListingEvents.OnSearchQueryChange(it)
                )
            }, modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            placeholder = {
                Text(text = "Search..")

            },
            maxLines = 1,
            singleLine = true
        )
        SwipeRefresh(state = swipeRefreshState,
            onRefresh = {
                viewmodel.onEvent(
                    CompanyListingEvents.Refresh
                )
            }) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(state.companies.size) { i ->

                    val company = state.companies[i]
                    CompanyItem(company = company,
                        Modifier
                            .fillMaxWidth()
                            .clickable {
                                navigator.navigate(
                                    CompanyInfoScreenDestination(company.symbol)
                                )
                                Log.e("Clicked", "Working")
                            }
                            .padding(16.dp))
                    if (i < state.companies.size) {
                        Divider(modifier = Modifier.padding(horizontal = 16.dp))
                    }
                }

            }

        }
    }

}


