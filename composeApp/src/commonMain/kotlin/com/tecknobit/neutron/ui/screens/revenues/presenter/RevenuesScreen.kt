@file:OptIn(ExperimentalMultiplatform::class, ExperimentalComposeApi::class)

package com.tecknobit.neutron.ui.screens.revenues.presenter

import androidx.compose.animation.AnimatedContent
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
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
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
import com.tecknobit.equinoxcompose.annotations.ScreenSection
import com.tecknobit.equinoxcompose.components.EmptyListUI
import com.tecknobit.equinoxcompose.resources.retry
import com.tecknobit.equinoxcompose.session.ManagedContent
import com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
import com.tecknobit.equinoxcompose.utilities.EXPANDED_CONTAINER
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import com.tecknobit.neutron.CloseApplicationOnNavBack
import com.tecknobit.neutron.INSERT_REVENUE_SCREEN
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
import com.tecknobit.neutron.ui.screens.shared.presenters.RevenuesContainerScreen
import com.tecknobit.neutron.ui.screens.shared.presenters.RevenuesContainerScreen.Companion.HIDE_BALANCE
import com.tecknobit.neutron.ui.theme.NeutronTheme
import com.tecknobit.neutroncore.dtos.WalletStatus
import com.tecknobit.neutroncore.enums.RevenuePeriod
import com.tecknobit.neutroncore.enums.RevenuePeriod.ALL
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyColumn
import neutron.composeapp.generated.resources.Res
import neutron.composeapp.generated.resources.add_revenue
import neutron.composeapp.generated.resources.last_month_period
import neutron.composeapp.generated.resources.last_six_months
import neutron.composeapp.generated.resources.last_three_months
import neutron.composeapp.generated.resources.last_week_period
import neutron.composeapp.generated.resources.last_year
import neutron.composeapp.generated.resources.no_revenues_yet
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * The [RevenuesScreen] displays the list of the revenues owned by the user
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
 * @see RevenuesContainerScreen
 */
class RevenuesScreen : EquinoxScreen<RevenuesScreenViewModel>(
    viewModel = RevenuesScreenViewModel()
), RevenuesContainerScreen {

    private companion object {

        /**
         *`POSITIVE_TREND` the positive sign constant value
         */
        const val POSITIVE_TREND = "+"

    }

    /**
     *`walletStatus` the current status of the wallet
     */
    private lateinit var walletStatus: State<WalletStatus?>

    /**
     * `revenuePeriod` the current period of the revenues displayed
     */
    private lateinit var revenuePeriod: State<RevenuePeriod>

    /**
     *`hideBalances` state used to manage the visibilities of the balances
     */
    override lateinit var hideBalances: State<Boolean>

    /**
     * Method to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        CloseApplicationOnNavBack()
        NeutronTheme {
            ManagedContent(
                viewModel = viewModel,
                initialDelay = 500L,
                loadingRoutine = { walletStatus.value != null },
                content = {
                    Scaffold(
                        snackbarHost = { SnackbarHost(viewModel.snackbarHostState!!) },
                        floatingActionButton = {
                            FabButton()
                        }
                    ) {
                        ScreenContent()
                    }
                },
                noInternetConnectionRetryText = com.tecknobit.equinoxcompose.resources.Res.string.retry,
                noInternetConnectionRetryAction = { viewModel.revenuesState.retryLastFailedRequest() }
            )
        }
    }

    /**
     * Method to get the extended floating action button text
     *
     * @return the text to for the extended floating action as [StringResource]
     */
    override fun extendedFabText(): StringResource {
        return Res.string.add_revenue
    }

    /**
     * Method to navigate to the related [com.tecknobit.neutron.ui.screens.insert.shared.presenter.InsertScreen]
     */
    override fun navToInsert() {
        navigator.navigate(INSERT_REVENUE_SCREEN)
    }

    /**
     * Method to display the custom content of the screen
     */
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

    /**
     * The header section of the screen
     */
    @Composable
    @ScreenSection
    @NonRestartableComposable
    override fun Header() {
        Card (
            modifier = Modifier
                .height(175.dp)
                .widthIn(
                    max = EXPANDED_CONTAINER
                ),
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
                    )
                    .padding(
                        top = 11.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                WalletStatus()
                UserProfilePicture()
            }
            FiltersBar(
                viewModel = viewModel
            )
        }
    }

    /**
     * The section used to display the current status of the wallet
     */
    @Composable
    private fun RowScope.WalletStatus() {
        Column(
            modifier = Modifier
                .weight(2f)
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = manageWalletBalanceVisibility(),
                    fontFamily = displayFontFamily,
                    fontSize = 30.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                IconButton(
                    onClick = { viewModel.manageBalancesVisibility() }
                ) {
                    Icon(
                        imageVector = if(hideBalances.value)
                            Icons.Default.Visibility
                        else
                            Icons.Default.VisibilityOff,
                        contentDescription = null
                    )
                }
            }
            WalletTrend()
        }
    }

    /**
     * Method to manage the wallet balance visibility based on the [hideBalances]
     *
     * @return the wallet balance value to display as [String]
     */
    private fun manageWalletBalanceVisibility(): String {
        return if(hideBalances.value)
            "****"
        else
            "${walletStatus.value!!.totalEarnings}" + localUser.currency.symbol
    }

    /**
     * The section used to display the current trend of the wallet
     */
    @Composable
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
            AnimatedContent(
                targetState = hideBalances.value
            ) { hide ->
                if (hide) {
                    Text(
                        text = HIDE_BALANCE,
                        fontSize = 18.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                } else {
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        val color = if (isPositiveTrend)
                            MaterialTheme.colorScheme.primary
                        else if (trend < 0.0)
                            MaterialTheme.colorScheme.error
                        else
                            MaterialTheme.colorScheme.onSurface
                        Text(
                            text = "$symbol${trend}%",
                            fontFamily = displayFontFamily,
                            color = color,
                            fontSize = 18.sp,
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
        }
    }

    /**
     * The user profile picture used to navigate to the [com.tecknobit.neutron.ui.screens.profile.presenter.ProfileScreen]
     */
    @Composable
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

    /**
     * The current revenues owned by the user
     */
    @Composable
    private fun Revenues() {
        PaginatedLazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .widthIn(
                    max = EXPANDED_CONTAINER
                )
                .navigationBarsPadding(),
            paginationState = viewModel.revenuesState,
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
                items = viewModel.revenuesState.allItems!!,
                key = { _, revenue -> revenue.id }
            ) { index, revenue ->
                RevenueCard(
                    viewModel = viewModel,
                    revenue = revenue,
                    position = index
                )
            }
        }
    }

    /**
     * Method invoked when the [ShowContent] composable has been started
     */
    override fun onStart() {
        super.onStart()
        viewModel.getWalletStatus()
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
        walletStatus = viewModel.walletStatus.collectAsState()
        revenuePeriod = viewModel.revenuePeriod.collectAsState()
        hideBalances = viewModel.hideBalances.collectAsState()
    }

}