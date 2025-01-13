package com.tecknobit.neutron.ui.components

import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import com.tecknobit.neutron.displayFontFamily
import com.tecknobit.neutron.ui.screens.revenues.data.Revenue
import com.tecknobit.neutron.ui.screens.revenues.presentation.RevenuesScreenViewModel
import neutron.composeapp.generated.resources.Res
import neutron.composeapp.generated.resources.delete_revenue
import neutron.composeapp.generated.resources.delete_revenue_text

@Composable
@NonRestartableComposable
fun DeleteRevenue(
    show: MutableState<Boolean>,
    viewModel: RevenuesScreenViewModel,
    revenue: Revenue
) {
    EquinoxAlertDialog(
        modifier = Modifier
            .widthIn(
                max = 400.dp
            ),
        show = show,
        viewModel = viewModel,
        title = Res.string.delete_revenue,
        titleStyle = TextStyle(
            fontFamily = displayFontFamily,
            fontSize = 20.sp
        ),
        text = Res.string.delete_revenue_text,
        confirmAction = {
            viewModel.deleteRevenue(
                revenue = revenue
            )
        }
    )
}