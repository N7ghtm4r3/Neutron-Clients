package com.tecknobit.neutron.helpers

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO

/**
 * Method to create a custom [HttpClient] for the [com.tecknobit.neutron.imageLoader] instance
 */
actual fun customHttpClient(): HttpClient {
    return HttpClient(CIO) {
        engine {
            https {
                serverName = null
            }
        }
    }
}