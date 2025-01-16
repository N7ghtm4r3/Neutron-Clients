package com.tecknobit.neutron.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

data class Step(
    val initiallyExpanded: Boolean = false,
    val enabled: MutableState<Boolean>? = null,
    val stepIcon: ImageVector,
    val title: StringResource,
    val content: @Composable ColumnScope.() -> Unit,
    val isError: (MutableState<Boolean>)? = null,
    val dismissAction: (() -> Unit)? = null,
    val confirmAction: (MutableState<Boolean>) -> Unit = { it.value = false }
)

@Composable
@NonRestartableComposable
@Deprecated("USE THE BUILT-ONE FROM EQUINOX")
// TODO: MORE CUSTOMIZATION IN THE OFFICIAL COMPONENT
fun Stepper(
    modifier: Modifier = Modifier,
    headerSection: @Composable (() -> Unit)? = null,
    startStepShape: Shape = RoundedCornerShape(
        topStart = 12.dp,
        topEnd = 12.dp
    ),
    middleStepShape: Shape = RectangleShape,
    finalStepShape: Shape = RoundedCornerShape(
        bottomStart = 12.dp,
        bottomEnd = 12.dp
    ),
    vararg steps: Step
) {
    val specialIndexes = remember { steps.mapSpecialIndexes() }
    val firstIndex = remember { specialIndexes.first }
    val lastIndex = remember { specialIndexes.second }
    val lastStep = remember { steps.last() }
    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        headerSection?.invoke()
        Column (
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            steps.forEachIndexed { index, step ->
                AnimatedVisibility(
                    visible = step.isEnabled()
                ) {
                    val shape = when(index) {
                        0 -> startStepShape
                        firstIndex -> {
                            if(steps[0].isEnabled())
                                middleStepShape
                            else
                                startStepShape
                        }
                        lastIndex -> {
                            if(lastIndex != steps.lastIndex && lastStep.isEnabled())
                                middleStepShape
                            else
                                finalStepShape
                        }
                        steps.lastIndex -> finalStepShape
                        else -> middleStepShape
                    }
                    StepAction(
                        shape = shape,
                        step = step,
                        bottomDivider = if(lastStep.isEnabled())
                            index != steps.lastIndex
                        else
                            index != lastIndex
                    )
                }
            }
        }
    }
}

private fun Array<out Step>.mapSpecialIndexes(): Pair<Int, Int> {
    val startIndex = this.findSpecialIndex(
        elementToCheck = this.first(),
        defaultIndexValue = 0
    )
    val tmpArray = this.reversedArray()
    val offset = this.size
    val lastIndex = tmpArray.findSpecialIndex(
        elementToCheck = this.last(),
        defaultIndexValue = this.lastIndex,
        onAssign = { index -> offset - index }
    )
    return Pair(
        first = startIndex,
        second = lastIndex
    )
}

private fun Array<out Step>.findSpecialIndex(
    elementToCheck: Step,
    defaultIndexValue: Int,
    onAssign: (Int) -> Int = { it }
) : Int {
    var specialIndex = defaultIndexValue
    if(elementToCheck.enabled != null) {
        forEachIndexed { index, step ->
            if(index != 0 && step.enabled == null) {
                specialIndex = onAssign.invoke(index)
                return@forEachIndexed
            }
        }
    }
    return specialIndex
}

private fun Step.isEnabled() : Boolean {
    return this.enabled?.value ?: true
}

/**
 * Section to execute a profile action
 *
 * @param shape The shape for the container
 * @param bottomDivider Whether create the bottom divider
 */
@Composable
@NonRestartableComposable
@Deprecated("USE THE BUILT-ONE FROM EQUINOX")
// TODO: MORE CUSTOMIZATION IN THE OFFICIAL COMPONENT
private fun StepAction(
    shape: Shape,
    step: Step,
    bottomDivider: Boolean = true
) {
    StepAction(
        shape = shape,
        step = step,
        controls = { expanded ->
            ActionControls(
                expanded = expanded,
                dismissAction = step.dismissAction,
                confirmAction = step.confirmAction
            )
        },
        bottomDivider = bottomDivider,
    )
}

/**
 * The controls action section to manage the [StepAction]
 *
 * @param expanded Whether the section is visible
 * @param dismissAction The action to execute when the action dismissed
 * @param confirmAction The action to execute when the action confirmed
 */
@Composable
@NonRestartableComposable
@Deprecated("USE THE BUILT-ONE FROM EQUINOX")
// TODO: MORE CUSTOMIZATION IN THE OFFICIAL COMPONENT
private fun ActionControls(
    expanded: MutableState<Boolean>,
    dismissAction: (() -> Unit)?,
    confirmAction: (MutableState<Boolean>) -> Unit,
) {
    Column (
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        AnimatedVisibility(
            visible = expanded.value
        ) {
            Row {
                dismissAction?.let { action ->
                    IconButton(
                        onClick = action
                    ) {
                        Icon(
                            imageVector = Icons.Default.Cancel, // TODO: ALLOW CUSTOM ICON
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error // TODO: ALLOW CUSTOM COLOR
                        )
                    }
                }
                IconButton(
                    onClick = {
                        confirmAction.invoke(
                            expanded
                        )
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle, // TODO: ALLOW CUSTOM ICON
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary // TODO: ALLOW CUSTOM COLOR
                    )
                }
            }
        }
        AnimatedVisibility(
            visible = !expanded.value
        ) {
            IconButton(
                onClick = { expanded.value = true }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, // TODO: ALLOW CUSTOM ICON
                    contentDescription = null
                )
            }
        }
    }
}

/**
 * Section to execute a profile action
 *
 * @param shape The shape for the container
 * @param bottomDivider Whether create the bottom divider
 */
@Composable
@NonRestartableComposable
@Deprecated("USE THE BUILT-ONE FROM EQUINOX")
// TODO: MORE CUSTOMIZATION IN THE OFFICIAL COMPONENT
private fun StepAction(
    shape: Shape = RoundedCornerShape(
        size = 0.dp
    ),
    step: Step,
    controls: @Composable (MutableState<Boolean>) -> Unit,
    bottomDivider: Boolean = true
) {
    Column {
        Card (
            colors = CardDefaults.cardColors(
                containerColor = if(step.isError?.value == true)
                    MaterialTheme.colorScheme.error // TODO: ALLOW CUSTOM COLOR
                else
                    MaterialTheme.colorScheme.surfaceContainerHighest // TODO: ALLOW CUSTOM COLOR
            ),
            shape = shape
        ) {
            val expanded = rememberSaveable {
                mutableStateOf(step.initiallyExpanded)
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = step.stepIcon,
                    contentDescription = null
                )
                Text(
                    modifier = Modifier
                        .padding(
                            start = 10.dp
                        ),
                    text = stringResource(step.title)
                )
                controls.invoke(expanded)
            }
            AnimatedVisibility(
                visible = expanded.value
            ) {
                step.isError?.value = false
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    HorizontalDivider()
                    step.content.invoke(this)
                }
            }
        }
        if(bottomDivider)
            HorizontalDivider()
    }
}