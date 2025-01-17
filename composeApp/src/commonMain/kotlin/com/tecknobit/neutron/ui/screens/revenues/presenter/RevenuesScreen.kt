@file:OptIn(ExperimentalMultiplatform::class)

package com.tecknobit.neutron.ui.screens.revenues.presenter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StickyNote2
import androidx.compose.material3.Card
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.components.EmptyListUI
import com.tecknobit.equinoxcompose.resources.retry
import com.tecknobit.equinoxcompose.session.EquinoxScreen
import com.tecknobit.equinoxcompose.session.ManagedContent
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import com.tecknobit.neutron.CloseApplicationOnNavBack
import com.tecknobit.neutron.INSERT_REVENUE_SCREEN
import com.tecknobit.neutron.MAX_CONTAINER_WIDTH
import com.tecknobit.neutron.PROFILE_SCREEN
import com.tecknobit.neutron.bodyFontFamily
import com.tecknobit.neutron.displayFontFamily
import com.tecknobit.neutron.localUser
import com.tecknobit.neutron.navigator
import com.tecknobit.neutron.ui.components.FirstPageProgressIndicator
import com.tecknobit.neutron.ui.components.NewPageProgressIndicator
import com.tecknobit.neutron.ui.components.ProfilePic
import com.tecknobit.neutron.ui.icons.ReceiptLong
import com.tecknobit.neutron.ui.screens.revenues.components.FiltersBar
import com.tecknobit.neutron.ui.screens.revenues.components.RevenueCard
import com.tecknobit.neutron.ui.screens.revenues.presentation.RevenuesScreenViewModel
import com.tecknobit.neutron.ui.theme.NeutronTheme
import com.tecknobit.neutroncore.dtos.WalletStatus
import com.tecknobit.neutroncore.enums.RevenuePeriod
import com.tecknobit.neutroncore.enums.RevenuePeriod.ALL
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyColumn
import neutron.composeapp.generated.resources.Res
import neutron.composeapp.generated.resources.add_revenue
import neutron.composeapp.generated.resources.earnings
import neutron.composeapp.generated.resources.last_month_period
import neutron.composeapp.generated.resources.last_six_months
import neutron.composeapp.generated.resources.last_three_months
import neutron.composeapp.generated.resources.last_week_period
import neutron.composeapp.generated.resources.last_year
import neutron.composeapp.generated.resources.no_revenues_yet
import org.jetbrains.compose.resources.stringResource

