package com.tecknobit.neutron.ui.components.screenkeyboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.rememberSaveable
import com.tecknobit.neutron.ui.components.screenkeyboard.ScreenKeyboardState.Companion.ZERO

/**
 * Method to remember the [ScreenKeyboardState] during the recompositions
 *
 * @param amount The initial amount value to register
 *
 * @return the state as [ScreenKeyboardState]
 */
@Composable
fun rememberKeyboardState(
    amount: String = ZERO
) : ScreenKeyboardState {
    val amountState = remember { mutableStateOf(amount) }
    val keyboardSaver = rememberSaveable(
        stateSaver = ScreenKeyboardSaver
    ) {
        mutableStateOf(
            ScreenKeyboardState(
                amount = amountState
            )
        )
    }
    return keyboardSaver.value
}

/**
 * The `ScreenKeyboardState` is used to register the [ScreenKeyboard]'s event and assemble the total
 * amount value inserted
 *
 * @property amount The current amount value inserted with the [ScreenKeyboard]
 *
 * @author N7ghtm4r3 - Tecknobit
 */
class ScreenKeyboardState internal constructor(
    var amount: MutableState<String>
) {

    internal companion object {

        /**
         * `DOT_CHARACTER` constant value represents the `.` character
         */
        const val DOT_CHARACTER = "."

        /**
         * `ZERO` constant value represents the `0` character
         */
        const val ZERO = "0"

    }

    /**
     * Method to remove the last digit registered by the state and inserted in the [amount]
     */
    fun removeLastDigit() {
        amount.value = amount.value.dropLast(1).ifEmpty { ZERO }
    }

    /**
     * Method to append a new [Int] digit in the total [amount]
     */
    fun appendDigit(
        digit: Int
    ) {
        appendDigit(
            digit = digit.toString()
        )
    }

    /**
     * Method to append a new [String] digit in the total [amount]. This method handles the [DOT_CHARACTER]
     * insertion also
     */
    fun appendDigit(
        digit: String
    ) {
        if(amount.value == ZERO && digit != DOT_CHARACTER)
            amount.value = digit
        else if(digit == DOT_CHARACTER) {
            if(!amount.value.contains(DOT_CHARACTER))
                amount.value += digit
        } else
            amount.value += digit
    }

    /**
     * Method to retrieve the current amount registered by the state
     *
     * @return the current amount as [String]
     */
    fun currentAmount() : String {
        return amount.value
    }

    /**
     * Method to parse the current amount registered by the state
     *
     * @return the current amount as [Double]
     */
    fun parseAmount() : Double {
        return amount.value.toDouble()
    }

}

/**
 * The `ScreenKeyboardSaver` is used to save and restore the [ScreenKeyboardState] value during the
 * recomposition to avoid to lose the current values calculated and registered
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 * @see Saver
 */
internal object ScreenKeyboardSaver : Saver<ScreenKeyboardState, MutableState<String>> {

    /**
     * Convert the restored value back to the original Class. If null is returned the value will
     * not be restored and would be initialized again instead
     */
    override fun restore(
        value: MutableState<String>
    ): ScreenKeyboardState {
        return ScreenKeyboardState(
            amount = value
        )
    }

    /**
     * Convert the value into a saveable one. If null is returned the value will not be saved.
     */
    override fun SaverScope.save(
        value: ScreenKeyboardState
    ): MutableState<String> {
        return value.amount
    }

}