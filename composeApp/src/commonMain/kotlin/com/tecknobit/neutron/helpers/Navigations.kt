@file:OptIn(ExperimentalStdlibApi::class)

package com.tecknobit.neutron.helpers

import androidx.navigation.NavHostController
import com.tecknobit.equinoxcompose.annotations.DestinationScreen
import com.tecknobit.equinoxmisc.navigationcomposeutil.navWithData
import com.tecknobit.neutron.ui.screens.Splashscreen
import com.tecknobit.neutron.ui.screens.insert.revenue.presenter.InsertRevenueScreen
import com.tecknobit.neutron.ui.screens.insert.ticket.presenter.InsertTicketScreen
import com.tecknobit.neutron.ui.screens.profile.presenter.ProfileScreen
import com.tecknobit.neutron.ui.screens.project.data.TicketRevenue
import com.tecknobit.neutron.ui.screens.project.presenter.ProjectScreen
import com.tecknobit.neutron.ui.screens.revenues.presenter.RevenuesScreen
import com.tecknobit.neutron.ui.screens.shared.data.ProjectRevenue
import com.tecknobit.neutron.ui.screens.shared.data.Revenue
import com.tecknobit.neutroncore.REVENUE_IDENTIFIER_KEY
import com.tecknobit.neutroncore.TICKET_IDENTIFIER_KEY

/**
 * `navigator` the navigator instance is useful to manage the navigation between the screens of the application
 */
lateinit var navigator: NavHostController

/**
 * `SPLASHSCREEN` route to navigate to the [com.tecknobit.neutron.ui.screens.Splashscreen]
 */
const val SPLASHSCREEN = "Splashscreen"

/**
 * `AUTH_SCREEN` route to navigate to the [com.tecknobit.neutron.ui.screens.auth.presenter.AuthScreen]
 */
const val AUTH_SCREEN = "AuthScreen"

/**
 * `REVENUES_SCREEN` route to navigate to the [com.tecknobit.neutron.ui.screens.auth.presenter.AuthScreen]
 */
const val REVENUES_SCREEN = "RevenuesScreen"

/**
 * `PROFILE_SCREEN` route to navigate to the [com.tecknobit.neutron.ui.screens.profile.presenter.ProfileScreen]
 */
const val PROFILE_SCREEN = "ProfileScreen"

/**
 * `INSERT_REVENUE_SCREEN` route to navigate to the [com.tecknobit.neutron.ui.screens.insert.revenue.presenter.InsertRevenueScreen]
 */
const val INSERT_REVENUE_SCREEN = "InsertRevenueScreen"

/**
 * `INSERT_TICKET_SCREEN` route to navigate to the [com.tecknobit.neutron.ui.screens.insert.ticket.presenter.InsertTicketScreen]
 */
const val INSERT_TICKET_SCREEN = "InsertTicketScreen"

/**
 * `PROJECT_REVENUE_SCREEN` route to navigate to the [com.tecknobit.neutron.ui.screens.project.presenter.ProjectScreen]
 */
const val PROJECT_REVENUE_SCREEN = "ProjectRevenueScreen"

/**
 * Method used to navigate to the [Splashscreen]
 *
 * @since 1.0.5
 */
@DestinationScreen(Splashscreen::class)
fun navToSplashscreen() {
    navigator.navigate(SPLASHSCREEN)
}

/**
 * Method used to navigate to the [RevenuesScreen]
 *
 * @since 1.0.5
 */
@DestinationScreen(RevenuesScreen::class)
fun navToRevenuesScreen() {
    navigator.navigate(REVENUES_SCREEN)
}

/**
 * Method used to navigate to the [InsertRevenueScreen]
 *
 * @param revenue The revenue to insert if not `null`
 *
 * @since 1.0.5
 */
@DestinationScreen(InsertRevenueScreen::class)
fun navToInsertRevenueScreen(
    revenue: Revenue? = null,
) {
    navigator.navWithData(
        route = INSERT_REVENUE_SCREEN,
        data = buildMap {
            put(REVENUE_IDENTIFIER_KEY, revenue?.id)
        }
    )
}

/**
 * Method used to navigate to the [ProjectScreen]
 *
 * @param project The project to display
 *
 * @since 1.0.5
 */
@DestinationScreen(ProjectScreen::class)
fun navToProjectScreen(
    project: ProjectRevenue,
) {
    navigator.navWithData(
        route = PROJECT_REVENUE_SCREEN,
        data = buildMap {
            put(REVENUE_IDENTIFIER_KEY, project.id)
        }
    )
}

/**
 * Method used to navigate to the [InsertTicketScreen]
 *
 * @param project The project owner of the ticket
 * @param ticket The ticket to edit if not `null`
 *
 * @since 1.0.5
 */
@DestinationScreen(InsertTicketScreen::class)
fun navToInsertTicketScreen(
    project: ProjectRevenue,
    ticket: TicketRevenue? = null,
) {
    navigator.navWithData(
        route = INSERT_TICKET_SCREEN,
        data = buildMap {
            put(REVENUE_IDENTIFIER_KEY, project.id)
            put(TICKET_IDENTIFIER_KEY, ticket?.id)
        }
    )
}

/**
 * Method used to navigate to the [ProfileScreen]
 *
 * @since 1.0.5
 */
@DestinationScreen(ProfileScreen::class)
fun navToProfileScreen() {
    navigator.navigate(PROFILE_SCREEN)
}