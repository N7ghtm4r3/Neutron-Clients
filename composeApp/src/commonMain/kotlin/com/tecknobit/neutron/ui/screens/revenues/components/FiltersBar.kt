@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.neutron.ui.screens.revenues.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import com.tecknobit.neutron.bodyFontFamily
import com.tecknobit.neutron.displayFontFamily
import com.tecknobit.neutron.helpers.mergeIfNotContained
import com.tecknobit.neutron.helpers.retainAndAdd
import com.tecknobit.neutron.ui.components.CategoryChip
import com.tecknobit.neutron.ui.components.LabelsGrid
import com.tecknobit.neutron.ui.components.PeriodFilterChip
import com.tecknobit.neutron.ui.screens.revenues.data.RevenueLabel
import com.tecknobit.neutron.ui.screens.revenues.presentation.RevenuesScreenViewModel
import neutron.composeapp.generated.resources.Res
import neutron.composeapp.generated.resources.generals
import neutron.composeapp.generated.resources.labels
import neutron.composeapp.generated.resources.projects
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
        CategoryChip(
            type = Res.string.generals,
            onClick = { retrieve ->
                viewModel.applyRetrieveGeneralRevenuesFilter(
                    retrieve = retrieve
                )
            }
        )
        CategoryChip(
            type = Res.string.projects,
            onClick = { retrieve ->
                viewModel.applyRetrieveProjectsFilter(
                    retrieve = retrieve
                )
            }
        )
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

// TODO: CHECK THE REAL BEHAVIOR
@Composable
private fun LabelsDialog(
    filtering: MutableState<Boolean>,
    viewModel: RevenuesScreenViewModel
) {
    val supportLabelsList = remember { mutableListOf<RevenueLabel>() }
    LaunchedEffect(filtering.value) {
        supportLabelsList.mergeIfNotContained(
            mergeCollection = viewModel.labelsFilter
        )
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
            LabelsGrid(
                labelsRetriever = viewModel,
                labels = viewModel.labelsFilter
            )
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