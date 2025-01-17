package com.tecknobit.neutron.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.neutron.ui.icons.ArrowSelectorTool
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
@NonRestartableComposable
fun CategoryChip(
    type: StringResource,
    onClick: (Boolean) -> Unit
) {
    var retrieve by remember { mutableStateOf(true) }
    FilterChip(
        selected = retrieve,
        label = {
            Text(
                text = stringResource(type)
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