@file:OptIn(ExperimentalMultiplatform::class)

package com.tecknobit.neutron.ui.screens.insert.presenter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import com.tecknobit.neutron.displayFontFamily
import com.tecknobit.neutron.localUser
import com.tecknobit.neutron.ui.components.screenkeyboard.ScreenKeyboard
import com.tecknobit.neutron.ui.components.screenkeyboard.ScreenKeyboardState
import com.tecknobit.neutron.ui.components.screenkeyboard.rememberKeyboardState
import com.tecknobit.neutron.ui.screens.NeutronScreen
import com.tecknobit.neutron.ui.screens.insert.presentation.InsertRevenueScreenViewModel
import neutron.composeapp.generated.resources.Res
import neutron.composeapp.generated.resources.add_revenue

class InsertRevenueScreen : NeutronScreen<InsertRevenueScreenViewModel>(
    viewModel = InsertRevenueScreenViewModel(),
    title = Res.string.add_revenue
) {

    private lateinit var keyboardState: ScreenKeyboardState

    @Composable
    override fun ScreenContent() {
        ResponsiveContent(
            onExpandedSizeClass = {
                AmountSection(
                    keyboardModifier = Modifier
                        .clip(
                            RoundedCornerShape(
                                topStart = 15.dp,
                                topEnd = 15.dp
                            )
                        )
                )
            },
            onMediumSizeClass = {
                AmountSection(
                    keyboardWeight = 4f
                )
            },
            onCompactSizeClass = {
                AmountSection()
            }
        )
    }

    @Composable
    @NonRestartableComposable
    private fun AmountSection(
        keyboardWeight: Float = 1f,
        keyboardModifier: Modifier = Modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Column (
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Amount()
            }
            ScreenKeyboard(
                modifier = keyboardModifier
                    .weight(keyboardWeight),
                state = keyboardState
            )
        }
    }

    @Composable
    @NonRestartableComposable
    private fun Amount(
        modifier: Modifier = Modifier
    ) {
        Row (
            modifier = modifier
                .padding(
                    horizontal = 16.dp
                ),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = keyboardState.currentAmount(),
                fontFamily = displayFontFamily,
                fontSize = 60.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = localUser.currency.symbol,
                fontFamily = displayFontFamily,
                fontSize = 35.sp
            )
        }
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
        keyboardState = rememberKeyboardState()
    }

}