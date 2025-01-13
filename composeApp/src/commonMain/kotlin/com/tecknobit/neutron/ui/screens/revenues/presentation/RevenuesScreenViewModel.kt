package com.tecknobit.neutron.ui.screens.revenues.presentation

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.pagination.PaginatedResponse
import com.tecknobit.neutron.ui.screens.revenues.data.GeneralRevenue
import com.tecknobit.neutron.ui.screens.revenues.data.Revenue
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlin.random.Random

class RevenuesScreenViewModel : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    val revenuesState = PaginationState<Int, Revenue>(
        initialPageKey = PaginatedResponse.DEFAULT_PAGE,
        onRequestPage = { page ->
            loadRevenues(
                page = page
            )
        }
    )

    private fun loadRevenues(
        page: Int,
    ) {
        // TODO: MAKE THE REQUEST THEN
        val revenues = listOf(
            GeneralRevenue.GeneralImpl(
                Random.nextLong().toString(),
                Random.nextLong().toString(),
                Random.nextDouble(),
                revenueDate = Random.nextLong()
            )
        ) // TODO: USE THE REAL VALUES
        revenuesState.appendPage(
            items = revenues,
            nextPageKey = 1, // TODO: USE THE ONE FROM THE PaginationResponse
            isLastPage = Random.nextBoolean() // TODO: USE THE ONE FROM THE PaginationResponse
        )
    }

}