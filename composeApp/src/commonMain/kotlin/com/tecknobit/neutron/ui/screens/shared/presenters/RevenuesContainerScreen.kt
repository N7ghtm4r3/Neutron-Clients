@file:OptIn(ExperimentalMultiplatform::class)

package com.tecknobit.neutron.ui.screens.shared.presenters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StickyNote2
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

interface RevenuesContainerScreen {

    companion object {

        const val HIDE_BALANCE = "****"

    }

    var hideBalances: State<Boolean>

    @Composable
    @NonRestartableComposable
    fun FabButton() {
        ResponsiveContent(
            onExpandedSizeClass = {
                ExtendedFloatingActionButton(
                    onClick = { navToInsert() }
                ) {
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.StickyNote2,
                            contentDescription = null
                        )
                        Text(
                            text = stringResource(extendedFabText())
                        )
                    }
                }
            },
            onMediumSizeClass = { CompactFabButton() },
            onCompactSizeClass = { CompactFabButton() }
        )
    }

    fun extendedFabText() : StringResource

    @Composable
    @NonRestartableComposable
    fun CompactFabButton() {
        FloatingActionButton(
            onClick = { navToInsert() }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.StickyNote2,
                contentDescription = null
            )
        }
    }

    fun navToInsert()

    @Composable
    @NonRestartableComposable
    fun Header()

}