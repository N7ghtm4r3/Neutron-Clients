package com.tecknobit.neutron.ui.helpers

import com.tecknobit.equinoxcompose.network.EquinoxRequester

class NeutronRequester(
    host: String,
    userId: String?,
    userToken: String?,
    debugMode: Boolean = false
) : EquinoxRequester(
    host = host,
    userId = userId,
    userToken = userToken,
    debugMode = debugMode,
    connectionErrorMessage = "", // TODO: TO SET
    byPassSSLValidation = true
) {


}