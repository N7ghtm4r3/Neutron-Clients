package com.tecknobit.neutron.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.tecknobit.neutron.bodyFontFamily
import com.tecknobit.neutron.displayFontFamily
import com.tecknobit.neutron.localUser
import com.tecknobit.neutron.ui.screens.revenues.data.Revenue
import neutron.composeapp.generated.resources.Res
import neutron.composeapp.generated.resources.date
import neutron.composeapp.generated.resources.revenue
import org.jetbrains.compose.resources.stringResource
import kotlin.math.round

@Composable
@NonRestartableComposable
fun RevenueInfo(
    revenue: Revenue,
    customTitle: String? = null
) {
    Column {
        Text(
            text = customTitle ?: revenue.title,
            fontFamily = displayFontFamily,
            fontSize = 20.sp,
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
                resource = Res.string.date,
                revenue.revenueDateAsString()
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 14.sp,
            fontFamily = bodyFontFamily
        )
    }
}