package com.tecknobit.neutron.helpers

import com.tecknobit.ametistaengine.AmetistaEngine
import com.tecknobit.equinoxcompose.network.EquinoxRequester
import com.tecknobit.equinoxcore.annotations.RequestPath
import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.equinoxcore.annotations.Wrapper
import com.tecknobit.equinoxcore.network.RequestMethod.DELETE
import com.tecknobit.equinoxcore.network.RequestMethod.GET
import com.tecknobit.equinoxcore.network.RequestMethod.PATCH
import com.tecknobit.equinoxcore.network.RequestMethod.POST
import com.tecknobit.equinoxcore.network.RequestMethod.PUT
import com.tecknobit.equinoxcore.network.Requester
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.PAGE_KEY
import com.tecknobit.neutron.ui.screens.revenues.data.ProjectRevenue
import com.tecknobit.neutron.ui.screens.revenues.data.Revenue
import com.tecknobit.neutron.ui.screens.revenues.data.RevenueLabel
import com.tecknobit.neutron.ui.screens.revenues.data.TicketRevenue
import com.tecknobit.neutroncore.CURRENCY_KEY
import com.tecknobit.neutroncore.GENERAL_REVENUES_KEY
import com.tecknobit.neutroncore.IS_PROJECT_REVENUE_KEY
import com.tecknobit.neutroncore.PROJECTS_KEY
import com.tecknobit.neutroncore.PROJECT_REVENUES_KEY
import com.tecknobit.neutroncore.REVENUES_KEY
import com.tecknobit.neutroncore.REVENUE_DATE_KEY
import com.tecknobit.neutroncore.REVENUE_DESCRIPTION_KEY
import com.tecknobit.neutroncore.REVENUE_LABELS_KEY
import com.tecknobit.neutroncore.REVENUE_PERIOD_KEY
import com.tecknobit.neutroncore.REVENUE_TITLE_KEY
import com.tecknobit.neutroncore.REVENUE_VALUE_KEY
import com.tecknobit.neutroncore.enums.NeutronCurrency
import com.tecknobit.neutroncore.enums.RevenuePeriod
import com.tecknobit.neutroncore.helpers.NeutronEndpoints.CHANGE_CURRENCY_ENDPOINT
import com.tecknobit.neutroncore.helpers.NeutronEndpoints.DYNAMIC_ACCOUNT_DATA_ENDPOINT
import com.tecknobit.neutroncore.helpers.NeutronEndpoints.TICKETS_ENDPOINT
import com.tecknobit.neutroncore.helpers.NeutronEndpoints.WALLET_ENDPOINT
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray


/**
 * The **NeutronRequester** class is useful to communicate with the Neutron's backend
 *
 * @param host The host where is running the Neutron's backend
 * @param userId The user identifier
 * @param userToken The user token
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see Requester
 */
