package com.tecknobit.neutron.ui.screens.project.presenter

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.tecknobit.neutron.INSERT_REVENUE_SCREEN
import com.tecknobit.neutron.INSERT_TICKET_SCREEN
import com.tecknobit.neutron.MAX_CONTAINER_WIDTH
import com.tecknobit.neutron.bodyFontFamily
import com.tecknobit.neutron.displayFontFamily
import com.tecknobit.neutron.localUser
import com.tecknobit.neutron.navigator
import com.tecknobit.neutron.ui.components.DeleteRevenue
import com.tecknobit.neutron.ui.components.FirstPageProgressIndicator
import com.tecknobit.neutron.ui.components.NewPageProgressIndicator
import com.tecknobit.neutron.ui.icons.ReceiptLong
import com.tecknobit.neutron.ui.screens.project.components.InitialRevenueItem
import com.tecknobit.neutron.ui.screens.project.components.TicketCard
import com.tecknobit.neutron.ui.screens.project.components.TicketsFilterBar
import com.tecknobit.neutron.ui.screens.project.presentation.ProjectScreenViewModel
import com.tecknobit.neutron.ui.screens.revenues.data.ProjectRevenue
import com.tecknobit.neutron.ui.screens.shared.presenters.RevenuesContainerScreen
import com.tecknobit.neutron.ui.screens.shared.presenters.RevenuesContainerScreen.Companion.HIDE_BALANCE
import com.tecknobit.neutron.ui.theme.NeutronTheme
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyColumn
import neutron.composeapp.generated.resources.Res
import neutron.composeapp.generated.resources.add_ticket
import neutron.composeapp.generated.resources.no_revenues_yet
import neutron.composeapp.generated.resources.total_revenues
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

class ProjectScreen(
    projectId: String
) : EquinoxScreen<ProjectScreenViewModel>(
    viewModel = ProjectScreenViewModel(
        projectId = projectId
    )
), RevenuesContainerScreen {

    private lateinit var project: State<ProjectRevenue?>

    private lateinit var balance: State<Double>

    override lateinit var hideBalances: State<Boolean>

    /**
     * Method to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        NeutronTheme {
            ManagedContent(
                viewModel = viewModel!!,
                content = {
                    Scaffold(
                        snackbarHost = { SnackbarHost(viewModel!!.snackbarHostState!!) },
                        floatingActionButton = { FabButton() }
                    ) {
                        ScreenContent()
                    }
                },
                loadingRoutine = { project.value != null },
                noInternetConnectionRetryText = com.tecknobit.equinoxcompose.resources.Res.string.retry,
                noInternetConnectionRetryAction = { viewModel!!.ticketsState.retryLastFailedRequest() }
            )
        }
    }

    override fun extendedFabText(): StringResource {
        return Res.string.add_ticket
    }

    override fun navToInsert() {
        navigator.navigate(INSERT_TICKET_SCREEN)
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
            Tickets()
        }
    }

    @Composable
    @NonRestartableComposable
    override fun Header() {
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
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(
                        top = 21.dp
                    )
            ) {
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NavButton()
                    Text(
                        modifier = Modifier
                            .weight(1f),
                        text = project.value!!.title,
                        fontSize = 30.sp,
                        fontFamily = displayFontFamily,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Toolbar()
                }
                TotalRevenues()
            }
            TicketsFilterBar(
                viewModel = viewModel!!
            )
        }
    }

    @Composable
    @NonRestartableComposable
    private fun NavButton() {
        IconButton(
            onClick = { navigator.goBack() }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null
            )
        }
    }

    @Composable
    @NonRestartableComposable
    private fun RowScope.Toolbar() {
        Row (
            modifier = Modifier
                .weight(1f),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = { navigator.navigate("$INSERT_REVENUE_SCREEN/${project.value!!.id}") }
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null
                )
            }
            val deleteProject = remember { mutableStateOf(false) }
            IconButton(
                onClick = { deleteProject.value = true }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            }
            DeleteRevenue(
                show = deleteProject,
                revenue = project.value!!,
                viewModel = viewModel!!,
                onDelete = { navigator.goBack() }
            )
        }
    }

    @Composable
    @NonRestartableComposable
    private fun TotalRevenues() {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            AnimatedVisibility(
                visible = hideBalances.value
            ) {
                Text(
                    text = HIDE_BALANCE
                )
            }
            AnimatedVisibility(
                visible = !hideBalances.value
            ) {
                Text(
                    modifier = Modifier
                        .padding(
                            start = 16.dp
                        ),
                    text = stringResource(
                        resource = Res.string.total_revenues,
                        balance.value, localUser.currency.symbol
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            IconButton(
                modifier = Modifier
                    .size(32.dp),
                onClick = { viewModel!!.manageBalancesVisibility() }
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
    }

    @Composable
    @NonRestartableComposable
    private fun Tickets() {
        PaginatedLazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .widthIn(
                    max = MAX_CONTAINER_WIDTH
                )
                .navigationBarsPadding(),
            paginationState = viewModel!!.ticketsState,
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
            item {
                InitialRevenueItem(
                    viewModel = viewModel!!,
                    initialRevenue = project.value!!.initialRevenue
                )
            }
            itemsIndexed(
                items = viewModel!!.ticketsState.allItems!!,
                key = { _, revenue -> revenue.id }
            ) { index, ticket ->
                TicketCard(
                    viewModel = viewModel!!,
                    ticket = ticket,
                    position = index
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel!!.retrieveProject()
        viewModel!!.getWalletBalance()
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
        project = viewModel!!.project.collectAsState()
        balance = viewModel!!.balance.collectAsState()
        hideBalances = viewModel!!.hideBalances.collectAsState()
    }

}