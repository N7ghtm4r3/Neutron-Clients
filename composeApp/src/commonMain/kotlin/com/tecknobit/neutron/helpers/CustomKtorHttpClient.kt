package com.tecknobit.neutron.helpers

import io.ktor.client.HttpClient

/**
 * Method to create a custom [HttpClient] for the [com.tecknobit.neutron.imageLoader] instance
 */
expect fun customHttpClient() : HttpClient