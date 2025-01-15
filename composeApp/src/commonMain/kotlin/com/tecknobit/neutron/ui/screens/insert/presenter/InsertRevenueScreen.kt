@file:OptIn(ExperimentalMultiplatform::class)

package com.tecknobit.neutron.ui.screens.insert.presenter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.components.EquinoxTextField
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import com.tecknobit.neutron.bodyFontFamily
import com.tecknobit.neutron.displayFontFamily
import com.tecknobit.neutron.localUser
import com.tecknobit.neutron.ui.components.Step
import com.tecknobit.neutron.ui.components.Stepper
import com.tecknobit.neutron.ui.components.screenkeyboard.ScreenKeyboard
import com.tecknobit.neutron.ui.components.screenkeyboard.ScreenKeyboardState
import com.tecknobit.neutron.ui.components.screenkeyboard.rememberKeyboardState
import com.tecknobit.neutron.ui.screens.NeutronScreen
import com.tecknobit.neutron.ui.screens.insert.presentation.InsertRevenueScreenViewModel
import com.tecknobit.neutroncore.helpers.NeutronInputsValidator.isRevenueDescriptionValid
import com.tecknobit.neutroncore.helpers.NeutronInputsValidator.isRevenueTitleValid
import neutron.composeapp.generated.resources.Res
import neutron.composeapp.generated.resources.add_revenue
import neutron.composeapp.generated.resources.description
import neutron.composeapp.generated.resources.description_not_valid
import neutron.composeapp.generated.resources.generals
import neutron.composeapp.generated.resources.go_back
import neutron.composeapp.generated.resources.next
import neutron.composeapp.generated.resources.project
import neutron.composeapp.generated.resources.revenue_type
import neutron.composeapp.generated.resources.title
import neutron.composeapp.generated.resources.title_not_valid
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
                    keyboardWeight = 2f
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
        keyboardWeight: Float = 2f,
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
                KeyboardSection(
                    keyboardModifier = keyboardModifier
                )
                FormSection()
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

    @Composable
    @NonRestartableComposable
    private fun KeyboardSection(
        keyboardModifier: Modifier
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

    @Composable
    @NonRestartableComposable
    private fun FormSection() {
        AnimatedVisibility(
            visible = !displayKeyboard.value
        ) {
            Stepper(
                steps = arrayOf(
                    Step(
                        stepIcon = Icons.Default.SelectAll,
                        title = Res.string.revenue_type,
                        content = { RevenueType() },
                        confirmAction = {
                        }
                    ),
                    Step(
                        stepIcon = Icons.Default.Title,
                        title = Res.string.title,
                        content = { RevenueTitle() },
                        confirmAction = {
                        }
                    ),
                    Step(
                        enabled = viewModel!!.addingGeneralRevenue,
                        stepIcon = Icons.Default.Description,
                        title = Res.string.description,
                        content = { RevenueDescription() },
                        confirmAction = {
                        }
                    )
                )
            )
        }
    }

    @Composable
    @NonRestartableComposable
    // TODO: ANNOTATE AS SPECIAL STEP WITH THE RELATED EQUINOX-ANNOTATION
    private fun RevenueType() {
        Row (
            modifier = Modifier
                .padding(
                    start = 4.dp
                )
                .fillMaxWidth()
                .selectableGroup(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = viewModel!!.addingGeneralRevenue.value,
                    onClick = {
                        if(!viewModel!!.addingGeneralRevenue.value)
                            viewModel!!.addingGeneralRevenue.value = true
                    }
                )
                Text(
                    text = stringResource(Res.string.generals)
                )
            }
            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = !viewModel!!.addingGeneralRevenue.value,
                    onClick = {
                        if(viewModel!!.addingGeneralRevenue.value)
                            viewModel!!.addingGeneralRevenue.value = false
                    }
                )
                Text(
                    text = stringResource(Res.string.project)
                )
            }
        }
    }

    @Composable
    @NonRestartableComposable
    // TODO: ANNOTATE AS SPECIAL STEP WITH THE RELATED EQUINOX-ANNOTATION
    private fun RevenueTitle() {
        val focusRequester = remember { FocusRequester() }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
        viewModel!!.title = remember { mutableStateOf("") }
        viewModel!!.titleError = remember { mutableStateOf(false) }
        EquinoxTextField(
            modifier = Modifier
                .focusRequester(focusRequester),
            textFieldColors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent
            ),
            value = viewModel!!.title,
            textFieldStyle = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = bodyFontFamily
            ),
            isError = viewModel!!.titleError,
            validator = { isRevenueTitleValid(it) },
            errorText = Res.string.title_not_valid,
            errorTextStyle = TextStyle(
                fontSize = 14.sp,
                fontFamily = bodyFontFamily
            ),
            placeholder = Res.string.title,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            )
        )
    }

    @Composable
    @NonRestartableComposable
    // TODO: ANNOTATE AS SPECIAL STEP WITH THE RELATED EQUINOX-ANNOTATION
    private fun RevenueDescription() {
        val focusRequester = remember { FocusRequester() }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
        viewModel!!.description = remember { mutableStateOf("") }
        viewModel!!.descriptionError = remember { mutableStateOf(false) }
        EquinoxTextField(
            modifier = Modifier
                .focusRequester(focusRequester),
            textFieldColors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent
            ),
            value = viewModel!!.description,
            textFieldStyle = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = bodyFontFamily
            ),
            isError = viewModel!!.descriptionError,
            validator = { isRevenueDescriptionValid(it) },
            errorText = Res.string.description_not_valid,
            errorTextStyle = TextStyle(
                fontSize = 14.sp,
                fontFamily = bodyFontFamily
            ),
            placeholder = Res.string.description,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            )
        )
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
        keyboardState = rememberKeyboardState()
        displayKeyboard = remember { mutableStateOf(true) }
        viewModel!!.addingGeneralRevenue = remember { mutableStateOf(true) }
    }

}