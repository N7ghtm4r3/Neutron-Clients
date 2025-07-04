@file:OptIn(ExperimentalComposeApi::class)

package com.tecknobit.neutron.ui.screens.shared.presenters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.annotations.ScreenCoordinator
import com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
import com.tecknobit.equinoxcompose.utilities.EXPANDED_CONTAINER
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.neutron.displayFontFamily
import com.tecknobit.neutron.navigator
import com.tecknobit.neutron.ui.theme.NeutronTheme
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * The [NeutronScreen] class is useful to provides the basic behavior of a NeutronScreen's UI screen
 *
 * @param viewModel The support viewmodel for the screen
 *
 * @property V generic type of the viewmodel of the screen
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxScreen
 */
@Structure
@ScreenCoordinator
abstract class NeutronScreen<V : EquinoxViewModel>(
    viewModel: V,
    private val title: StringResource
) : EquinoxScreen<V>(
    viewModel = viewModel
) {

    /**
     * Method to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        NeutronTheme {
            Scaffold (
                snackbarHost = { SnackbarHost(viewModel.snackbarHostState!!) }
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .widthIn(
                                max = EXPANDED_CONTAINER
                            ),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row (
                            modifier = Modifier
                                .padding(
                                    top = 35.dp
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = { navigator.goBack() }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = null
                                )
                            }
                            Text(
                                text = stringResource(title),
                                fontSize = 35.sp,
                                fontFamily = displayFontFamily,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        ScreenContent()
                    }
                }
            }
        }
    }

    /**
     * Method to display the custom content of the screen
     */
    @Composable
    abstract fun ScreenContent()

}