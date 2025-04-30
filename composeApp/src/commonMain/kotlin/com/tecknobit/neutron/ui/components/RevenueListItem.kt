package com.tecknobit.neutron.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.tecknobit.neutron.ui.screens.revenues.components.RevenueLabels
import com.tecknobit.neutron.ui.screens.revenues.data.Revenue
import com.tecknobit.neutron.ui.screens.revenues.data.RevenueLabel
import com.tecknobit.neutron.ui.screens.shared.presentations.RevenueRelatedScreenViewModel

/**
 * Custom [ListItem] used to display and to manage a [revenue]
 *
 * @param viewModel The support viewmodel for the screen
 * @param revenue The revenue to display
 * @param labels The labels attached to the [revenue]
 * @param containerColor The color to apply to the container
 * @param overline The overline section of the component
 * @param info The info section of the component
 * @param allowEdit Whether the edit of the revenue is allowed
 * @param onEdit The action to execute when the user clicked the edit button
 * @param deleteIcon The icon of the delete button
 * @param actionButton The action button
 * @param deleteAlertDialog The related dialog used to warn about the [revenue] deletion
 */
@Composable
fun RevenueListItem(
    viewModel: RevenueRelatedScreenViewModel,
    revenue: Revenue,
    labels: List<RevenueLabel>,
    containerColor: Color,
    overline: @Composable (List<RevenueLabel>) -> Unit = { ticketLabel ->
        RevenueLabels(
            labels = ticketLabel
        )
    },
    info: @Composable () -> Unit = {
        RevenueInfo(
            viewModel = viewModel,
            revenue = revenue
        )
    },
    allowEdit: Boolean = true,
    onEdit: () -> Unit,
    deleteIcon: ImageVector,
    actionButton: @Composable () -> Unit,
    deleteAlertDialog: @Composable (MutableState<Boolean>) -> Unit
) {
    ListItem(
        colors = ListItemDefaults.colors(
            containerColor = containerColor
        ),
        overlineContent = { overline.invoke(labels) },
        headlineContent = info,
        trailingContent = {
            RevenueToolbar(
                allowEdit = allowEdit,
                onEdit = onEdit,
                deleteIcon = deleteIcon,
                actionButton = actionButton,
                deleteAlertDialog = { delete ->
                    deleteAlertDialog.invoke(delete)
                }
            )
        }
    )
}

/**
 * The toolbar of the [RevenueListItem] component
 *
 * @param allowEdit Whether the edit of the revenue is allowed
 * @param onEdit The action to execute when the user clicked the edit button
 * @param deleteIcon The icon of the delete button
 * @param actionButton The action button
 * @param deleteAlertDialog The related dialog used to warn about a revenue deletion
 */
@Composable
private fun RevenueToolbar(
    allowEdit: Boolean,
    onEdit: () -> Unit,
    deleteIcon: ImageVector,
    actionButton: @Composable (() -> Unit)? = null,
    deleteAlertDialog: @Composable (MutableState<Boolean>) -> Unit
) {
    Column (
        horizontalAlignment = Alignment.End
    ) {
        Row {
            AnimatedVisibility(
                visible = allowEdit
            ) {
                IconButton(
                    onClick = onEdit
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null
                    )
                }
            }
            val delete = remember { mutableStateOf(false) }
            IconButton(
                onClick = { delete.value = true }
            ) {
                Icon(
                    imageVector = deleteIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            }
            deleteAlertDialog.invoke(delete)
        }
        actionButton?.invoke()
    }
}