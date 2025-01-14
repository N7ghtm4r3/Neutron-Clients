@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.neutron.ui.screens.revenues.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.components.EquinoxDialog
import com.tecknobit.neutron.bodyFontFamily
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
import neutron.composeapp.generated.resources.labels
import neutron.composeapp.generated.resources.last_month_period
import neutron.composeapp.generated.resources.last_six_months
import neutron.composeapp.generated.resources.last_three_months
import neutron.composeapp.generated.resources.last_week_period
import neutron.composeapp.generated.resources.last_year
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
    }
}

@Composable
@NonRestartableComposable
private fun PeriodFilterChip(
    viewModel: RevenuesScreenViewModel
) {
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
@NonRestartableComposable
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
@NonRestartableComposable
private fun LabelsDialog(
    filtering: MutableState<Boolean>,
    viewModel: RevenuesScreenViewModel
) {
    EquinoxDialog(
        show = filtering,
        viewModel = viewModel
    ) {
        Surface(
            modifier = Modifier
                .size(4500.dp)
                .background(Color.Cyan),
        ) {
            viewModel.labelsFilter.forEach { label ->
                RevenueLabel(
                    modifier = Modifier
                        .padding(
                            all = 5.dp
                        ),
                    fontSize = 10.sp,
                    label = label
                )
            }
        }
    }
}

@Composable
@NonRestartableComposable
private fun LabelItem(
    label: RevenueLabel
) {
    ListItem(
        headlineContent = {
            RevenueLabel(
                label = label
            )
        },
        trailingContent = {

        }
    )
}