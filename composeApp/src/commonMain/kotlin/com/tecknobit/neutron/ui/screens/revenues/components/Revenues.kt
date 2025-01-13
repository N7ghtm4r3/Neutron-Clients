package com.tecknobit.neutron.ui.screens.revenues.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.tecknobit.neutron.localUser
import com.tecknobit.neutron.ui.screens.revenues.data.GeneralRevenue
import com.tecknobit.neutron.ui.screens.revenues.data.ProjectRevenue
import com.tecknobit.neutron.ui.screens.revenues.data.Revenue
import com.tecknobit.neutron.ui.screens.revenues.presentation.RevenuesScreenViewModel

@Composable
@NonRestartableComposable
fun RevenueItem(
    viewModel: RevenuesScreenViewModel,
    revenue: Revenue,
) {
    when (revenue) {
        is GeneralRevenue -> {
            GeneralRevenue(
                viewModel = viewModel,
                revenue = revenue
            )
        }

        else -> {
            ProjectRevenue(
                viewModel = viewModel,
                revenue = revenue as ProjectRevenue
            )
        }
    }
    HorizontalDivider()
}

@Composable
@NonRestartableComposable
private fun GeneralRevenue(
    viewModel: RevenuesScreenViewModel,
    revenue: GeneralRevenue,
) {
    ListItem(
        headlineContent = {
            RevenueInfo(
                revenue = revenue
            )
        },
        trailingContent = {

        }
    )
}

@Composable
@NonRestartableComposable
private fun ProjectRevenue(
    viewModel: RevenuesScreenViewModel,
    revenue: ProjectRevenue,
) {

}

@Composable
@NonRestartableComposable
private fun RevenueInfo(
    revenue: Revenue,
) {
    Column {
        Text(
            text = revenue.title
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = revenue.value.toString()
            )
            Text(
                text = localUser.currency!!.symbol
            )
        }
    }
}