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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.tecknobit.neutron.bodyFontFamily
import com.tecknobit.neutron.ui.screens.shared.presentations.RevenueRelatedScreenViewModel
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

/**
 * Custom [FilterChip] used to select a [RevenuePeriod] value
 *
 * @param viewModel The support viewmodel for the screen
 */
@Composable
fun PeriodFilterChip(
    viewModel: RevenueRelatedScreenViewModel
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

/**
 * Menu to display and to select a specific [RevenuePeriod] value
 *
 * @param viewModel The support viewmodel for the screen
 * @param expanded Whether the menu has been expanded or not
 */
@Composable
private fun PeriodsMenu(
    viewModel: RevenueRelatedScreenViewModel,
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

/**
 * Method to convert a [RevenuePeriod] value as [String]
 *
 * @return the converted value as [String]
 */
@Composable
private fun RevenuePeriod.asText() : String {
    return stringResource(
        resource = this.asResource()
    ).replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}

/**
 * Method to internationalize the [RevenuePeriod] value
 *
 * @return the international value of the [RevenuePeriod] as [StringResource]
 */
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