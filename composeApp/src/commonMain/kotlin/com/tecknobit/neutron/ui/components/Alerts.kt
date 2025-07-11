package com.tecknobit.neutron.ui.components

import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import com.tecknobit.neutron.SPLASHSCREEN
import com.tecknobit.neutron.displayFontFamily
import com.tecknobit.neutron.navigator
import com.tecknobit.neutron.ui.screens.profile.presentation.ProfileScreenViewModel
import com.tecknobit.neutron.ui.screens.project.data.TicketRevenue
import com.tecknobit.neutron.ui.screens.project.presentation.ProjectScreenViewModel
import com.tecknobit.neutron.ui.screens.shared.data.Revenue
import com.tecknobit.neutron.ui.screens.shared.presentations.RevenueRelatedScreenViewModel
import neutron.composeapp.generated.resources.Res
import neutron.composeapp.generated.resources.delete_account
import neutron.composeapp.generated.resources.delete_revenue
import neutron.composeapp.generated.resources.delete_revenue_text
import neutron.composeapp.generated.resources.delete_ticket
import neutron.composeapp.generated.resources.delete_ticket_text
import neutron.composeapp.generated.resources.delete_warn_text
import neutron.composeapp.generated.resources.logout
import neutron.composeapp.generated.resources.logout_warn_text

/**
 * **titleStyle** -> the style to apply to the title of the [EquinoxAlertDialog]
 */
val titleStyle = TextStyle(
    fontFamily = displayFontFamily,
    fontSize = 20.sp
)

/**
 * Custom [EquinoxAlertDialog] used to warn the user about the deletion of a [Revenue]
 *
 * @param show Whether the dialog is shown
 * @param viewModel The support viewmodel for the screen
 * @param revenue The revenue to delete
 * @param onDelete The action to execute when the user confirm the revenue deletion
 */
@Composable
fun DeleteRevenue(
    show: MutableState<Boolean>,
    viewModel: RevenueRelatedScreenViewModel,
    revenue: Revenue,
    onDelete: () -> Unit
) {
    EquinoxAlertDialog(
        modifier = Modifier
            .widthIn(
                max = 400.dp
            ),
        show = show,
        viewModel = viewModel,
        title = Res.string.delete_revenue,
        titleStyle = titleStyle,
        text = Res.string.delete_revenue_text,
        confirmAction = {
            show.value = false
            viewModel.deleteRevenue(
                revenue = revenue,
                onDelete = onDelete
            )
        }
    )
}

/**
 * Custom [EquinoxAlertDialog] used to warn the user about the deletion of a [TicketRevenue]
 *
 * @param show Whether the dialog is shown
 * @param viewModel The support viewmodel for the screen
 * @param ticket The ticket to delete
 */
@Composable
fun DeleteTicket(
    show: MutableState<Boolean>,
    viewModel: ProjectScreenViewModel,
    ticket: TicketRevenue
) {
    EquinoxAlertDialog(
        modifier = Modifier
            .widthIn(
                max = 400.dp
            ),
        show = show,
        viewModel = viewModel,
        title = Res.string.delete_ticket,
        titleStyle = titleStyle,
        text = Res.string.delete_ticket_text,
        confirmAction = {
            viewModel.deleteTicket(
                ticket = ticket
            )
        }
    )
}

/**
 * Alert to warn about the logout action
 *
 * @param viewModel The support viewmodel for the screen
 * @param show Whether the alert is shown
 */
@Composable
fun Logout(
    viewModel: ProfileScreenViewModel,
    show: MutableState<Boolean>
) {
    EquinoxAlertDialog(
        icon = Icons.AutoMirrored.Filled.Logout,
        modifier = Modifier
            .widthIn(
                max = 400.dp
            ),
        viewModel = viewModel,
        show = show,
        title = Res.string.logout,
        titleStyle = titleStyle,
        text = Res.string.logout_warn_text,
        confirmAction = {
            viewModel.clearSession {
                show.value = false
                navigator.navigate(SPLASHSCREEN)
            }
        }
    )
}

/**
 * Alert to warn about the account deletion
 *
 * @param viewModel The support viewmodel for the screen
 * @param show Whether the alert is shown
 */
@Composable
fun DeleteAccount(
    viewModel: ProfileScreenViewModel,
    show: MutableState<Boolean>
) {
    EquinoxAlertDialog(
        icon = Icons.Default.Delete,
        modifier = Modifier
            .widthIn(
                max = 400.dp
            ),
        viewModel = viewModel,
        show = show,
        title = Res.string.delete_account,
        titleStyle = titleStyle,
        text = Res.string.delete_warn_text,
        confirmAction = {
            viewModel.deleteAccount(
                onDelete = {
                    show.value = false
                    navigator.navigate(SPLASHSCREEN)
                }
            )
        }
    )
}