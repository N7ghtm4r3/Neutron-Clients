@file:OptIn(ExperimentalMultiplatform::class)

package com.tecknobit.neutron.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.EmptyState
import com.tecknobit.equinoxcompose.utilities.responsiveAssignment
import com.tecknobit.neutron.ui.theme.AppTypography
import com.tecknobit.neutron.ui.theme.applyDarkTheme
import neutron.composeapp.generated.resources.Res
import neutron.composeapp.generated.resources.no_revenues_dark
import neutron.composeapp.generated.resources.no_revenues_light
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * Empty state displayed when there are no revenues available
 *
 * @param titleRes The representative title of the empty state
 */
@Composable
fun NoRevenues(
    titleRes: StringResource,
) {
    val title = stringResource(titleRes)
    EmptyState(
        containerModifier = Modifier
            .fillMaxSize(),
        resourceSize = responsiveAssignment(
            onExpandedSizeClass = { 275.dp },
            onMediumSizeClass = { 250.dp },
            onCompactSizeClass = { 200.dp }
        ),
        lightResource = Res.drawable.no_revenues_light,
        darkResource = Res.drawable.no_revenues_dark,
        useDarkResource = applyDarkTheme(),
        title = title,
        titleStyle = AppTypography.titleLarge,
        contentDescription = title
    )
}