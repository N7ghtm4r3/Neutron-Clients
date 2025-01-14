package com.tecknobit.neutron.ui.screens.revenues.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.ChameleonText
import com.tecknobit.equinoxcompose.utilities.toColor
import com.tecknobit.neutron.ui.screens.revenues.data.RevenueLabel

@Composable
@NonRestartableComposable
fun RevenueLabel(
    modifier: Modifier = Modifier,
    fontSize: TextUnit = TextUnit.Unspecified,
    label: RevenueLabel
) {
    val color = label.color.toColor()
    Card (
        modifier = modifier,
        colors = CardDefaults.outlinedCardColors(
            containerColor = color
        ),
        shape = RoundedCornerShape(
            size = 5.dp
        )
    ) {
        ChameleonText(
            modifier = Modifier
                .padding(
                    all = 4.dp
                ),
            backgroundColor = color,
            fontSize = fontSize,
            text = label.text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}