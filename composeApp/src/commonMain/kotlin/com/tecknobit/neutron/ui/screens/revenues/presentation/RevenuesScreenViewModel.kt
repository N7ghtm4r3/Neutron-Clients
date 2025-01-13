package com.tecknobit.neutron.ui.screens.revenues.presentation

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.equinoxcompose.utilities.generateRandomColor
import com.tecknobit.equinoxcompose.utilities.toHex
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.pagination.PaginatedResponse
import com.tecknobit.neutron.ui.screens.revenues.data.GeneralRevenue
import com.tecknobit.neutron.ui.screens.revenues.data.Revenue
import com.tecknobit.neutron.ui.screens.revenues.data.RevenueLabel
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
                "Title #${Random.nextInt(100)}",
                Random.nextDouble(0.0, 1000.0),
                revenueDate = 1736771172000,
                labels = listOf(
                    RevenueLabel(
                        Random.nextLong().toString(),
                        "ETH",
                        color = generateRandomColor().toHex()
                    ),
                    RevenueLabel(
                        Random.nextLong().toString(),
                        "SOL",
                        color = generateRandomColor().toHex()
                    )
                ),
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris eleifend arcu ut arcu cursus, a accumsan tellus maximus. Nulla tempus ultricies augue, at gravida lectus lacinia a. Morbi a auctor nisl, vel commodo ipsum. Etiam sit amet nunc tincidunt lacus tempor sollicitudin nec at leo. Etiam maximus faucibus sem, rutrum sodales ligula mattis lobortis. Sed et eros bibendum, pharetra orci et, blandit tellus. Etiam sagittis dignissim diam, in sollicitudin est vehicula at. Nam consectetur odio augue, eu bibendum mauris pellentesque eget. Sed egestas tellus lectus, sit amet consectetur lectus aliquam id. Nulla porta aliquet justo in consequat.\n" +
                        "\n" +
                        "Phasellus pulvinar mauris nibh, eget venenatis massa suscipit ut. Vestibulum fringilla, nisl ut ullamcorper fermentum, ligula odio posuere massa, ac pretium dolor metus in nisl. Fusce ut erat nunc. Nam non purus ante. Integer vel aliquam nunc. Morbi lobortis dui cursus dapibus vehicula. Pellentesque viverra justo a ipsum aliquet facilisis. Sed eu sagittis sapien, ac egestas purus. Duis blandit purus ac risus convallis egestas. Vestibulum tincidunt porttitor ullamcorper. Aenean eu tempor tortor.\n" +
                        "\n" +
                        "Praesent auctor, leo quis blandit viverra, est ligula ultricies velit, at egestas nibh est non quam. Nunc ut leo ac ligula facilisis bibendum id nec lectus. Phasellus at lorem eget velit pretium auctor id congue tellus. Cras nec laoreet lacus. Duis vulputate, ligula non accumsan tempor, justo quam lacinia elit, sit amet ornare nibh massa eget lectus. Sed scelerisque neque eu auctor sodales. Duis lectus orci, bibendum eu egestas id, semper vitae enim.\n" +
                        "\n" +
                        "Nulla in bibendum mauris. Proin mattis justo felis, nec efficitur erat ornare ut. Sed vel diam nec orci finibus venenatis. Vestibulum vel neque libero. Aliquam erat volutpat. Morbi eget faucibus ligula, sit amet convallis mi. Nulla et aliquet ex. Aenean rhoncus lorem nec justo lobortis viverra. Suspendisse quis mi nec leo maximus congue vitae at libero. Fusce vel malesuada lorem. Donec semper maximus ullamcorper. Suspendisse condimentum congue velit non vestibulum. Etiam tempor ante ullamcorper ligula lobortis imperdiet.\n" +
                        "\n" +
                        "Nulla scelerisque facilisis lorem rutrum mollis. Pellentesque maximus nunc eu ipsum gravida interdum. Suspendisse sodales hendrerit velit, ac facilisis orci feugiat at. Sed iaculis vehicula nisl et iaculis. Nulla auctor mauris libero, convallis auctor felis dignissim non. Ut velit tortor, volutpat in egestas et, accumsan accumsan lorem. Sed nec lorem placerat, maximus lorem nec, fermentum orci. Donec ac laoreet lorem."
            )
        ) // TODO: USE THE REAL VALUES
        revenuesState.appendPage(
            items = revenues,
            nextPageKey = 1, // TODO: USE THE ONE FROM THE PaginationResponse
            isLastPage = Random.nextBoolean() // TODO: USE THE ONE FROM THE PaginationResponse
        )
    }

    fun deleteRevenue(
        revenue: Revenue
    ) {
        // TODO: MAKE THE REQUEST THEN
    }

}