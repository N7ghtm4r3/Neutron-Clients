package com.tecknobit.neutron.ui.components.screenkeyboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.rememberSaveable
import com.tecknobit.neutron.ui.components.screenkeyboard.ScreenKeyboardState.Companion.ZERO

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

class ScreenKeyboardState internal constructor(
    var amount: MutableState<String>
) {

    internal companion object {

        const val DOT_CHARACTER = "."

        const val ZERO = "0"

    }

    fun removeLastDigit() {
        amount.value = amount.value.dropLast(1).ifEmpty { ZERO }
    }

    fun appendDigit(
        digit: Int
    ) {
        appendDigit(
            digit = digit.toString()
        )
    }

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

    fun currentAmount() : String {
        return amount.value
    }

    fun parseAmount() : Double {
        return amount.value.toDouble()
    }

}

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