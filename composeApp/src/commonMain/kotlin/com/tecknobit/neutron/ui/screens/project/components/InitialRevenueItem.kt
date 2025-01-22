@file:OptIn(ExperimentalMultiplatform::class)

package com.tecknobit.neutron.ui.screens.project.components

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import com.tecknobit.neutron.ui.components.RevenueInfo
import com.tecknobit.neutron.ui.screens.project.presentation.ProjectScreenViewModel
import com.tecknobit.neutron.ui.screens.revenues.components.RevenueLabels
import com.tecknobit.neutron.ui.screens.revenues.data.Revenue
import com.tecknobit.neutron.ui.screens.revenues.data.RevenueLabel
import com.tecknobit.neutroncore.FIRST_INCOME_LABEL_COLOR
import neutron.composeapp.generated.resources.Res
import neutron.composeapp.generated.resources.first_income
import neutron.composeapp.generated.resources.initial_revenue
import org.jetbrains.compose.resources.stringResource

/**
 * Custom [ListItem] used to display an initial revenue of a project
 *
 * @param viewModel The support viewmodel for the screen
 * @param initialRevenue The initial revenue to display
 */
@Composable
@NonRestartableComposable
fun InitialRevenueItem(
    viewModel: ProjectScreenViewModel,
    initialRevenue: Revenue.RevenueImpl
) {
    ListItem(
        overlineContent = {
            RevenueLabels(
                labels = listOf(
                    RevenueLabel(
                        text = stringResource(Res.string.first_income),
                        color = FIRST_INCOME_LABEL_COLOR
                    )
                )
            )
        },
        headlineContent = {
            RevenueInfo(
                viewModel = viewModel,
                revenue = initialRevenue,
                customTitle = stringResource(Res.string.initial_revenue)
            )
        }
    )
    ResponsiveContent(
        onExpandedSizeClass = {},
        onMediumSizeClass = {
            HorizontalDivider()
        },
        onCompactSizeClass = {
            HorizontalDivider()
        }
    )
}