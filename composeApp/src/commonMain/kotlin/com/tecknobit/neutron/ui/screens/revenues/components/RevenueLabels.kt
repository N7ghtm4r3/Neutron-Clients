@file:OptIn(ExperimentalFoundationApi::class)

package com.tecknobit.neutron.ui.screens.revenues.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.ChameleonText
import com.tecknobit.equinoxcompose.utilities.toColor
import com.tecknobit.neutron.ui.screens.shared.data.RevenueLabel

/**
 * Component used to display the labels attached to a revenue
 *
 * @param contentPadding The padding to apply to the component
 * @param labels The labels attached to the revenue
 * @param trailingIcon The trailing icon section
 * @param stickyHeaderContent The content of the sticky header
 */
@Composable
@NonRestartableComposable
fun RevenueLabels(
    contentPadding: PaddingValues = PaddingValues(
        all = 5.dp
    ),
    labels: List<RevenueLabel>,
    trailingIcon: @Composable ((RevenueLabel) -> Unit)? = null,
    stickyHeaderContent: @Composable (LazyItemScope.() -> Unit)? = null
) {
    LazyRow (
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        stickyHeaderContent?.let {
            stickyHeader {
                stickyHeaderContent.invoke(this)
            }
        }
        items(
            items = labels,
            key = { label -> label.id }
        ) { label ->
            RevenueLabelBadge(
                label = label,
                trailingIcon = trailingIcon
            )
        }
    }
}

/**
 * Badge used to display a [RevenueLabel] information
 *
 * @param modifier The modifier to apply to the component
 * @param fontSize The size of the font
 * @param trailingIcon The trailing icon section
 * @param label The label to display
 */
@Composable
@NonRestartableComposable
private fun RevenueLabelBadge(
    modifier: Modifier = Modifier,
    fontSize: TextUnit = TextUnit.Unspecified,
    trailingIcon: @Composable ((RevenueLabel) -> Unit)? = null,
    label: RevenueLabel
) {
    val color = label.color.toColor()
    Card (
        modifier = modifier,
        colors = CardDefaults.outlinedCardColors(
            containerColor = color
        ),
        shape = RoundedCornerShape(
            size = 10.dp
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ChameleonText(
                modifier = Modifier
                    .padding(
                        all = 4.dp
                    )
                    .widthIn(
                        max = 100.dp
                    ),
                backgroundColor = color,
                fontSize = fontSize,
                text = label.text,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            trailingIcon?.invoke(label)
        }
    }
}