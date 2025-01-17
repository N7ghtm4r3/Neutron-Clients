package com.tecknobit.neutron.ui.screens.project.presenter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.resources.no_internet_connection
import com.tecknobit.equinoxcompose.session.EquinoxScreen
import com.tecknobit.equinoxcompose.session.ManagedContent
import com.tecknobit.neutron.MAX_CONTAINER_WIDTH
import com.tecknobit.neutron.displayFontFamily
import com.tecknobit.neutron.localUser
import com.tecknobit.neutron.navigator
import com.tecknobit.neutron.ui.screens.project.components.TicketsFilterBar
import com.tecknobit.neutron.ui.screens.project.presentation.ProjectScreenViewModel
import com.tecknobit.neutron.ui.screens.revenues.data.ProjectRevenue
import com.tecknobit.neutron.ui.theme.NeutronTheme
import neutron.composeapp.generated.resources.total_revenues
import org.jetbrains.compose.resources.stringResource

class ProjectScreen(
    projectId: String
) : EquinoxScreen<ProjectScreenViewModel>(
    viewModel = ProjectScreenViewModel(
        projectId = projectId
    )
) {

    private lateinit var project: State<ProjectRevenue?>

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
                        floatingActionButton = {
                            // FabButton()
                        }
                    ) {
                        ScreenContent()
                    }
                },
                loadingRoutine = { project.value != null },
                noInternetConnectionRetryText = com.tecknobit.equinoxcompose.resources.Res.string.no_internet_connection,
                noInternetConnectionRetryAction = { viewModel!!.ticketsState.retryLastFailedRequest() }
            )
        }
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
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        NavButton()
                        Text(
                            text = project.value!!.title,
                            fontSize = 35.sp,
                            fontFamily = displayFontFamily,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Text(
                        modifier = Modifier
                            .padding(
                                start = 16.dp
                            ),
                        text = stringResource(
                            resource = neutron.composeapp.generated.resources.Res.string.total_revenues,
                            project.value!!.getBalance(), localUser.currency.symbol
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 18.sp
                    )
                }
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

    override fun onStart() {
        super.onStart()
        viewModel!!.retrieveProject()
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
        project = viewModel!!.project.collectAsState()
    }

}