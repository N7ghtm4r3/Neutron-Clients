@file:OptIn(ExperimentalMultiplatform::class)

package com.tecknobit.neutron.ui.screens.insert.presenter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import neutron.composeapp.generated.resources.go_back
import neutron.composeapp.generated.resources.next
import org.jetbrains.compose.resources.stringResource

class InsertRevenueScreen : NeutronScreen<InsertRevenueScreenViewModel>(
    viewModel = InsertRevenueScreenViewModel(),
    title = Res.string.add_revenue
) {

    private lateinit var keyboardState: ScreenKeyboardState

    private lateinit var displayKeyboard: MutableState<Boolean>

    @Composable
    override fun ScreenContent() {
        ResponsiveContent(
            onExpandedSizeClass = {
                AmountSection(
                    keyboardModifier = Modifier
                        .clip(
                            RoundedCornerShape(
                                topStart = 21.dp,
                                topEnd = 21.dp
                            )
                        )
                )
            },
            onMediumSizeClass = {
                AmountSection(
                    keyboardWeight = 1.5f
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
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Amount()
                AnimatedVisibility(
                    visible = keyboardState.parseAmount() > 0.0
                ) {
                    TextButton(
                        onClick = { displayKeyboard.value = !displayKeyboard.value }
                    ) {
                        Text(
                            text = stringResource(
                                if(displayKeyboard.value)
                                    Res.string.next
                                else
                                    Res.string.go_back
                            )
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .weight(keyboardWeight)
            ) {
                AnimatedVisibility(
                    visible = displayKeyboard.value
                ) {
                    ScreenKeyboard(
                        modifier = keyboardModifier
                            .fillMaxSize(),
                        state = keyboardState
                    )
                }
            }
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
        displayKeyboard = remember { mutableStateOf(true) }
    }

}