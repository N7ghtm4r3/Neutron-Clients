package com.tecknobit.neutron.ui.screens.profile.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.viewmodels.EquinoxProfileViewModel
import com.tecknobit.equinoxcore.network.sendRequest
import com.tecknobit.neutron.localUser
import com.tecknobit.neutron.requester
import com.tecknobit.neutroncore.enums.NeutronCurrency
import kotlinx.coroutines.launch

/**
 * The `ProfileScreenViewModel` class is the support class used to change the user account settings
 * or preferences
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see androidx.lifecycle.ViewModel
 * @see com.tecknobit.equinoxcompose.session.Retriever
 * @see com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
 * @see EquinoxProfileViewModel
 */
class ProfileScreenViewModel : EquinoxProfileViewModel(
    snackbarHostState = SnackbarHostState(),
    requester = requester,
    localUser = localUser
) {

    /**
     * `profilePic` -> the profile picture of the user
     */
    lateinit var currency: MutableState<NeutronCurrency>

    /**
     * Method to execute the currency change
     *
     * @param onChange The callback action to invoke after currency changed
     */
    fun changeCurrency(
        onChange: () -> Unit,
    ) {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    changeCurrency(
                        newCurrency = currency.value
                    )
                },
                onSuccess = {
                    localUser.currency = currency.value
                    onChange.invoke()
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

}