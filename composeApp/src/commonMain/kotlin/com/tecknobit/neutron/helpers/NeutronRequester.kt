package com.tecknobit.neutron.helpers

import com.tecknobit.equinoxcompose.network.EquinoxRequester
import com.tecknobit.equinoxcore.annotations.RequestPath
import com.tecknobit.equinoxcore.annotations.Wrapper
import com.tecknobit.equinoxcore.network.RequestMethod.DELETE
import com.tecknobit.equinoxcore.network.RequestMethod.GET
import com.tecknobit.equinoxcore.network.RequestMethod.PATCH
import com.tecknobit.equinoxcore.network.RequestMethod.POST
import com.tecknobit.equinoxcore.network.RequestMethod.PUT
import com.tecknobit.equinoxcore.network.Requester
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.PAGE_KEY
import com.tecknobit.equinoxcore.time.TimeFormatter.toMillis
import com.tecknobit.neutron.ui.screens.revenues.data.Revenue
import com.tecknobit.neutron.ui.screens.revenues.data.RevenueLabel
import com.tecknobit.neutron.ui.screens.revenues.data.TicketRevenue
import com.tecknobit.neutroncore.CLOSED_TICKETS_KEY
import com.tecknobit.neutroncore.CURRENCY_KEY
import com.tecknobit.neutroncore.GENERAL_REVENUES_KEY
import com.tecknobit.neutroncore.IS_PROJECT_REVENUE_KEY
import com.tecknobit.neutroncore.LABELS_KEY
import com.tecknobit.neutroncore.PENDING_TICKETS_KEY
import com.tecknobit.neutroncore.PROJECTS_KEY
import com.tecknobit.neutroncore.PROJECT_REVENUES_KEY
import com.tecknobit.neutroncore.REVENUES_KEY
import com.tecknobit.neutroncore.REVENUE_DATE_KEY
import com.tecknobit.neutroncore.REVENUE_DESCRIPTION_KEY
import com.tecknobit.neutroncore.REVENUE_PERIOD_KEY
import com.tecknobit.neutroncore.REVENUE_TITLE_KEY
import com.tecknobit.neutroncore.REVENUE_VALUE_KEY
import com.tecknobit.neutroncore.enums.NeutronCurrency
import com.tecknobit.neutroncore.enums.RevenuePeriod
import com.tecknobit.neutroncore.helpers.NeutronEndpoints.CHANGE_CURRENCY_ENDPOINT
import com.tecknobit.neutroncore.helpers.NeutronEndpoints.PROJECT_BALANCE_ENDPOINT
import com.tecknobit.neutroncore.helpers.NeutronEndpoints.TICKETS_ENDPOINT
import com.tecknobit.neutroncore.helpers.NeutronEndpoints.WALLET_ENDPOINT
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.put


/**
 * The `NeutronRequester** class is useful to communicate with the Neutron's backend
 *
 * @param host The host where is running the Neutron's backend
 * @param userId The user identifier
 * @param userToken The user token
 * @param debugMode Whether the requester is still in development and who is developing needs the log of the requester's
 * workflow, if it is enabled all the details of the requests sent and the errors occurred will be printed in the console
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see Requester
 */
