package com.tecknobit.neutron.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.neutron.ui.icons.ArrowSelectorTool
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * Custom [FilterChip] used to select a specific [category] of items to select
 *
 * @param category The category title
 * @param onClick The action to execute when the chip has been selected
 */
@Composable
fun CategoryChip(
    category: StringResource,
    onClick: (Boolean) -> Unit,
) {
    var retrieve by remember { mutableStateOf(true) }
    val contentColor = contentColorFor(MaterialTheme.colorScheme.primary)
    FilterChip(
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor = contentColor,
            selectedTrailingIconColor = contentColor
        ),
        selected = retrieve,
        label = {
            Text(
                text = stringResource(category)
            )
        },
        onClick = {
            retrieve = !retrieve
            onClick(retrieve)
        },
        trailingIcon = {
            Icon(
                modifier = Modifier
                    .size(20.dp),
                imageVector = if(retrieve)
                    Icons.Default.CheckCircle
                else
                    ArrowSelectorTool,
                contentDescription = null
            )
        }
    )
}