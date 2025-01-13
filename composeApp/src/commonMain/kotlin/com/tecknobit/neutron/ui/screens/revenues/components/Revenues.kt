@file:OptIn(ExperimentalUuidApi::class)

package com.tecknobit.neutron.ui.screens.revenues.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.components.ChameleonText
import com.tecknobit.equinoxcompose.utilities.toColor
import com.tecknobit.neutron.bodyFontFamily
import com.tecknobit.neutron.displayFontFamily
import com.tecknobit.neutron.localUser
import com.tecknobit.neutron.ui.icons.ContractDelete
import com.tecknobit.neutron.ui.screens.revenues.data.GeneralRevenue
import com.tecknobit.neutron.ui.screens.revenues.data.ProjectRevenue
import com.tecknobit.neutron.ui.screens.revenues.data.Revenue
import com.tecknobit.neutron.ui.screens.revenues.data.RevenueLabel
import com.tecknobit.neutron.ui.screens.revenues.presentation.RevenuesScreenViewModel
import com.tecknobit.neutroncore.PROJECT_LABEL_COLOR
import neutron.composeapp.generated.resources.Res
import neutron.composeapp.generated.resources.date
import neutron.composeapp.generated.resources.project
import neutron.composeapp.generated.resources.revenue
import org.jetbrains.compose.resources.stringResource
import kotlin.math.round
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

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
    var expanded by remember { mutableStateOf(false) }
    Column {
        RevenueCard(
            viewModel = viewModel,
            revenue = revenue,
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
        AnimatedVisibility(
            visible = expanded
        ) {
            Text(
                modifier = Modifier
                    .padding(
                        horizontal = 16.dp
                    )
                    .padding(
                        bottom = 16.dp
                    ),
                text = revenue.description,
                textAlign = TextAlign.Justify,
                overflow = TextOverflow.Ellipsis,
                fontFamily = bodyFontFamily
            )
        }
    }
}

@Composable
@NonRestartableComposable
private fun ProjectRevenue(
    viewModel: RevenuesScreenViewModel,
    revenue: ProjectRevenue,
) {
    RevenueCard(
        viewModel = viewModel,
        revenue = revenue,
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
                onClick = {
                    // TODO: NAV TO PROJECT
                }
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
private fun RevenueCard(
    viewModel: RevenuesScreenViewModel,
    revenue: Revenue,
    labels: List<RevenueLabel>,
    deleteIcon: ImageVector,
    actionButton: @Composable () -> Unit
) {
    ListItem(
        headlineContent = {
            RevenueInfo(
                revenue = revenue
            )
        },
        overlineContent = {
            RevenueLabels(
                labels = labels
            )
        },
        trailingContent = {
            Column {
                IconButton(
                    onClick = {
                        viewModel.deleteRevenue(
                            revenue = revenue
                        )
                    }
                ) {
                    Icon(
                        imageVector = deleteIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                }
                actionButton()
            }
        }
    )
}

@Composable
@NonRestartableComposable
private fun RevenueInfo(
    revenue: Revenue,
) {
    Column {
        Text(
            text = revenue.title,
            fontFamily = displayFontFamily,
            fontSize = 20.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = stringResource(
                resource = Res.string.revenue,
                round(revenue.value),
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

@Composable
@NonRestartableComposable
private fun RevenueLabels(
    labels: List<RevenueLabel>
) {
    LazyRow (
        contentPadding = PaddingValues(
            vertical = 5.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(
            items = labels,
            key = { label -> label.id }
        ) { label ->
            Label(
                label = label
            )
        }
    }
}

@Composable
@NonRestartableComposable
private fun Label(
    label: RevenueLabel
) {
    val color = label.color.toColor()
    Card (
        colors = CardDefaults.outlinedCardColors(
            containerColor = color
        ),
        shape = RoundedCornerShape(
            size = 5.dp
        )
    ) {
        ChameleonText(
            modifier = Modifier
                .padding(
                    all = 4.dp
                ),
            backgroundColor = color,
            text = label.text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}