class NeutronRequester(
    host: String,
    userId: String? = null,
    userToken: String? = null,
    debugMode: Boolean = false,
) : EquinoxRequester (
    host = host,
    userId = userId,
    userToken = userToken,
    connectionErrorMessage = "Server is temporarily unavailable",
    debugMode = debugMode,
    byPassSSLValidation = true
) {
    
    init {
        // TODO: TO INTEGRATE
        /*attachInterceptorOnRequest {
            AmetistaEngine.ametistaEngine.notifyNetworkRequest()
        }*/
    }

    /**
     * Method to request to change the currency of the user
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

    /**
     * Method to request the current wallet status of the user
     *
     * @param period The period of the revenues to consider in the wallet count
     * @param labels The labels used as filters
     * @param retrieveGeneralRevenues Whether count also the general revenues
     * @param retrieveProjectsRevenues Whether count also the project
     *
     * @return the result of the request as [JsonObject]
     */
    @RequestPath(path = "/api/v1/users/{id}/wallet", method = GET)
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

    /**
     * Method to assemble an endpoint url about the wallet section
     *
     * @param subEndpoint The sub-endpoint of the path
     *
     * @return the endpoint url as [String]
     */
    private fun assembleWalletEndpoint(
        subEndpoint: String = ""
    ) : String {
        return assembleCustomEndpointPath(
            customEndpoint = WALLET_ENDPOINT,
            subEndpoint = subEndpoint
        )
    }

    /**
     * Method to request the current labels created by the user
     *
     * @return the result of the request as [JsonObject]
     */
    @RequestPath(path = "/api/v1/users/{id}/revenues/labels", method = GET)
    suspend fun getRevenuesLabels(): JsonObject {
        return execGet(
            endpoint = assembleCustomEndpointPath(
                customEndpoint = REVENUES_KEY,
                subEndpoint = LABELS_KEY
            )
        )
    }

    /**
     * Method to request the current revenues owned by the user
     *
     * @param page The page to request
     * @param period The period of the revenues to consider in the wallet count
     * @param labels The labels used as filters
     * @param retrieveGeneralRevenues Whether count also the general revenues
     * @param retrieveProjectsRevenues Whether count also the project
     *
     * @return the result of the request as [JsonObject]
     */
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

    /**
     * Method to assemble the query for the [getWalletStatus] and the [getRevenues] requests
     *
     * @param period The period of the revenues to consider in the wallet count
     * @param labels The labels used as filters
     * @param retrieveGeneralRevenues Whether count also the general revenues
     * @param retrieveProjectsRevenues Whether count also the project
     *
     * @return the query as [JsonObject]
     */
    private fun assembleRevenuesQuery(
        period: RevenuePeriod,
        labels: List<RevenueLabel>,
        retrieveGeneralRevenues: Boolean,
        retrieveProjectsRevenues: Boolean,
    ): JsonObject {
        return buildJsonObject {
            put(REVENUE_PERIOD_KEY, period.name)
            put(LABELS_KEY, labels.joinToString { it.text })
            put(GENERAL_REVENUES_KEY, retrieveGeneralRevenues)
            put(PROJECT_REVENUES_KEY, retrieveProjectsRevenues)
        }
    }

    /**
     * Method to request the revenue insertion, this includes both the add or editing actions
     *
     * @param addingGeneralRevenue Whether the revenue to insert is a [com.tecknobit.neutron.ui.screens.revenues.data.GeneralRevenue] one
     * @param revenue The revenue to edit
     * @param title The title of the general revenue
     * @param description The description of the general revenue
     * @param value The amount revenue value
     * @param revenueDate The date when the revenue has been created
     * @param labels The labels to attach to the general revenue
     *
     * @return the result of the request as [JsonObject]
     */
    suspend fun insertRevenue(
        addingGeneralRevenue: Boolean,
        revenue: Revenue? = null,
        title: String,
        description: String,
        value: Double,
        revenueDate: LocalDateTime,
        labels: List<RevenueLabel> = emptyList(),
    ): JsonObject {
        val revenueDateMillis = revenueDate.toMillis()
        return if (revenue != null) {
            if (addingGeneralRevenue) {
                editGeneralRevenue(
                    revenue = revenue,
                    title = title,
                    description = description,
                    value = value,
                    revenueDate = revenueDateMillis,
                    labels = labels
                )
            } else {
                editProjectRevenue(
                    revenue = revenue,
                    title = title,
                    value = value,
                    revenueDate = revenueDateMillis
                )
            }
        } else {
            if (addingGeneralRevenue) {
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
    }

    /**
     * Method to request to edit an existing general revenue
     *
     * @param revenue The revenue to edit
     * @param title The title of the general revenue
     * @param description The description of the general revenue
     * @param value The amount revenue value
     * @param revenueDate The date when the general revenue has been created
     * @param labels The labels to attach to the general revenue
     *
     * @return the result of the request as [JsonObject]
     */
    @RequestPath(path = "/api/v1/users/{id}/revenues/{revenue_id}", method = PATCH)
    private suspend fun editGeneralRevenue(
        revenue: Revenue,
        title: String,
        description: String,
        value: Double,
        revenueDate: Long,
        labels: List<RevenueLabel> = emptyList(),
    ): JsonObject {
        val payload = buildJsonObject {
            put(REVENUE_DESCRIPTION_KEY, description)
            put(LABELS_KEY, Json.encodeToJsonElement(labels))
        }
        return editRevenue(
            revenue = revenue,
            title = title,
            value = value,
            revenueDate = revenueDate,
            payload = payload
        )
    }

    /**
     * Method to request to edit an existing project revenue
     *
     * @param revenue The revenue to edit
     * @param title The title of the general revenue
     * @param value The amount revenue value
     * @param revenueDate The date when the project revenue has been created
     *
     * @return the result of the request as [JsonObject]
     */
    @RequestPath(path = "/api/v1/users/{id}/revenues/{revenue_id}", method = PATCH)
    private suspend fun editProjectRevenue(
        revenue: Revenue,
        title: String,
        value: Double,
        revenueDate: Long,
    ): JsonObject {
        return editRevenue(
            revenue = revenue,
            title = title,
            value = value,
            revenueDate = revenueDate
        )
    }

    /**
     * Method to request to edit an existing project revenue
     *
     * @param revenue The revenue to edit
     * @param payload The payload with the extra information of a general revenue
     * @param title The title of the general revenue
     * @param value The amount revenue value
     * @param revenueDate The date when the project revenue has been created
     *
     * @return the result of the request as [JsonObject]
     */
    @RequestPath(path = "/api/v1/users/{id}/revenues/{revenue_id}", method = PATCH)
    private suspend fun editRevenue(
        revenue: Revenue,
        payload: JsonObject? = null,
        title: String,
        value: Double,
        revenueDate: Long,
    ): JsonObject {
        val revenuePayload = assembleRevenuePayload(
            payload = payload,
            title = title,
            value = value,
            revenueDate = revenueDate
        )
        return execPatch(
            endpoint = assembleRevenuesEndpointPath(
                revenueId = revenue.id
            ),
            payload = revenuePayload
        )
    }

    /**
     * Method to request to create a new general revenue
     *
     * @param title The title of the general revenue
     * @param description The description of the general revenue
     * @param value The amount revenue value
     * @param revenueDate The date when the general revenue has been created
     * @param labels The labels to attach to the general revenue
     *
     * @return the result of the request as [JsonObject]
     */
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
            put(LABELS_KEY, Json.encodeToJsonElement(labels))
        }
        return createRevenue(
            title = title,
            value = value,
            revenueDate = revenueDate,
            payload = payload
        )
    }

    /**
     * Method to request to create a new project revenue
     *
     * @param title The title of the project
     * @param value The initial revenue value
     * @param revenueDate The date when the project has been created
     *
     * @return the result of the request as [JsonObject]
     */
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
     * Method to request to create a new revenue
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
        revenueDate: Long,
    ): JsonObject {
        val revenuePayload = assembleRevenuePayload(
            payload = payload,
            title = title,
            value = value,
            revenueDate = revenueDate
        )
        return execPost(
            endpoint = assembleRevenuesEndpointPath(),
            payload = revenuePayload
        )
    }

    /**
     * Method to assemble the payload for the [insertRevenue] request
     *
     * @param payload The payload with the extra information of a general revenue
     * @param title The title of the general revenue
     * @param value The amount revenue value
     * @param revenueDate The date when the revenue has been created
     *
     * @return the payload as [JsonObject]
     */
    private fun assembleRevenuePayload(
        payload: JsonObject?,
        title: String,
        value: Double,
        revenueDate: Long,
    ): JsonObject {
        val isProjectRevenue = payload == null
        return buildJsonObject {
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
    }

    /**
     * Method to request a revenue by the [revenueId] specified
     *
     * @param revenueId The identifier of the revenue to get
     *
     * @return the result of the request as [JsonObject]
     */
    @Wrapper
    @RequestPath(path = "/api/v1/users/{id}/revenues/{revenue_id}", method = GET)
    suspend fun getRevenue(
        revenueId: String,
    ): JsonObject {
        return execGet(
            endpoint = assembleRevenuesEndpointPath(
                revenueId = revenueId
            )
        )
    }

    /**
     * Method to request a project revenue
     *
     * @param projectId The identifier of the project revenue to get
     *
     * @return the result of the request as [JsonObject]
     */
    @RequestPath(path = "/api/v1/users/{id}/revenues/projects/{revenue_id}", method = GET)
    suspend fun getProjectRevenue(
        projectId: String,
    ): JsonObject {
        return execGet(
            endpoint = assembleRevenuesEndpointPath(
                revenueId = projectId,
                isProjectPath = true
            )
        )
    }

    /**
     * Method to request the current balance of a project revenue
     *
     * @param projectId The identifier of the project revenue
     * @param period The period of the revenues to consider in the balance
     * @param retrieveClosedTickets Whether retrieve the closed tickets
     *
     * @return the result of the request as [JsonObject]
     */
    @RequestPath(path = "/api/v1/users/{id}/revenues/projects/{revenue_id}/balance", method = GET)
    suspend fun getProjectBalance(
        projectId: String,
        period: RevenuePeriod,
        retrieveClosedTickets: Boolean,
    ): JsonObject {
        val query = buildJsonObject {
            put(REVENUE_PERIOD_KEY, period.name)
            put(CLOSED_TICKETS_KEY, retrieveClosedTickets)
        }
        return execGet(
            endpoint = assembleRevenuesEndpointPath(
                revenueId = projectId,
                isProjectPath = true,
                extraPath = PROJECT_BALANCE_ENDPOINT
            ),
            query = query
        )
    }

    /**
     * Method to request to insert a tickets, this includes both edit and addition of a ticket item
     *
     * @param projectId The identifier of the project where the tickets is attached
     * @param ticketId The identifier of the ticket to edit
     * @param title The title of the ticket
     * @param value The amount revenue value
     * @param description The description of the ticket
     * @param openingDate The date when the ticket has been opened
     *
     * @return the result of the request as [JsonObject]
     */
    suspend fun insertTicket(
        projectId: String,
        ticketId: String?,
        title: String,
        value: Double,
        description: String,
        openingDate: LocalDateTime,
    ): JsonObject {
        val openingDateMillis = openingDate.toMillis()
        val payload = buildJsonObject {
            put(REVENUE_TITLE_KEY, title)
            put(REVENUE_VALUE_KEY, value)
            put(REVENUE_DESCRIPTION_KEY, description)
            put(REVENUE_DATE_KEY, openingDateMillis)
        }
        return if (ticketId == null) {
            addTicket(
                projectId = projectId,
                payload = payload
            )
        } else {
            editTicket(
                projectId = projectId,
                ticketId = ticketId,
                payload = payload
            )
        }
    }

    /**
     * Method to request to add a ticket
     *
     * @param projectId The identifier of the project where the tickets is attached
     * @param payload The payload with the ticket information
     *
     * @return the result of the request as [JsonObject]
     */
    @RequestPath(path = "/api/v1/users/{id}/revenues/projects/{revenue_id}/tickets", method = POST)
    private suspend fun addTicket(
        projectId: String,
        payload: JsonObject,
    ): JsonObject {
        return execPost(
            endpoint = assembleRevenuesEndpointPath(
                revenueId = projectId,
                isProjectPath = true,
                extraPath = TICKETS_ENDPOINT
            ),
            payload = payload
        )
    }

    /**
     * Method to request to edit an existing ticket
     *
     * @param projectId The identifier of the project where the tickets is attached
     * @param ticketId The identifier of the ticket to edit
     * @param payload The payload with the ticket information
     *
     * @return the result of the request as [JsonObject]
     */
    @RequestPath(
        path = "/api/v1/users/{id}/revenues/projects/{revenue_id}/tickets/{ticket_id}",
        method = PATCH
    )
    private suspend fun editTicket(
        projectId: String,
        ticketId: String,
        payload: JsonObject,
    ): JsonObject {
        return execPatch(
            endpoint = assembleRevenuesEndpointPath(
                revenueId = projectId,
                isProjectPath = true,
                extraPath = TICKETS_ENDPOINT,
                extraId = "/$ticketId"
            ),
            payload = payload
        )
    }

    /**
     * Method to request the tickets attached to the [projectId]
     *
     * @param projectId The identifier of the project revenue
     * @param page The page to request
     * @param period The period of the tickets to retrieve
     * @param retrievePendingTickets Whether retrieve the pending tickets
     * @param retrieveClosedTickets Whether retrieve the closed tickets
     *
     * @return the result of the request as [JsonObject]
     */
    @RequestPath(path = "/api/v1/users/{id}/revenues/projects/{revenue_id}/tickets", method = GET)
    suspend fun getTickets(
        projectId: String,
        page: Int,
        period: RevenuePeriod,
        retrievePendingTickets: Boolean,
        retrieveClosedTickets: Boolean,
    ): JsonObject {
        val query = buildJsonObject {
            put(PAGE_KEY, page)
            put(REVENUE_PERIOD_KEY, period.name)
            put(PENDING_TICKETS_KEY, retrievePendingTickets)
            put(CLOSED_TICKETS_KEY, retrieveClosedTickets)
        }
        return execGet(
            endpoint = assembleRevenuesEndpointPath(
                revenueId = projectId,
                isProjectPath = true,
                extraPath = TICKETS_ENDPOINT
            ),
            query = query
        )
    }

    /**
     * Method to request to close a ticket
     *
     * @param projectId The identifier of the project revenue where close a ticket
     * @param ticket The ticket to close
     *
     * @return the result of the request as [JsonObject]
     */
    @RequestPath(path = "/api/v1/users/{id}/revenues/projects/{revenue_id}/tickets/{ticket_id}", method = PUT)
    suspend fun closeTicket(
        projectId: String,
        ticket: TicketRevenue,
    ): JsonObject {
        return execPut(
            endpoint = assembleRevenuesEndpointPath(
                revenueId = projectId,
                isProjectPath = true,
                extraPath = TICKETS_ENDPOINT,
                extraId = "/${ticket.id}"
            )
        )
    }

    /**
     * Method to request to delete a ticket
     *
     * @param projectId The identifier of project revenue where delete a ticket
     * @param ticket The ticket to delete
     *
     * @return the result of the request as [JsonObject]
     */
    @RequestPath(
        path = "/api/v1/users/{id}/revenues/projects/{revenue_id}/tickets/{ticket_id}",
        method = DELETE
    )
    suspend fun deleteTicket(
        projectId: String,
        ticket: TicketRevenue,
    ): JsonObject {
        return execDelete(
            endpoint = assembleRevenuesEndpointPath(
                revenueId = projectId,
                isProjectPath = true,
                extraPath = TICKETS_ENDPOINT,
                extraId = "/${ticket.id}"
            )
        )
    }

    /**
     * Method to request to delete a revenue
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