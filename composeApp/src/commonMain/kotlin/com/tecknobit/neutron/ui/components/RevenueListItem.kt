package com.tecknobit.neutron.ui.components

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
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.tecknobit.neutron.ui.screens.revenues.components.RevenueLabels
import com.tecknobit.neutron.ui.screens.revenues.data.Revenue
import com.tecknobit.neutron.ui.screens.revenues.data.RevenueLabel

@Composable
@NonRestartableComposable
fun RevenueListItem(
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
            revenue = revenue
        )
    },
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

@Composable
@NonRestartableComposable
private fun RevenueToolbar(
    onEdit: () -> Unit,
    deleteIcon: ImageVector,
    actionButton: @Composable (() -> Unit)? = null,
    deleteAlertDialog: @Composable (MutableState<Boolean>) -> Unit
) {
    Column (
        horizontalAlignment = Alignment.End
    ) {
        Row {
            IconButton(
                onClick = onEdit
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null
                )
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