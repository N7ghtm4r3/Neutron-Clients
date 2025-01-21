package com.tecknobit.neutron.ui.screens.insert.revenue.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.tecknobit.equinoxcompose.components.EquinoxDialog
import com.tecknobit.equinoxcompose.components.EquinoxTextField
import com.tecknobit.equinoxcompose.components.getContrastColor
import com.tecknobit.equinoxcompose.utilities.toHex
import com.tecknobit.neutron.helpers.mergeIfNotContained
import com.tecknobit.neutron.helpers.retainAndAdd
import com.tecknobit.neutron.ui.components.LabelsGrid
import com.tecknobit.neutron.ui.icons.ArrowSelectorTool
import com.tecknobit.neutron.ui.screens.insert.revenue.presentation.InsertRevenueScreenViewModel
import com.tecknobit.neutron.ui.screens.revenues.data.RevenueLabel
import neutron.composeapp.generated.resources.Res
import neutron.composeapp.generated.resources.add
import neutron.composeapp.generated.resources.create
import neutron.composeapp.generated.resources.label_text
import neutron.composeapp.generated.resources.select
import org.jetbrains.compose.resources.stringResource

@Composable
@NonRestartableComposable
fun LabelsPicker(
    show: MutableState<Boolean>,
    viewModel: InsertRevenueScreenViewModel
) {
    EquinoxDialog(
        show = show
    ) {
        Card(
            modifier = Modifier
                .size(
                    width = 400.dp,
                    height = 375.dp
                ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            )
        ) {
            val creatingNewLabel = remember { mutableStateOf(true) }
            SectionSelector(
                creatingNewLabel = creatingNewLabel,
                onCreating = {
                    CreateLabel(
                        show = show,
                        viewModel = viewModel
                    )
                },
                onSelecting = {
                    SelectLabels(
                        show = show,
                        viewModel = viewModel
                    )
                }
            )
        }
    }
}

@Composable
@NonRestartableComposable
private fun ColumnScope.SectionSelector(
    creatingNewLabel: MutableState<Boolean>,
    onCreating: @Composable () -> Unit,
    onSelecting: @Composable () -> Unit
) {
    SingleChoiceSegmentedButtonRow (
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 10.dp
            )
            .align(Alignment.CenterHorizontally)
    ) {
        SegmentedButton(
            selected = creatingNewLabel.value,
            shape = RoundedCornerShape(
                topStart = 7.dp,
                bottomStart = 7.dp
            ),
            icon = {
                Icon(
                    imageVector = Icons.Default.Create,
                    contentDescription = null
                )
            },
            label = {
                Text(
                    text = stringResource(Res.string.create)
                )
            },
            onClick = {
                if(!creatingNewLabel.value)
                    creatingNewLabel.value = true
            }
        )
        SegmentedButton(
            selected = !creatingNewLabel.value,
            shape = RoundedCornerShape(
                topEnd = 7.dp,
                bottomEnd = 7.dp
            ),
            icon = {
                Icon(
                    imageVector = ArrowSelectorTool,
                    contentDescription = null
                )
            },
            label = {
                Text(
                    text = stringResource(Res.string.select)
                )
            },
            onClick = {
                if(creatingNewLabel.value)
                    creatingNewLabel.value = false
            }
        )
    }
    AnimatedVisibility(
        visible = creatingNewLabel.value
    ) {
        onCreating.invoke()
    }
    AnimatedVisibility(
        visible = !creatingNewLabel.value
    ) {
        onSelecting.invoke()
    }
}

@Composable
@NonRestartableComposable
private fun CreateLabel(
    show: MutableState<Boolean>,
    viewModel: InsertRevenueScreenViewModel
) {
    val labelText = remember { mutableStateOf("") }
    val controller = rememberColorPickerController()
    Column (
        modifier = Modifier
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        DummyRevenueLabelBadge(
            modifier = Modifier
                .widthIn(
                    max = 200.dp
                ),
            color = controller.selectedColor.value,
            labelText = labelText
        )
        HsvColorPicker(
            modifier = Modifier
                .fillMaxWidth()
                .height(165.dp),
            controller = controller
        )
        ConfirmOperationButton(
            visible = labelText.value.isNotEmpty(),
            action = {
                viewModel.labels.add(
                    RevenueLabel(
                        text = labelText.value,
                        color = controller.selectedColor.value.toHex()
                    )
                )
                show.value = false
            }
        )
    }
}

@Composable
@NonRestartableComposable
private fun DummyRevenueLabelBadge(
    modifier: Modifier = Modifier,
    color: Color,
    labelText: MutableState<String>
) {
    Card (
        modifier = modifier,
        colors = CardDefaults.outlinedCardColors(
            containerColor = color
        ),
        shape = RoundedCornerShape(
            size = 5.dp
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 3.dp
        )
    ) {
        val onLabelColor = getContrastColor(
            backgroundColor = color
        )
        EquinoxTextField(
            textFieldColors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedLabelColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedTextColor = onLabelColor,
                unfocusedTextColor = onLabelColor
            ),
            value = labelText,
            maxLines = 1,
            placeholder = stringResource(Res.string.label_text),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            )
        )
    }
}

@Composable
@NonRestartableComposable
private fun SelectLabels(
    show: MutableState<Boolean>,
    viewModel: InsertRevenueScreenViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val tmpLabels = remember { mutableStateListOf<RevenueLabel>() }
        LaunchedEffect(show.value) {
            tmpLabels.mergeIfNotContained(
                collectionToMerge = viewModel.labels
            )
        }
        Column(
            modifier = Modifier
                .weight(
                    weight = 1f,
                    fill = false
                )
        ) {
            LabelsGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = 16.dp
                    ),
                labelsRetriever = viewModel,
                labels = tmpLabels
            )
        }
        ConfirmOperationButton(
            visible = tmpLabels.isNotEmpty(),
            action = {
                viewModel.labels.retainAndAdd(
                    supportCollection = tmpLabels
                )
                show.value = false
            }
        )
    }
}

@Composable
@NonRestartableComposable
private fun ColumnScope.ConfirmOperationButton(
    modifier: Modifier = Modifier,
    visible: Boolean,
    action: () -> Unit,
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = visible
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    end = 10.dp,
                    bottom = 10.dp
                )
                .align(Alignment.End),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = action
            ) {
                Text(
                    text = stringResource(Res.string.add)
                )
            }
        }
    }
}