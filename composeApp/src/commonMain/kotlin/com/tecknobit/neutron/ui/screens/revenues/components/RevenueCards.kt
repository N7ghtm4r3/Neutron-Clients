@file:OptIn(ExperimentalUuidApi::class, ExperimentalMultiplatform::class)

package com.tecknobit.neutron.ui.screens.revenues.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import com.tecknobit.neutron.INSERT_REVENUE_SCREEN
import com.tecknobit.neutron.PROJECT_REVENUE_SCREEN
import com.tecknobit.neutron.navigator
import com.tecknobit.neutron.ui.components.DeleteRevenue
import com.tecknobit.neutron.ui.components.RevenueDescription
import com.tecknobit.neutron.ui.components.RevenueListItem
import com.tecknobit.neutron.ui.icons.ContractDelete
import com.tecknobit.neutron.ui.screens.revenues.data.GeneralRevenue
import com.tecknobit.neutron.ui.screens.revenues.data.ProjectRevenue
import com.tecknobit.neutron.ui.screens.revenues.data.Revenue
import com.tecknobit.neutron.ui.screens.revenues.data.RevenueLabel
import com.tecknobit.neutron.ui.screens.revenues.presentation.RevenuesScreenViewModel
import com.tecknobit.neutroncore.PROJECT_LABEL_COLOR
import neutron.composeapp.generated.resources.Res
import neutron.composeapp.generated.resources.project
import org.jetbrains.compose.resources.stringResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Composable
@NonRestartableComposable
fun RevenueCard(
    viewModel: RevenuesScreenViewModel,
    revenue: Revenue,
    position: Int
) {
    when (revenue) {
        is GeneralRevenue -> {
            GeneralRevenueCard(
                viewModel = viewModel,
                revenue = revenue,
                position = position
            )
        }

        else -> {
            ProjectRevenueCard(
                viewModel = viewModel,
                revenue = revenue as ProjectRevenue,
                position = position
            )
        }
    }
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

@Composable
@NonRestartableComposable
private fun GeneralRevenueCard(
    viewModel: RevenuesScreenViewModel,
    revenue: GeneralRevenue,
    position: Int
) {
    ResponsiveContent(
        onExpandedSizeClass = {
            if(position %2 == 1) {
                Card {
                    GeneralRevenueContent(
                        viewModel = viewModel,
                        revenue = revenue,
                        containerColor = Color.Transparent
                    )
                }
            } else {
                GeneralRevenueContent(
                    viewModel = viewModel,
                    revenue = revenue,
                    containerColor = Color.Transparent
                )
            }
        },
        onMediumSizeClass = {
            GeneralRevenueContent(
                viewModel = viewModel,
                revenue = revenue
            )
        },
        onCompactSizeClass = {
            GeneralRevenueContent(
                viewModel = viewModel,
                revenue = revenue
            )
        }
    )
}

@Composable
@NonRestartableComposable
private fun GeneralRevenueContent(
    viewModel: RevenuesScreenViewModel,
    revenue: GeneralRevenue,
    containerColor: Color = ListItemDefaults.containerColor
) {
    Column {
        var expanded by remember { mutableStateOf(false) }
        RevenueItem(
            viewModel = viewModel,
            revenue = revenue,
            containerColor = containerColor,
            labels = revenue.labels,
            deleteIcon = ContractDelete,
            actionButton = {
                IconButton(
                    onClick = { expanded = !expanded }
                ) {
                    Icon(
                        imageVector = if(expanded)
                            Icons.Default.ExpandLess
                        else
                            Icons.Default.ExpandMore,
                        contentDescription = null
                    )
                }
            }
        )
        RevenueDescription(
            expanded = expanded,
            revenue = revenue
        )
    }
}

@Composable
@NonRestartableComposable
private fun ProjectRevenueCard(
    viewModel: RevenuesScreenViewModel,
    revenue: ProjectRevenue,
    position: Int
) {
    ResponsiveContent(
        onExpandedSizeClass = {
            if(position %2 == 1) {
                Card {
                    ProjectRevenueContent(
                        viewModel = viewModel,
                        revenue = revenue,
                        containerColor = Color.Transparent
                    )
                }
            } else {
                ProjectRevenueContent(
                    viewModel = viewModel,
                    revenue = revenue,
                    containerColor = Color.Transparent
                )
            }
        },
        onMediumSizeClass = {
            ProjectRevenueContent(
                viewModel = viewModel,
                revenue = revenue
            )
        },
        onCompactSizeClass = {
            ProjectRevenueContent(
                viewModel = viewModel,
                revenue = revenue
            )
        }
    )
}

@Composable
@NonRestartableComposable
private fun ProjectRevenueContent(
    viewModel: RevenuesScreenViewModel,
    revenue: ProjectRevenue,
    containerColor: Color = ListItemDefaults.containerColor
) {
    RevenueItem(
        viewModel = viewModel,
        revenue = revenue,
        containerColor = containerColor,
        labels = listOf(
            RevenueLabel(
                id = Uuid.random().toHexString(),
                text = stringResource(Res.string.project),
                color = PROJECT_LABEL_COLOR
            )
        ),
        deleteIcon = Icons.Default.Delete,
        actionButton = {
            IconButton(
                onClick = { navigator.navigate("$PROJECT_REVENUE_SCREEN/${revenue.id}") }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.NavigateNext,
                    contentDescription = null
                )
            }
        }
    )
}

@Composable
@NonRestartableComposable
private fun RevenueItem(
    viewModel: RevenuesScreenViewModel,
    revenue: Revenue,
    labels: List<RevenueLabel>,
    containerColor: Color,
    deleteIcon: ImageVector,
    actionButton: @Composable () -> Unit
) {
    RevenueListItem(
        revenue = revenue,
        labels = labels,
        containerColor = containerColor,
        onEdit = {
            navigator.navigate("$INSERT_REVENUE_SCREEN/${revenue.id}")
        },
        deleteIcon = deleteIcon,
        actionButton = actionButton,
        deleteAlertDialog = { delete ->
            DeleteRevenue(
                show = delete,
                viewModel = viewModel,
                revenue = revenue
            )
        }
    )
}

