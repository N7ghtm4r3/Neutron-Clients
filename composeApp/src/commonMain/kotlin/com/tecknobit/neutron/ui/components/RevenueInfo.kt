package com.tecknobit.neutron.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcore.time.TimeFormatter.COMPLETE_EUROPEAN_DATE_PATTERN
import com.tecknobit.equinoxcore.time.TimeFormatter.toDateString
import com.tecknobit.neutron.bodyFontFamily
import com.tecknobit.neutron.displayFontFamily
import com.tecknobit.neutron.localUser
import com.tecknobit.neutron.ui.screens.project.data.TicketRevenue
import com.tecknobit.neutron.ui.screens.project.presentation.ProjectScreenViewModel
import com.tecknobit.neutron.ui.screens.shared.data.Revenue
import com.tecknobit.neutron.ui.screens.shared.presentations.RevenueRelatedScreenViewModel
import com.tecknobit.neutron.ui.screens.shared.presenters.RevenuesContainerScreen.Companion.HIDE_BALANCE
import neutron.composeapp.generated.resources.Res
import neutron.composeapp.generated.resources.closing_date
import neutron.composeapp.generated.resources.date
import neutron.composeapp.generated.resources.duration
import neutron.composeapp.generated.resources.opening_date
import neutron.composeapp.generated.resources.revenue
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

/**
 * Used to display the information of a [TicketRevenue] such title, opening date, closing date, etc
 *
 * @param viewModel The support viewmodel for the screen
 * @param ticket The ticket to retrieve the information to display
 */
@Composable
fun TicketInfo(
    viewModel: ProjectScreenViewModel,
    ticket: TicketRevenue
) {
    Column {
        RevenueInfo(
            viewModel = viewModel,
            revenue = ticket,
            dateHeader = Res.string.opening_date
        )
        AnimatedVisibility(
            visible = !ticket.isPending()
        ) {
            Column {
                Text(
                    text = stringResource(
                        resource = Res.string.closing_date,
                        ticket.closingDate.toDateString()
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 14.sp,
                    fontFamily = bodyFontFamily
                )
                val duration = ticket.getTicketDuration()
                Text(
                    text = pluralStringResource(
                        resource = Res.plurals.duration,
                        quantity = duration,
                        duration
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 14.sp,
                    fontFamily = bodyFontFamily
                )
            }
        }
    }
}

/**
 * Used to display the information of a [Revenue] such title, value, etc
 *
 * @param viewModel The support viewmodel for the screen
 * @param revenue The revenue from retrieve the information to display
 * @param customTitle Value of a custom title to use for the component
 * @param dateHeader The header of the date
 * @param pattern The pattern to use to format the long value
 */
@Composable
fun RevenueInfo(
    viewModel: RevenueRelatedScreenViewModel,
    revenue: Revenue,
    customTitle: String? = null,
    dateHeader: StringResource = Res.string.date,
    pattern: String = COMPLETE_EUROPEAN_DATE_PATTERN,
) {
    val hideBalances by viewModel.hideBalances.collectAsState()
    Column {
        Text(
            text = customTitle ?: revenue.title,
            fontFamily = displayFontFamily,
            fontSize = 18.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        AnimatedVisibility(
            visible = hideBalances
        ) {
            Text(
                text = HIDE_BALANCE
            )
        }
        AnimatedVisibility(
            visible = !hideBalances
        ) {
            Text(
                text = stringResource(
                    resource = Res.string.revenue,
                    revenue.value,
                    localUser.currency.symbol
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp,
                fontFamily = bodyFontFamily
            )
        }
        Text(
            text = stringResource(
                resource = dateHeader,
                revenue.revenueDate.toDateString(
                    pattern = pattern
                )
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 14.sp,
            fontFamily = bodyFontFamily
        )
    }
}
