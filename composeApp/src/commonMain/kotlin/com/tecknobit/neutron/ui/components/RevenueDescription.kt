package com.tecknobit.neutron.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tecknobit.neutron.bodyFontFamily
import com.tecknobit.neutron.ui.screens.revenues.data.GeneralRevenue

/**
 * Used to display the description of a [revenue]
 *
 * @param expanded Whether the description is visible
 * @param revenue The revenue from retrieve the description to display
 */
@Composable
fun RevenueDescription(
    expanded: Boolean,
    revenue: GeneralRevenue,
) {
    AnimatedVisibility(
        visible = expanded
    ) {
        Text(
            modifier = Modifier
                .padding(
                    horizontal = 16.dp
                )
                .padding(
                    bottom = 16.dp
                ),
            text = revenue.description,
            textAlign = TextAlign.Justify,
            overflow = TextOverflow.Ellipsis,
            fontFamily = bodyFontFamily
        )
    }
}