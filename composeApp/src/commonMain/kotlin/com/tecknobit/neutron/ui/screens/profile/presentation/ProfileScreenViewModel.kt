package com.tecknobit.neutron.ui.screens.profile.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import com.tecknobit.equinoxcompose.viewmodels.EquinoxProfileViewModel
import com.tecknobit.neutron.localUser
import com.tecknobit.neutron.requester
import com.tecknobit.neutroncore.enums.NeutronCurrency

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
     * `newEmailError` -> whether the [currency] field is not valid
     */
    lateinit var currencyError: MutableState<Boolean>

}