class RevenuesScreen : EquinoxScreen<RevenuesScreenViewModel>(
    viewModel = RevenuesScreenViewModel()
) {

    private companion object {

        const val POSITIVE_TREND = "+"

    }

    private lateinit var walletStatus: State<WalletStatus?>

    private lateinit var revenuePeriod: State<RevenuePeriod>

    /**
     * Method to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        CloseApplicationOnNavBack()
        NeutronTheme {
            ManagedContent(
                viewModel = viewModel!!,
                loadingRoutine = { walletStatus.value != null },
                content = {
                    Scaffold(
                        snackbarHost = { SnackbarHost(viewModel!!.snackbarHostState!!) },
                        floatingActionButton = {
                            FabButton()
                        }
                    ) {
                        ScreenContent()
                    }
                },
                noInternetConnectionRetryText = com.tecknobit.equinoxcompose.resources.Res.string.retry,
                noInternetConnectionRetryAction = { viewModel!!.revenuesState.retryLastFailedRequest() }
            )
        }
    }

    @Composable
    @NonRestartableComposable
    private fun FabButton() {
        ResponsiveContent(
            onExpandedSizeClass = {
                ExtendedFloatingActionButton(
                    onClick = { navToCreate() }
                ) {
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.StickyNote2,
                            contentDescription = null
                        )
                        Text(
                            text = stringResource(Res.string.add_revenue)
                        )
                    }
                }
            },
            onMediumSizeClass = { CompactFabButton() },
            onCompactSizeClass = { CompactFabButton() }
        )
    }

    @Composable
    @NonRestartableComposable
    private fun CompactFabButton() {
        FloatingActionButton(
            onClick = { navToCreate() }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.StickyNote2,
                contentDescription = null
            )
        }
    }

    private fun navToCreate() {
        navigator.navigate(INSERT_REVENUE_SCREEN)
    }

    @Composable
    @NonRestartableComposable
    private fun ScreenContent() {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Header()
            Revenues()
        }
    }

    @Composable
    @NonRestartableComposable
    private fun Header() {
        Card (
            modifier = Modifier
                .widthIn(
                    max = MAX_CONTAINER_WIDTH
                )
                .height(175.dp),
            shape = RoundedCornerShape(
                bottomStart = 15.dp,
                bottomEnd = 15.dp
            )
        ) {
            Row (
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(
                        all = 10.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                WalletStatus()
                UserProfilePicture()
            }
            FiltersBar(
                viewModel = viewModel!!
            )
        }
    }

    @Composable
    @NonRestartableComposable
    private fun RowScope.WalletStatus() {
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = stringResource(Res.string.earnings)
            )
            Text(
                text = "${walletStatus.value!!.totalEarnings}" + localUser.currency.symbol,
                fontFamily = displayFontFamily,
                fontSize = 30.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            WalletTrend()
        }
    }

    @Composable
    @NonRestartableComposable
    private fun WalletTrend() {
        AnimatedVisibility(
            visible = revenuePeriod.value != ALL
        ) {
            val trend = walletStatus.value!!.trend
            val isPositiveTrend = trend > 0
            val symbol = if(isPositiveTrend)
                POSITIVE_TREND
            else
                ""
            val periodText = when(revenuePeriod.value) {
                RevenuePeriod.LAST_WEEK -> Res.string.last_week_period
                RevenuePeriod.LAST_MONTH -> Res.string.last_month_period
                RevenuePeriod.LAST_THREE_MONTHS ->  Res.string.last_three_months
                RevenuePeriod.LAST_SIX_MONTHS -> Res.string.last_six_months
                RevenuePeriod.LAST_YEAR -> Res.string.last_year
                else -> return@AnimatedVisibility
            }
            Row (
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                val color = if(isPositiveTrend)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.error
                Text(
                    text = "$symbol${trend}%",
                    fontFamily = displayFontFamily,
                    fontSize = 18.sp,
                    color = color,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = stringResource(periodText),
                    fontFamily = displayFontFamily,
                    fontSize = 14.sp,
                    color = color,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun RowScope.UserProfilePicture() {
        Column(
            modifier = Modifier
                .weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            var size by remember { mutableStateOf(75.dp) }
            ResponsiveContent(
                onExpandedSizeClass = { size = 95.dp },
                onMediumSizeClass = { size = 85.dp },
                onCompactSizeClass = { size = 75.dp}
            )
            ProfilePic(
                profilePic = localUser.profilePic,
                size = size,
                onClick = { navigator.navigate(PROFILE_SCREEN) }
            )
        }
    }

    @Composable
    @NonRestartableComposable
    private fun Revenues() {
        PaginatedLazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .widthIn(
                    max = MAX_CONTAINER_WIDTH
                )
                .navigationBarsPadding(),
            paginationState = viewModel!!.revenuesState,
            contentPadding = PaddingValues(
                bottom = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            firstPageProgressIndicator = { FirstPageProgressIndicator() },
            firstPageEmptyIndicator = {
                EmptyListUI(
                    icon = ReceiptLong,
                    subText = Res.string.no_revenues_yet,
                    textStyle = TextStyle(
                        fontFamily = bodyFontFamily
                    )
                )
            },
            newPageProgressIndicator = { NewPageProgressIndicator() }
        ) {
            itemsIndexed(
                items = viewModel!!.revenuesState.allItems!!,
                key = { _, revenue -> revenue.id }
            ) { index, revenue ->
                RevenueCard(
                    viewModel = viewModel!!,
                    revenue = revenue,
                    position = index
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel!!.getWalletStatus()
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
        walletStatus = viewModel!!.walletStatus.collectAsState()
        revenuePeriod = viewModel!!.revenuePeriod.collectAsState()
    }

}