@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.neutron.ui.screens.revenues.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import com.tecknobit.equinoxcompose.components.getContrastColor
import com.tecknobit.equinoxcompose.utilities.toColor
import com.tecknobit.neutron.bodyFontFamily
import com.tecknobit.neutron.displayFontFamily
import com.tecknobit.neutron.helpers.retainAndAdd
import com.tecknobit.neutron.ui.icons.Target
import com.tecknobit.neutron.ui.screens.revenues.data.RevenueLabel
import com.tecknobit.neutron.ui.screens.revenues.presentation.RevenuesScreenViewModel
import com.tecknobit.neutroncore.enums.RevenuePeriod
import com.tecknobit.neutroncore.enums.RevenuePeriod.ALL
import com.tecknobit.neutroncore.enums.RevenuePeriod.LAST_MONTH
import com.tecknobit.neutroncore.enums.RevenuePeriod.LAST_SIX_MONTHS
import com.tecknobit.neutroncore.enums.RevenuePeriod.LAST_THREE_MONTHS
import com.tecknobit.neutroncore.enums.RevenuePeriod.LAST_WEEK
import com.tecknobit.neutroncore.enums.RevenuePeriod.LAST_YEAR
import neutron.composeapp.generated.resources.Res
import neutron.composeapp.generated.resources.all
import neutron.composeapp.generated.resources.generals
import neutron.composeapp.generated.resources.labels
import neutron.composeapp.generated.resources.last_month_period
import neutron.composeapp.generated.resources.last_six_months
import neutron.composeapp.generated.resources.last_three_months
import neutron.composeapp.generated.resources.last_week_period
import neutron.composeapp.generated.resources.last_year
import neutron.composeapp.generated.resources.projects
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
@NonRestartableComposable
fun FiltersBar(
    viewModel: RevenuesScreenViewModel
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 10.dp
            )
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        PeriodFilterChip(
            viewModel = viewModel
        )
        LabelsChip(
            viewModel = viewModel
        )
        RevenueTypeChip(
            type = Res.string.generals,
            onClick = { selected ->
                viewModel.applySelectGeneralRevenuesFilter(
                    select = selected
                )
            }
        )
        RevenueTypeChip(
            type = Res.string.projects,
            onClick = { selected ->
                viewModel.applySelectProjectsFilter(
                    select = selected
                )
            }
        )
    }
}

@Composable
@NonRestartableComposable
private fun PeriodFilterChip(
    viewModel: RevenuesScreenViewModel
) {
    Column {
        val filter = remember { mutableStateOf(false) }
        FilterChip(
            selected = filter.value,
            onClick = { filter.value = !filter.value },
            label = {
                Text(
                    text = viewModel.revenuePeriod.value.asText(),
                    fontFamily = bodyFontFamily
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = if(filter.value)
                        Icons.Default.ExpandLess
                    else
                        Icons.Default.ExpandMore,
                    contentDescription = null
                )
            }
        )
        PeriodsMenu(
            viewModel = viewModel,
            expanded = filter
        )
    }
}

@Composable
@NonRestartableComposable
private fun PeriodsMenu(
    viewModel: RevenuesScreenViewModel,
    expanded: MutableState<Boolean>
) {
    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false }
    ) {
        RevenuePeriod.entries.forEach { period ->
            DropdownMenuItem(
                onClick = {
                    viewModel.setRevenuePeriodFilter(
                        revenuePeriod = period,
                        afterSet = { expanded.value = false }
                    )
                },
                text = {
                    Text(
                        text = period.asText()
                    )
                }
            )
        }
    }
}

@Composable
private fun RevenuePeriod.asText() : String {
    return stringResource(
        resource = this.asResource()
    ).capitalize()
}

private fun RevenuePeriod.asResource() : StringResource {
    return when(this) {
        LAST_WEEK -> Res.string.last_week_period
        LAST_MONTH -> Res.string.last_month_period
        LAST_THREE_MONTHS -> Res.string.last_three_months
        LAST_SIX_MONTHS -> Res.string.last_six_months
        LAST_YEAR -> Res.string.last_year
        ALL -> Res.string.all
    }
}

@Composable
private fun LabelsChip(
    viewModel: RevenuesScreenViewModel
) {
    val filter = remember { mutableStateOf(false) }
    FilterChip(
        selected = filter.value,
        onClick = { filter.value = !filter.value },
        label = {
            Text(
                text = stringResource(Res.string.labels),
                fontFamily = bodyFontFamily
            )
        },
        trailingIcon = {
            Icon(
                imageVector = if(filter.value)
                    Icons.Default.ExpandLess
                else
                    Icons.Default.ExpandMore,
                contentDescription = null
            )
        }
    )
    LabelsDialog(
        filtering = filter,
        viewModel = viewModel
    )
}

@Composable
private fun LabelsDialog(
    filtering: MutableState<Boolean>,
    viewModel: RevenuesScreenViewModel
) {
    val currentLabels = remember { mutableStateListOf<RevenueLabel>() }
    val supportLabelsList = remember { mutableListOf<RevenueLabel>() }
    LaunchedEffect(filtering.value) {
        supportLabelsList.clear()
        supportLabelsList.addAll(viewModel.labelsFilter)
        currentLabels.addAll(viewModel.retrieveUserLabels())
    }
    EquinoxAlertDialog(
        modifier = Modifier
            .sizeIn(
                maxWidth = 400.dp,
                maxHeight = 400.dp,
            ),
        show = filtering,
        viewModel = viewModel,
        title = Res.string.labels,
        titleStyle = TextStyle(
            fontSize = 20.sp,
            fontFamily = displayFontFamily
        ),
        text = {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(
                    all = 5.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                items(
                    items = currentLabels,
                    key = { label -> label.id }
                ) { label ->
                    var selected by remember { mutableStateOf(viewModel.labelsFilter.contains(label)) }
                    val color = label.color.toColor()
                    val containerColor = if(selected)
                        color
                    else
                        Color.Unspecified
                    OutlinedCard(
                        colors = CardDefaults.outlinedCardColors(
                            containerColor = containerColor
                        ),
                        border = BorderStroke(
                            width = 1.5.dp,
                            color = color
                        ),
                        onClick = {
                            selected = !selected
                            if(selected)
                                viewModel.labelsFilter.add(label)
                            else
                                viewModel.labelsFilter.remove(label)
                        }
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(
                                    all = 5.dp
                                ),
                            text = label.text,
                            color = if(selected) {
                                getContrastColor(
                                    backgroundColor = containerColor
                                )
                            } else
                                color,
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        },
        confirmAction = {
            viewModel.applyLabelsFilters(
                onApply = { filtering.value = false }
            )
        },
        onDismissAction = {
            viewModel.labelsFilter.retainAndAdd(
                supportCollection = supportLabelsList
            )
            filtering.value = false
        }
    )
}

@Composable
@NonRestartableComposable
private fun RevenueTypeChip(
    type: StringResource,
    onClick: (Boolean) -> Unit
) {
    var selected by remember { mutableStateOf(true) }
    FilterChip(
        selected = selected,
        label = {
            Text(
                text = stringResource(type)
            )
        },
        onClick = {
            selected = !selected
            onClick(selected)
        },
        trailingIcon = {
            Icon(
                modifier = Modifier
                    .size(20.dp),
                imageVector = if(selected)
                    Icons.Default.CheckCircle
                else
                    Target,
                contentDescription = null
            )
        }
    )
}