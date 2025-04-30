@file:OptIn(ExperimentalComposeApi::class)

package com.tecknobit.neutron.ui.components.screenkeyboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.utilities.EXPANDED_CONTAINER
import com.tecknobit.neutron.displayFontFamily
import com.tecknobit.neutron.ui.components.screenkeyboard.ScreenKeyboardState.Companion.DOT_CHARACTER

/**
 * `buttons` the buttons list displayed in the keyboard, includes also the special buttons as backspace
 * and dot separator
 */
private val buttons = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "backspace", "0", ".")

/**
 * `BACKSPACE_INDEX` constant value to indicate the index of the [Backspace] button
 */
private const val BACKSPACE_INDEX = 9

/**
 * Custom keyboard displayed as component on screen
 *
 * @param modifier The modifier to apply to the component
 * @param state The state manager of the component
 */
@Composable
@NonRestartableComposable
fun ScreenKeyboard(
    modifier: Modifier = Modifier,
    state: ScreenKeyboardState
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    LazyVerticalGrid(
        modifier = modifier
            .background(MaterialTheme.colorScheme.primary)
            .focusRequester(focusRequester)
            .attachHardwareKeyboardEvents(
                state = state
            )
            .widthIn(
                max = EXPANDED_CONTAINER
            ),
        contentPadding = PaddingValues(
            all = 5.dp
        ),
        horizontalArrangement = Arrangement.Center,
        verticalArrangement = Arrangement.SpaceAround,
        columns = GridCells.Fixed(3),
    ) {
        itemsIndexed(
            items = buttons,
            key = { _, it -> it }
        ) { index, digit ->
            if(index == BACKSPACE_INDEX) {
                Backspace(
                    state = state
                )
            } else {
                DigitButton(
                    state = state,
                    digit = digit
                )
            }
        }
    }
}

/**
 * Method to handle the hardware keyboard inputs and register them with the [state]
 *
 * @param state The state manager of the [ScreenKeyboard] component
 */
private inline fun Modifier.attachHardwareKeyboardEvents(
    state: ScreenKeyboardState
) : Modifier {
    return onKeyEvent { event ->
        return@onKeyEvent if(event.type == KeyEventType.KeyDown) {
            when(event.key) {
                Key.Zero, Key.NumPad0 -> {
                    state.appendDigit(
                        digit = 0
                    )
                    true
                }
                Key.One, Key.NumPad1 -> {
                    state.appendDigit(
                        digit = 1
                    )
                    true
                }
                Key.Two, Key.NumPad2 -> {
                    state.appendDigit(
                        digit = 2
                    )
                    true
                }
                Key.Three, Key.NumPad3 -> {
                    state.appendDigit(
                        digit = 3
                    )
                    true
                }
                Key.Four, Key.NumPad4 -> {
                    state.appendDigit(
                        digit = 4
                    )
                    true
                }
                Key.Five, Key.NumPad5 -> {
                    state.appendDigit(
                        digit = 5
                    )
                    true
                }
                Key.Six, Key.NumPad6 -> {
                    state.appendDigit(
                        digit = 6
                    )
                    true
                }
                Key.Seven, Key.NumPad7 -> {
                    state.appendDigit(
                        digit = 7
                    )
                    true
                }
                Key.Eight, Key.NumPad8 -> {
                    state.appendDigit(
                        digit = 8
                    )
                    true
                }
                Key.Nine, Key.NumPad9 -> {
                    state.appendDigit(
                        digit = 9
                    )
                    true
                }
                Key.Backspace -> {
                    state.removeLastDigit()
                    true
                }
                Key.Period, Key.NumPadDot -> {
                    state.appendDigit(
                        digit = DOT_CHARACTER
                    )
                    true
                }
                else -> false
            }
        } else
            false
    }
}

/**
 * Custom button used to remove the last character inserted in the [state]
 *
 * @param state The state manager of the [ScreenKeyboard] component
 */
@Composable
@NonRestartableComposable
private fun Backspace(
    state: ScreenKeyboardState
) {
    Button(
        modifier = Modifier
            .size(70.dp)
            .clip(CircleShape),
        onClick = { state.removeLastDigit() }
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.Backspace,
            contentDescription = null
        )
    }
}

/**
 * Custom button used to insert the amount value (and the dot separator) in the  [state]
 *
 * @param state The state manager of the [ScreenKeyboard] component
 * @param digit The value of the digit managed by the button
 */
@Composable
@NonRestartableComposable
private fun DigitButton(
    state: ScreenKeyboardState,
    digit: String
) {
    Button(
        onClick = {
            state.appendDigit(
                digit = digit
            )
        },
        shape = CircleShape
    ) {
        Text(
            text = digit,
            fontFamily = displayFontFamily,
            fontSize = 40.sp
        )
    }
}