package com.tecknobit.neutron.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.tecknobit.neutron.bodyFontFamily
import com.tecknobit.neutron.helpers.PeriodFiltererViewModel
import com.tecknobit.neutroncore.enums.RevenuePeriod
import com.tecknobit.neutroncore.enums.RevenuePeriod.ALL
import com.tecknobit.neutroncore.enums.RevenuePeriod.LAST_MONTH
import com.tecknobit.neutroncore.enums.RevenuePeriod.LAST_SIX_MONTHS
import com.tecknobit.neutroncore.enums.RevenuePeriod.LAST_THREE_MONTHS
import com.tecknobit.neutroncore.enums.RevenuePeriod.LAST_WEEK
import com.tecknobit.neutroncore.enums.RevenuePeriod.LAST_YEAR
import neutron.composeapp.generated.resources.Res
import neutron.composeapp.generated.resources.all
import neutron.composeapp.generated.resources.last_month_period
import neutron.composeapp.generated.resources.last_six_months
import neutron.composeapp.generated.resources.last_three_months
import neutron.composeapp.generated.resources.last_week_period
import neutron.composeapp.generated.resources.last_year
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
@NonRestartableComposable
fun PeriodFilterChip(
    viewModel: PeriodFiltererViewModel
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
    viewModel: PeriodFiltererViewModel,
    expanded: MutableState<Boolean>
) {
    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false }
    ) {
        RevenuePeriod.entries.forEach { period ->
            DropdownMenuItem(
                onClick = {
                    viewModel.applyRevenuePeriodFilter(
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