package com.tecknobit.neutron.ui.screens.insert.revenue.presenter

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.getContrastColor
import com.tecknobit.equinoxcompose.session.EquinoxScreen
import com.tecknobit.equinoxcompose.utilities.toColor
import com.tecknobit.neutron.ui.components.Step
import com.tecknobit.neutron.ui.screens.insert.revenue.components.LabelsPicker
import com.tecknobit.neutron.ui.screens.insert.revenue.presentation.InsertRevenueScreenViewModel
import com.tecknobit.neutron.ui.screens.insert.shared.presenter.InsertScreen
import com.tecknobit.neutron.ui.screens.revenues.components.RevenueLabels
import com.tecknobit.neutron.ui.screens.shared.presenters.NeutronScreen
import neutron.composeapp.generated.resources.Res
import neutron.composeapp.generated.resources.add_revenue
import neutron.composeapp.generated.resources.edit_revenue
import neutron.composeapp.generated.resources.generals
import neutron.composeapp.generated.resources.labels
import neutron.composeapp.generated.resources.project
import neutron.composeapp.generated.resources.revenue_type
import org.jetbrains.compose.resources.stringResource

/**
 * The [InsertRevenueScreen] is used to allow the user to add or to edit a revenue
 *
 * @param revenueId The identifier of the revenue to edit
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxScreen
 * @see NeutronScreen
 * @see InsertScreen
 */
class InsertRevenueScreen(
    revenueId: String?
) : InsertScreen<InsertRevenueScreenViewModel>(
    revenueId = revenueId,
    addingTitle = Res.string.add_revenue,
    editingTitle = Res.string.edit_revenue,
    viewModel = InsertRevenueScreenViewModel(
        revenueId = revenueId
    )
) {

    /**
     * Method to get the custom insertion steps for the [FormSection]
     *
     * @return the custom insertion steps as [Array] of out [Step]
     */
    @Composable
    override fun getInsertionSteps(): Array<out Step> {
        return remember {
            if(!isEditing) {
                arrayOf(
                    Step(
                        initiallyExpanded = true,
                        stepIcon = Icons.Default.Savings,
                        title = Res.string.revenue_type,
                        content = { RevenueType() }
                    ),
                    titleStep,
                    descriptionStep,
                    Step(
                        enabled = viewModel!!.addingGeneralRevenue,
                        stepIcon = Icons.AutoMirrored.Filled.Label,
                        title = Res.string.labels,
                        content = { RevenueLabelsForm() }
                    ),
                    insertionDateStep
                )
            } else {
                arrayOf(
                    titleStep,
                    descriptionStep,
                    Step(
                        enabled = viewModel!!.addingGeneralRevenue,
                        stepIcon = Icons.AutoMirrored.Filled.Label,
                        title = Res.string.labels,
                        content = { RevenueLabelsForm() }
                    ),
                    insertionDateStep
                )
            }
        }
    }

    /**
     * This section allows to insert the type of the revenue to create
     */
    @Composable
    @NonRestartableComposable
    // TODO: ANNOTATE AS SPECIAL STEP WITH THE RELATED EQUINOX-ANNOTATION
    private fun RevenueType() {
        Row (
            modifier = Modifier
                .padding(
                    start = 4.dp
                )
                .fillMaxWidth()
                .selectableGroup(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = viewModel!!.addingGeneralRevenue.value,
                    onClick = {
                        if(!viewModel!!.addingGeneralRevenue.value)
                            viewModel!!.addingGeneralRevenue.value = true
                    }
                )
                Text(
                    text = stringResource(Res.string.generals)
                )
            }
            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = !viewModel!!.addingGeneralRevenue.value,
                    onClick = {
                        if(viewModel!!.addingGeneralRevenue.value)
                            viewModel!!.addingGeneralRevenue.value = false
                    }
                )
                Text(
                    text = stringResource(Res.string.project)
                )
            }
        }
    }

    /**
     * This section allows to insert the labels to attach to the revenue
     */
    @Composable
    @NonRestartableComposable
    // TODO: ANNOTATE AS SPECIAL STEP WITH THE RELATED EQUINOX-ANNOTATION
    private fun RevenueLabelsForm() {
        val pickLabels = remember { mutableStateOf(false) }
        RevenueLabels(
            contentPadding = PaddingValues(
                all = 16.dp
            ),
            labels = viewModel!!.labels,
            stickyHeaderContent = {
                SmallFloatingActionButton(
                    onClick = { pickLabels.value = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                }
            },
            trailingIcon = { label ->
                IconButton(
                    modifier = Modifier
                        .size(24.dp),
                    onClick = { viewModel!!.labels.remove(label) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = null,
                        tint = getContrastColor(
                            backgroundColor = label.color.toColor()
                        )
                    )
                }
            }
        )
        LabelsPicker(
            show = pickLabels,
            viewModel = viewModel!!
        )
    }

}