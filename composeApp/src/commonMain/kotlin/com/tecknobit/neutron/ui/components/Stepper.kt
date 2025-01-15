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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
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
    val enabled: MutableState<Boolean> = mutableStateOf(true),
    val stepIcon: ImageVector,
    val title: StringResource,
    val content: @Composable ColumnScope.() -> Unit,
    val dismissAction: (() -> Unit)? = null,
    val confirmAction: (MutableState<Boolean>) -> Unit
)

@Composable
@NonRestartableComposable
@Deprecated("USE THE BUILT-ONE FROM EQUINOX")
// TODO: MORE CUSTOMIZATION IN THE OFFICIAL COMPONENT
fun Stepper(
    startStepShape: Shape = RoundedCornerShape(
        topStart = 12.dp,
        topEnd = 12.dp
    ),
    middleStepShape: Shape = RectangleShape,
    finalStepShape: Shape = RoundedCornerShape(
        bottomStart = 12.dp,
        bottomEnd = 12.dp
    ),
    headerSection: @Composable (() -> Unit)? = null,
    vararg steps: Step
) {
    Column (
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        headerSection?.invoke()
        Column (
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            val lastIndex = steps.lastIndex
            steps.forEachIndexed { index, step ->
                AnimatedVisibility(
                    visible = step.enabled.value
                ) {
                    val shape = when(index) {
                        0 -> startStepShape
                        lastIndex -> finalStepShape
                        else -> middleStepShape
                    }
                    StepAction(
                        shape = shape,
                        stepIcon = step.stepIcon,
                        stepTitle = step.title,
                        stepContent = step.content,
                        dismissAction = step.dismissAction,
                        confirmAction = step.confirmAction,
                        bottomDivider = index != lastIndex
                    )
                }
            }
        }
    }
}

/**
 * Section to execute a profile action
 *
 * @param shape The shape for the container
 * @param stepIcon The representative leading icon
 * @param stepTitle The representative action text
 * @param stepContent The content to display to execute the action
 * @param dismissAction The action to execute when the action dismissed
 * @param confirmAction The action to execute when the action confirmed
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
    stepIcon: ImageVector,
    stepTitle: StringResource,
    stepContent: @Composable ColumnScope.() -> Unit,
    dismissAction: (() -> Unit)? = null,
    confirmAction: (MutableState<Boolean>) -> Unit,
    bottomDivider: Boolean = true
) {
    StepAction(
        shape = shape,
        stepIcon = stepIcon,
        actionText = stepTitle,
        actionContent = stepContent,
        controls = { expanded ->
            ActionControls(
                expanded = expanded,
                dismissAction = dismissAction,
                confirmAction = confirmAction
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
            Row{
                IconButton(
                    onClick = {
                        dismissAction?.invoke()
                        expanded.value = !expanded.value
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                }
                IconButton(
                    onClick = {
                        confirmAction.invoke(
                            expanded
                        )
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
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
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
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
 * @param stepIcon The representative leading icon
 * @param actionText The representative action text
 * @param actionContent The content to display to execute the action
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
    stepIcon: ImageVector,
    actionText: StringResource,
    actionContent: @Composable ColumnScope.() -> Unit,
    controls: @Composable (MutableState<Boolean>) -> Unit,
    bottomDivider: Boolean = true
) {
    Card (
        shape = shape
    ) {
        val expanded = rememberSaveable { mutableStateOf(false) }
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = stepIcon,
                contentDescription = null
            )
            Text(
                modifier = Modifier
                    .padding(
                        start = 10.dp
                    ),
                text = stringResource(actionText)
            )
            controls.invoke(expanded)
        }
        AnimatedVisibility(
            visible = expanded.value
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                HorizontalDivider()
                actionContent.invoke(this)
            }
        }
    }
    if(bottomDivider)
        HorizontalDivider()
}