@file:OptIn(ExperimentalMultiplatform::class)

package com.tecknobit.neutron.ui.screens.insert.shared.presenter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
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
import com.tecknobit.equinoxcompose.session.ManagedContent
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.neutron.bodyFontFamily
import com.tecknobit.neutron.displayFontFamily
import com.tecknobit.neutron.localUser
import com.tecknobit.neutron.ui.components.Step
import com.tecknobit.neutron.ui.components.Stepper
import com.tecknobit.neutron.ui.components.screenkeyboard.ScreenKeyboard
import com.tecknobit.neutron.ui.components.screenkeyboard.ScreenKeyboardState.Companion.ZERO
import com.tecknobit.neutron.ui.components.screenkeyboard.rememberKeyboardState
import com.tecknobit.neutron.ui.screens.insert.shared.presentation.InsertScreenViewModel
import com.tecknobit.neutron.ui.screens.revenues.data.GeneralRevenue
import com.tecknobit.neutron.ui.screens.revenues.data.Revenue
import com.tecknobit.neutron.ui.screens.shared.presenters.NeutronScreen
import com.tecknobit.neutroncore.helpers.NeutronInputsValidator.isRevenueDescriptionValid
import com.tecknobit.neutroncore.helpers.NeutronInputsValidator.isRevenueTitleValid
import dev.darkokoa.datetimewheelpicker.WheelDateTimePicker
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import neutron.composeapp.generated.resources.Res
import neutron.composeapp.generated.resources.description
import neutron.composeapp.generated.resources.description_not_valid
import neutron.composeapp.generated.resources.edit
import neutron.composeapp.generated.resources.go_back
import neutron.composeapp.generated.resources.insert
import neutron.composeapp.generated.resources.insertion_date
import neutron.composeapp.generated.resources.next
import neutron.composeapp.generated.resources.title
import neutron.composeapp.generated.resources.title_not_valid
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Structure
abstract class InsertScreen<V : InsertScreenViewModel>(
    revenueId: String?,
    addingTitle: StringResource,
    editingTitle: StringResource,
    viewModel: V
) : NeutronScreen<V>(
    title = if(revenueId != null)
        editingTitle
    else
        addingTitle,
    viewModel = viewModel
) {

    protected val titleStep by lazy {
        Step(
            stepIcon = Icons.Default.Title,
            title = Res.string.title,
            content = { RevenueTitle() },
            isError = viewModel.titleError,
            dismissAction = { viewModel.title.value = "" }
        )
    }

    protected val descriptionStep by lazy {
        Step(
            enabled = viewModel.addingGeneralRevenue,
            stepIcon = Icons.Default.Description,
            title = Res.string.description,
            content = { RevenueDescription() },
            isError = viewModel.descriptionError,
            dismissAction = { viewModel.description.value = "" }
        )
    }

    protected val insertionDateStep by lazy {
        Step(
            stepIcon = Icons.Default.EditCalendar,
            title = Res.string.insertion_date,
            content = { InsertionDate() }
        )
    }

    protected lateinit var displayKeyboard: MutableState<Boolean>

    protected lateinit var revenue: State<Revenue?>

    protected val isEditing: Boolean = revenueId != null

    @Composable
    override fun ScreenContent() {
        ManagedContent(
            viewModel = viewModel!!,
            content = {
                CollectStatesAfterLoading()
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
            },
            loadingRoutine = {
                if(isEditing)
                    revenue.value != null
                else
                    true
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
                    visible = viewModel!!.keyboardState.parseAmount() > 0.0
                ) {
                    Row (
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
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
                        AnimatedVisibility(
                            visible = !displayKeyboard.value
                        ) {
                            TextButton(
                                colors = ButtonDefaults.textButtonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                ),
                                onClick = { viewModel!!.insert() }
                            ) {
                                Text(
                                    text = stringResource(
                                        if(isEditing)
                                            Res.string.edit
                                        else
                                            Res.string.insert
                                    )
                                )
                            }
                        }
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
                text = viewModel!!.keyboardState.currentAmount(),
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
                state = viewModel!!.keyboardState
            )
        }
    }

    @Composable
    @NonRestartableComposable
    private fun FormSection() {
        AnimatedVisibility(
            visible = !displayKeyboard.value
        ) {
            val steps = getInsertionSteps()
            Stepper(
                modifier = Modifier
                    .padding(
                        horizontal = 16.dp
                    )
                    .padding(
                        bottom = 10.dp
                    ),
                steps = steps
            )
        }
    }

    @Composable
    protected abstract fun getInsertionSteps() : Array<out Step>

    @Composable
    @NonRestartableComposable
    // TODO: ANNOTATE AS SPECIAL STEP WITH THE RELATED EQUINOX-ANNOTATION
    protected fun RevenueTitle() {
        val focusRequester = remember { FocusRequester() }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
        EquinoxTextField(
            modifier = Modifier
                .padding(
                    bottom = 10.dp
                )
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
            maxLines = 1,
            placeholder = Res.string.title,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            )
        )
    }

    @Composable
    @NonRestartableComposable
    // TODO: ANNOTATE AS SPECIAL STEP WITH THE RELATED EQUINOX-ANNOTATION
    protected fun RevenueDescription() {
        val focusRequester = remember { FocusRequester() }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
        EquinoxTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    bottom = 10.dp
                )
                .focusRequester(focusRequester),
            textFieldColors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent
            ),
            maxLines = 10,
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
                imeAction = ImeAction.Done
            )
        )
    }

    @Composable
    @NonRestartableComposable
    // TODO: ANNOTATE AS SPECIAL STEP WITH THE RELATED EQUINOX-ANNOTATION
    protected fun ColumnScope.InsertionDate() {
        WheelDateTimePicker(
            modifier = Modifier
                .padding(
                    vertical = 16.dp
                )
                .align(Alignment.CenterHorizontally),
            startDateTime = viewModel!!.insertionDate.value,
            rowCount = 5
        ) { snappedDateTime -> viewModel!!.insertionDate.value = snappedDateTime }
    }

    override fun onStart() {
        super.onStart()
        viewModel!!.retrieveRevenue()
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    @RequiresSuperCall
    override fun CollectStates() {
        displayKeyboard = remember { mutableStateOf(true) }
        revenue = viewModel!!.revenue.collectAsState()
        viewModel!!.titleError = remember { mutableStateOf(false) }
        viewModel!!.descriptionError = remember { mutableStateOf(false) }
    }

    @Composable
    @NonRestartableComposable
    private fun CollectStatesAfterLoading() {
        viewModel!!.keyboardState = rememberKeyboardState(
            amount = if(isEditing)
                revenue.value!!.value.toString()
            else
                ZERO
        )
        viewModel!!.addingGeneralRevenue = remember {
            mutableStateOf(!isEditing || revenue.value!! is GeneralRevenue)
        }
        viewModel!!.title = remember {
            mutableStateOf(
                if(isEditing)
                    revenue.value!!.title
                else
                    ""
            )
        }
        viewModel!!.description = remember {
            mutableStateOf(
                if(revenue.value != null && revenue.value is GeneralRevenue)
                    (revenue.value as GeneralRevenue).description
                else
                    ""
            )
        }
        viewModel!!.insertionDate = remember {
            mutableStateOf(
                if(isEditing) {
                    Instant.fromEpochMilliseconds(revenue.value!!.revenueDate)
                        .toLocalDateTime(TimeZone.currentSystemDefault())
                } else
                    Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            )
        }
    }

}