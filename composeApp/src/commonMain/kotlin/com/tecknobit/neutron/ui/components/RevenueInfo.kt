package com.tecknobit.neutron.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcore.time.TimeFormatter.toDateString
import com.tecknobit.neutron.bodyFontFamily
import com.tecknobit.neutron.displayFontFamily
import com.tecknobit.neutron.localUser
import com.tecknobit.neutron.ui.screens.revenues.data.Revenue
import com.tecknobit.neutron.ui.screens.revenues.data.TicketRevenue
import neutron.composeapp.generated.resources.Res
import neutron.composeapp.generated.resources.closing_date
import neutron.composeapp.generated.resources.date
import neutron.composeapp.generated.resources.duration
import neutron.composeapp.generated.resources.opening_date
import neutron.composeapp.generated.resources.revenue
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import kotlin.math.round

@Composable
@NonRestartableComposable
fun TicketInfo(
    ticket: TicketRevenue
) {
    Column {
        RevenueInfo(
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

@Composable
@NonRestartableComposable
fun RevenueInfo(
    revenue: Revenue,
    dateHeader: StringResource = Res.string.date,
    customTitle: String? = null
) {
    Column {
        Text(
            text = customTitle ?: revenue.title,
            fontFamily = displayFontFamily,
            fontSize = 18.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = stringResource(
                resource = Res.string.revenue,
                round(revenue.value), // TODO: CHANGE WITH ANY METHOD TO FORMAT DIGITS INSTEAD 
                localUser.currency.symbol
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 14.sp,
            fontFamily = bodyFontFamily
        )
        Text(
            text = stringResource(
                resource = dateHeader,
                revenue.revenueDateAsString()
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 14.sp,
            fontFamily = bodyFontFamily
        )
    }
}
