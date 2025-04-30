package com.tecknobit.neutron.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.LabelOff
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.components.EmptyListUI
import com.tecknobit.equinoxcompose.components.UIAnimations
import com.tecknobit.equinoxcompose.components.getContrastColor
import com.tecknobit.equinoxcompose.utilities.toColor
import com.tecknobit.neutron.ui.screens.revenues.data.RevenueLabel
import com.tecknobit.neutron.ui.screens.shared.presentations.RevenueLabelsRetriever
import neutron.composeapp.generated.resources.Res
import neutron.composeapp.generated.resources.no_labels_available

/**
 * Grid used to display a list of [labels]
 *
 * @param modifier The modifier to apply to the component
 * @param labelsRetriever The retriever used to retrieve the labels to display
 * @param labels The support labels list used to add or remove the labels selected or unselected
 */
@Composable
fun LabelsGrid(
    modifier: Modifier = Modifier,
    labelsRetriever: RevenueLabelsRetriever,
    labels: SnapshotStateList<RevenueLabel>,
) {
    val currentLabels = remember { mutableStateListOf<RevenueLabel>() }
    LaunchedEffect(Unit) {
        labelsRetriever.retrieveUserLabels(
            labels = currentLabels
        )
    }
    AnimatedVisibility(
        visible = currentLabels.isNotEmpty(),
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        LazyVerticalGrid(
            modifier = modifier,
            columns = GridCells.Fixed(4),
            contentPadding = PaddingValues(
                all = 5.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(
                items = currentLabels,
                key = { label -> label.id }
            ) { label ->
                var selected by remember { mutableStateOf(labels.any { it.id == label.id }) }
                val color = label.color.toColor()
                val containerColor = if (selected)
                    color
                else
                    Color.Unspecified
                OutlinedCard(
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = containerColor
                    ),
                    border = BorderStroke(
                        width = 1.5.dp,
                        color = color
                    ),
                    onClick = {
                        selected = !selected
                        if (selected)
                            labels.add(label)
                        else
                            labels.remove(label)
                    }
                ) {
                    Text(
                        modifier = Modifier
                            .padding(
                                all = 5.dp
                            ),
                        text = label.text,
                        color = if (selected) {
                            getContrastColor(
                                backgroundColor = containerColor
                            )
                        } else
                            color,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
    EmptyListUI(
        animations = UIAnimations(
            visible = currentLabels.isEmpty(),
            onEnter = fadeIn(),
            onExit = fadeOut()
        ),
        icon = Icons.AutoMirrored.Filled.LabelOff,
        subText = Res.string.no_labels_available
    )
}