@Structure
open class NeutronRequester(
    host: String,
    userId: String? = null,
    userToken: String? = null,
    debugMode: Boolean = false
) : EquinoxRequester (
    host = host,
    userId = userId,
    userToken = userToken,
    connectionErrorMessage = "Server is temporarily unavailable",
    debugMode = debugMode,
    byPassSSLValidation = true
) {
    
    init {
        attachInterceptorOnRequest { 
            AmetistaEngine.ametistaEngine.notifyNetworkRequest()
        }
    }

    @Deprecated("REMOVE WHEN INTEGRATED IN THE EQUINOX LIBRARY")
    @RequestPath(path = "/api/v1/users/{id}/dynamicAccountData", method = PATCH)
    suspend fun getDynamicAccountData() : JsonObject {
        return execGet(
            endpoint = assembleUsersEndpointPath(DYNAMIC_ACCOUNT_DATA_ENDPOINT)
        )
    }

    /**
     * Method to execute the request to change the currency of the user
     *
     * @param newCurrency The new currency of the user
     *
     * @return the result of the request as [JsonObject]
     */
    @RequestPath(path = "/api/v1/users/{id}/changeCurrency", method = PATCH)
    suspend fun changeCurrency(
        newCurrency: NeutronCurrency
    ): JsonObject {
        val payload = buildJsonObject {
            put(CURRENCY_KEY, newCurrency.name)
        }
        return execPatch(
            endpoint = assembleUsersEndpointPath(CHANGE_CURRENCY_ENDPOINT),
            payload = payload
        )
    }

    @RequestPath(path = "/api/v1/users/{id}/revenues", method = GET)
    suspend fun getWalletStatus(
        period: RevenuePeriod,
        labels: List<RevenueLabel>,
        retrieveGeneralRevenues: Boolean,
        retrieveProjectsRevenues: Boolean
    ): JsonObject {
        val query = assembleRevenuesQuery(
            period = period,
            labels = labels,
            retrieveGeneralRevenues = retrieveGeneralRevenues,
            retrieveProjectsRevenues = retrieveProjectsRevenues
        )
        return execGet(
            endpoint = assembleWalletEndpoint(),
            query = query
        )
    }

    private fun assembleWalletEndpoint(
        subEndpoint: String = ""
    ) : String {
        return assembleCustomEndpointPath(
            customEndpoint = WALLET_ENDPOINT,
            subEndpoint = subEndpoint
        )
    }

    @RequestPath(path = "/api/v1/users/{id}/revenues/revenue_labels", method = GET)
    suspend fun getRevenuesLabels(): JsonObject {
        return execGet(
            endpoint = assembleCustomEndpointPath(
                customEndpoint = REVENUES_KEY,
                subEndpoint = REVENUE_LABELS_KEY
            )
        )
    }

    @RequestPath(path = "/api/v1/users/{id}/revenues", method = GET)
    suspend fun getRevenues(
        page: Int,
        period: RevenuePeriod,
        labels: List<RevenueLabel>,
        retrieveGeneralRevenues: Boolean,
        retrieveProjectsRevenues: Boolean
    ): JsonObject {
        val query = assembleRevenuesQuery(
            period = period,
            labels = labels,
            retrieveGeneralRevenues = retrieveGeneralRevenues,
            retrieveProjectsRevenues = retrieveProjectsRevenues
        ).toMutableMap()
        query[PAGE_KEY] = JsonPrimitive(page)
        return execGet(
            endpoint = assembleRevenuesEndpointPath(),
            query = Json.encodeToJsonElement(query).jsonObject
        )
    }

    private fun assembleRevenuesQuery(
        period: RevenuePeriod,
        labels: List<RevenueLabel>,
        retrieveGeneralRevenues: Boolean,
        retrieveProjectsRevenues: Boolean,
    ): JsonObject {
        return buildJsonObject {
            put(REVENUE_PERIOD_KEY, period.name)
            putJsonArray(REVENUE_LABELS_KEY) {
                labels.forEach { add(it.text) }
            }
            put(GENERAL_REVENUES_KEY, retrieveGeneralRevenues)
            put(PROJECT_REVENUES_KEY, retrieveProjectsRevenues)
        }
    }

    suspend fun insertRevenue(
        addingGeneralRevenue: Boolean,
        title: String,
        description: String,
        value: Double,
        revenueDate: LocalDateTime,
        labels: List<RevenueLabel> = emptyList(),
    ): JsonObject {
        val revenueDateMillis = revenueDate
            .toInstant(TimeZone.currentSystemDefault())
            .toEpochMilliseconds()
        return if (addingGeneralRevenue) {
            createGeneralRevenue(
                title = title,
                description = description,
                value = value,
                revenueDate = revenueDateMillis,
                labels = labels
            )
        } else {
            createProjectRevenue(
                title = title,
                value = value,
                revenueDate = revenueDateMillis
            )
        }
    }

    /**
     * Method to execute the request to create a new general revenue
     *
     * @param title The title of the general revenue
     * @param description The description of the general revenue
     * @param value The amount revenue value
     * @param revenueDate The date when the general revenue has been created
     * @param labels The labels to attach to the general revenue
     *
     * @return the result of the request as [JsonObject]
     */
    @Wrapper
    @RequestPath(path = "/api/v1/users/{id}/revenues", method = POST)
    private suspend fun createGeneralRevenue(
        title: String,
        description: String,
        value: Double,
        revenueDate: Long,
        labels: List<RevenueLabel> = emptyList(),
    ): JsonObject {
        val payload = buildJsonObject {
            put(REVENUE_DESCRIPTION_KEY, description)
            put(REVENUE_LABELS_KEY, labels.joinToString())
        }
        return createRevenue(
            title = title,
            value = value,
            revenueDate = revenueDate,
            payload = payload
        )
    }

    /**
     * Method to execute the request to create a new project revenue
     *
     * @param title The title of the project
     * @param value The initial revenue value
     * @param revenueDate The date when the project has been created
     *
     * @return the result of the request as [JsonObject]
     */
    @Wrapper
    @RequestPath(path = "/api/v1/users/{id}/revenues", method = POST)
    private suspend fun createProjectRevenue(
        title: String,
        value: Double,
        revenueDate: Long,
    ): JsonObject {
        return createRevenue(
            title = title,
            value = value,
            revenueDate = revenueDate
        )
    }

    /**
     * Method to execute the request to create a new revenue
     *
     * @param payload The payload to send
     * @param title The title of the revenue
     * @param value The amount revenue value
     * @param revenueDate The date when the revenue has been created
     *
     * @return the result of the request as [JsonObject]
     */
    @RequestPath(path = "/api/v1/users/{id}/revenues", method = POST)
    private suspend fun createRevenue(
        payload: JsonObject? = null,
        title: String,
        value: Double,
        revenueDate: Long
    ): JsonObject {
        val isProjectRevenue = payload == null
        val revenuePayload = buildJsonObject {
            put(IS_PROJECT_REVENUE_KEY, isProjectRevenue)
            put(REVENUE_TITLE_KEY, title)
            put(REVENUE_VALUE_KEY, value)
            put(REVENUE_DATE_KEY, revenueDate)
            payload?.let { generalRevenuePayload ->
                generalRevenuePayload.entries.forEach { entry ->
                    put(entry.key, entry.value)
                }
            }
        }
        return execPost(
            endpoint = assembleRevenuesEndpointPath(),
            payload = revenuePayload
        )
    }

    /**
     * Method to execute the request to get a project revenue
     *
     * @param revenue The project revenue to get
     *
     * @return the result of the request as [JsonObject]
     */
    @RequestPath(path = "/api/v1/users/{id}/revenues/projects/{revenue_id}", method = GET)
    suspend fun getProjectRevenue(
        revenue: Revenue
    ): JsonObject {
        return getProjectRevenue(
            revenueId = revenue.id
        )
    }

    /**
     * Method to execute the request to get a project revenue
     *
     * @param revenueId The identifier of the project revenue to get
     *
     * @return the result of the request as [JsonObject]
     */
    @RequestPath(path = "/api/v1/users/{id}/revenues/projects/{revenue_id}", method = GET)
    suspend fun getProjectRevenue(
        revenueId: String
    ): JsonObject {
        return execGet(
            endpoint = assembleRevenuesEndpointPath(
                revenueId = revenueId,
                isProjectPath = true
            )
        )
    }

    /**
     * Method to execute the request to add a new ticket to a project revenue
     *
     * @param project The project where add the ticket
     * @param title The title of the ticket
     * @param value The value of the ticket
     * @param description The description of the ticket
     * @param openingDate The date when the ticket has been opened
     *
     * @return the result of the request as [JsonObject]
     */
    @RequestPath(path = "/api/v1/users/{id}/revenues/projects/{revenue_id}/tickets", method = POST)
    suspend fun addTicketToProject(
        project: ProjectRevenue,
        title: String,
        value: Double,
        description: String,
        openingDate: Long
    ): JsonObject {
        val payload = buildJsonObject {
            put(REVENUE_TITLE_KEY, title)
            put(REVENUE_VALUE_KEY, value)
            put(REVENUE_DESCRIPTION_KEY, description)
            put(REVENUE_DATE_KEY, openingDate)
        }
        return execPost(
            endpoint = assembleRevenuesEndpointPath(
                revenueId = project.id,
                isProjectPath = true,
                extraPath = TICKETS_ENDPOINT
            ),
            payload = payload
        )
    }

    /**
     * Method to execute the request to close a ticket
     *
     * @param project The project revenue where close a ticket
     * @param ticket The ticket to close
     *
     * @return the result of the request as [JsonObject]
     */
    @RequestPath(path = "/api/v1/users/{id}/revenues/projects/{revenue_id}/tickets/{ticket_id}", method = PUT)
    suspend fun closeTicket(
        project: ProjectRevenue,
        ticket: TicketRevenue
    ): JsonObject {
        return execPut(
            endpoint = assembleRevenuesEndpointPath(
                revenueId = project.id,
                isProjectPath = true,
                extraPath = TICKETS_ENDPOINT,
                extraId = "/${ticket.id}"
            )
        )
    }

    /**
     * Method to execute the request to delete a ticket
     *
     * @param project The project revenue where delete a ticket
     * @param ticket The ticket to delete
     *
     * @return the result of the request as [JsonObject]
     */
    @RequestPath(
        path = "/api/v1/users/{id}/revenues/projects/{revenue_id}/tickets/{ticket_id}",
        method = DELETE
    )
    suspend fun deleteTicket(
        project: ProjectRevenue,
        ticket: TicketRevenue
    ): JsonObject {
        return execDelete(
            endpoint = assembleRevenuesEndpointPath(
                revenueId = project.id,
                isProjectPath = true,
                extraPath = TICKETS_ENDPOINT,
                extraId = "/${ticket.id}"
            )
        )
    }

    /**
     * Method to execute the request to delete a revenue
     *
     * @param revenue The revenue to delete
     *
     * @return the result of the request as [JsonObject]
     */
    @RequestPath(path = "/api/v1/users/{id}/revenues/{revenue_id}", method = DELETE)
    suspend fun deleteRevenue(
        revenue: Revenue
    ): JsonObject {
        return execDelete(
            endpoint = assembleRevenuesEndpointPath(
                revenueId = revenue.id
            )
        )
    }

    /**
     * Method to assemble the endpoint to make the request to the users controller
     *
     * @param endpoint The endpoint path of the url
     *
     * @return an endpoint to make the request as [String]
     */
    private fun assembleRevenuesEndpointPath(
        endpoint: String = "",
        isProjectPath: Boolean = false,
        revenueId: String? = null,
        extraPath: String = "",
        extraId: String = ""
    ): String {
        var baseEndpoint = "${assembleUsersEndpointPath(endpoint)}/$REVENUES_KEY"
        if(isProjectPath)
            baseEndpoint = "$baseEndpoint$PROJECTS_KEY$revenueId$extraPath$extraId"
        else if (revenueId != null)
            baseEndpoint = "$baseEndpoint/$revenueId"
        return baseEndpoint
    }

}