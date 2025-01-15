@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.neutron.ui.screens.profile.presenter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CurrencyPound
import androidx.compose.material.icons.filled.CurrencyYen
import androidx.compose.material.icons.filled.EuroSymbol
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.components.ChameleonText
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import com.tecknobit.equinoxcompose.components.EquinoxTextField
import com.tecknobit.equinoxcompose.session.EquinoxLocalUser.ApplicationTheme
import com.tecknobit.equinoxcompose.session.EquinoxLocalUser.ApplicationTheme.Auto
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.LANGUAGES_SUPPORTED
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.isEmailValid
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.isPasswordValid
import com.tecknobit.neutron.SPLASHSCREEN
import com.tecknobit.neutron.bodyFontFamily
import com.tecknobit.neutron.displayFontFamily
import com.tecknobit.neutron.localUser
import com.tecknobit.neutron.navigator
import com.tecknobit.neutron.ui.components.CurrencyDollar
import com.tecknobit.neutron.ui.components.DeleteAccount
import com.tecknobit.neutron.ui.components.Logout
import com.tecknobit.neutron.ui.components.ProfilePic
import com.tecknobit.neutron.ui.screens.NeutronScreen
import com.tecknobit.neutron.ui.screens.profile.presentation.ProfileScreenViewModel
import com.tecknobit.neutroncore.enums.NeutronCurrency
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType
import kotlinx.coroutines.launch
import neutron.composeapp.generated.resources.Res
import neutron.composeapp.generated.resources.change_currency
import neutron.composeapp.generated.resources.change_email
import neutron.composeapp.generated.resources.change_language
import neutron.composeapp.generated.resources.change_password
import neutron.composeapp.generated.resources.change_theme
import neutron.composeapp.generated.resources.delete
import neutron.composeapp.generated.resources.email_not_valid
import neutron.composeapp.generated.resources.logout
import neutron.composeapp.generated.resources.new_email
import neutron.composeapp.generated.resources.new_password
import neutron.composeapp.generated.resources.password_not_valid
import neutron.composeapp.generated.resources.profile
import neutron.composeapp.generated.resources.settings
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

class ProfileScreen : NeutronScreen<ProfileScreenViewModel>(
    viewModel = ProfileScreenViewModel(),
    title = Res.string.profile
) {

    @Composable
    override fun ScreenContent() {
        UserDetails()
        Settings()
    }

    /**
     * The details of the [localUser]
     */
    @Composable
    @NonRestartableComposable
    private fun UserDetails() {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 5.dp
                )
                .padding(
                    horizontal = 16.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            ProfilePicker()
            Column {
                Text(
                    text = localUser.completeName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = viewModel!!.email.value,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 12.sp
                )
                ActionButtons()
            }
        }
    }

    /**
     * The profile picker to allow the [localUser] to change his/her profile picture
     */
    @Composable
    @NonRestartableComposable
    private fun ProfilePicker() {
        val launcher = rememberFilePickerLauncher(
            type = PickerType.Image,
            mode = PickerMode.Single
        ) { image ->
            image?.let {
                viewModel!!.viewModelScope.launch {
                    viewModel!!.changeProfilePic(
                        profilePicName = image.name,
                        profilePicBytes = image.readBytes()
                    )
                }
            }
        }
        ProfilePic(
            size = 100.dp,
            profilePic = viewModel!!.profilePic.value,
            onClick = { launcher.launch() }
        )
    }

    /**
     * The actions can be execute on the [localUser] account such logout and delete account
     */
    @Composable
    @NonRestartableComposable
    private fun ActionButtons() {
        Row (
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            val logout = remember { mutableStateOf(false) }
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.inversePrimary
                ),
                shape = RoundedCornerShape(
                    size = 10.dp
                ),
                onClick = { logout.value = true }
            ) {
                ChameleonText(
                    text = stringResource(Res.string.logout),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    backgroundColor = MaterialTheme.colorScheme.inversePrimary
                )
            }
            Logout(
                viewModel = viewModel!!,
                show = logout
            )
            val deleteAccount = remember { mutableStateOf(false) }
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                shape = RoundedCornerShape(
                    size = 10.dp
                ),
                onClick = { deleteAccount.value = true }
            ) {
                Text(
                    text = stringResource(Res.string.delete),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            DeleteAccount(
                viewModel = viewModel!!,
                show = deleteAccount
            )
        }
    }

    /**
     * The settings section to customize the [localUser] experience
     */
    @Composable
    @NonRestartableComposable
    @Deprecated("USE THE BUILT-ONE FROM EQUINOX")
    // TODO: MORE CUSTOMIZATION IN THE OFFICIAL COMPONENT
    private fun Settings() {
        Column (
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    modifier = Modifier
                        .padding(
                            start = 10.dp
                        ),
                    text = stringResource(Res.string.settings),
                    fontFamily = displayFontFamily,
                    fontSize = 22.sp
                )
            }
            Column (
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                ProfileAction(
                    shape = RoundedCornerShape(
                        topStart = 12.dp,
                        topEnd = 12.dp
                    ),
                    leadingIcon = Icons.Default.AlternateEmail,
                    actionText = Res.string.change_email,
                    actionContent = { ChangeEmail() },
                    confirmAction = { visible ->
                        changeEmail {
                            viewModel!!.email.value = localUser.email!!
                            visible.value = false
                        }
                    }
                )
                ProfileAction(
                    leadingIcon = Icons.Default.Password,
                    actionText = Res.string.change_password,
                    actionContent = { ChangePassword() },
                    confirmAction = { visible ->
                        changePassword {
                            visible.value = false
                        }
                    }
                )
                ProfileAction(
                    leadingIcon = Icons.Default.Language,
                    actionText = Res.string.change_language,
                    actionContent = {
                        ChangeLanguage()
                    },
                    dismissAction = { viewModel!!.language.value = localUser.language!! },
                    confirmAction = { visible ->
                        changeLanguage(
                            newLanguage = language.value,
                            onSuccess = {
                                visible.value = false
                                navigator.navigate(SPLASHSCREEN)
                            }
                        )
                    }
                )
                ProfileAction(
                    leadingIcon = when(viewModel!!.currency.value) {
                        NeutronCurrency.EURO -> Icons.Default.EuroSymbol
                        NeutronCurrency.DOLLAR -> CurrencyDollar
                        NeutronCurrency.POUND_STERLING -> Icons.Default.CurrencyPound
                        else -> Icons.Default.CurrencyYen
                    },
                    actionText = Res.string.change_currency,
                    actionContent = {
                        ChangeCurrency()
                    },
                    dismissAction = { viewModel!!.currency.value = localUser.currency },
                    confirmAction = { visible ->
                        changeCurrency(
                            onSuccess = {
                                visible.value = false
                            }
                        )
                    }
                )
                ProfileAction(
                    shape = RoundedCornerShape(
                        bottomStart = 12.dp,
                        bottomEnd = 12.dp
                    ),
                    leadingIcon = Icons.Default.Palette,
                    actionText = Res.string.change_theme,
                    actionContent = {
                        ChangeTheme()
                    },
                    dismissAction = { viewModel!!.theme.value = localUser.theme!! },
                    confirmAction = { visible ->
                        changeTheme(
                            newTheme = theme.value,
                            onChange = {
                                visible.value = false
                                navigator.navigate(SPLASHSCREEN)
                            }
                        )
                    },
                    bottomDivider = false
                )
            }
        }
    }

    /**
     * Section to change the [localUser]'s email
     */
    @Composable
    @NonRestartableComposable
    private fun ChangeEmail() {
        val focusRequester = remember { FocusRequester() }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
        viewModel!!.newEmail = remember { mutableStateOf("") }
        viewModel!!.newEmailError = remember { mutableStateOf(false) }
        EquinoxTextField(
            modifier = Modifier
                .focusRequester(focusRequester),
            textFieldColors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent
            ),
            value = viewModel!!.newEmail,
            textFieldStyle = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = bodyFontFamily
            ),
            isError = viewModel!!.newEmailError,
            mustBeInLowerCase = true,
            allowsBlankSpaces = false,
            validator = { isEmailValid(it) },
            errorText = Res.string.email_not_valid,
            errorTextStyle = TextStyle(
                fontSize = 14.sp,
                fontFamily = bodyFontFamily
            ),
            placeholder = Res.string.new_email,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Email
            )
        )
    }

    /**
     * Section to change the [localUser]'s password
     */
    @Composable
    @NonRestartableComposable
    private fun ChangePassword() {
        val focusRequester = remember { FocusRequester() }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
        viewModel!!.newPassword = remember { mutableStateOf("") }
        viewModel!!.newPasswordError = remember { mutableStateOf(false) }
        var hiddenPassword by remember { mutableStateOf(true) }
        EquinoxOutlinedTextField(
            modifier = Modifier
                .focusRequester(focusRequester),
            outlinedTextFieldColors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent
            ),
            value = viewModel!!.newPassword,
            outlinedTextFieldStyle = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = bodyFontFamily
            ),
            isError = viewModel!!.newPasswordError,
            allowsBlankSpaces = false,
            trailingIcon = {
                IconButton(
                    onClick = { hiddenPassword = !hiddenPassword }
                ) {
                    Icon(
                        imageVector = if (hiddenPassword)
                            Icons.Default.Visibility
                        else
                            Icons.Default.VisibilityOff,
                        contentDescription = null
                    )
                }
            },
            visualTransformation = if (hiddenPassword)
                PasswordVisualTransformation()
            else
                VisualTransformation.None,
            validator = { isPasswordValid(it) },
            errorText = stringResource(Res.string.password_not_valid),
            errorTextStyle = TextStyle(
                fontSize = 14.sp,
                fontFamily = bodyFontFamily
            ),
            placeholder = stringResource(Res.string.new_password),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            )
        )
    }

    /**
     * Section to change the [localUser]'s language
     */
    @Composable
    @NonRestartableComposable
    private fun ChangeLanguage() {
        Column (
            modifier = Modifier
                .selectableGroup()
        ) {
            LANGUAGES_SUPPORTED.entries.forEach { entry ->
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = viewModel!!.language.value == entry.key,
                        onClick = { viewModel!!.language.value = entry.key }
                    )
                    Text(
                        text = entry.value
                    )
                }
            }
        }
    }

    /**
     * Section to change the [localUser]'s currency
     */
    @Composable
    @NonRestartableComposable
    private fun ChangeCurrency() {
        Column (
            modifier = Modifier
                .selectableGroup()
        ) {
            NeutronCurrency.entries.forEach { currency ->
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = viewModel!!.currency.value == currency,
                        onClick = { viewModel!!.currency.value = currency }
                    )
                    Text(
                        text = currency.name.lowercase().capitalize().replace("_", " ")
                    )
                }
            }
        }
    }

    /**
     * Section to change the [localUser]'s theme
     */
    @Composable
    @NonRestartableComposable
    private fun ChangeTheme() {
        Column (
            modifier = Modifier
                .selectableGroup()
        ) {
            ApplicationTheme.entries.forEach { entry ->
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = viewModel!!.theme.value == entry,
                        onClick = { viewModel!!.theme.value = entry }
                    )
                    Text(
                        text = entry.name
                    )
                }
            }
        }
    }

    /**
     * Section to execute a profile action
     *
     * @param shape The shape for the container
     * @param leadingIcon The representative leading icon
     * @param actionText The representative action text
     * @param actionContent The content to display to execute the action
     * @param dismissAction The action to execute when the action dismissed
     * @param confirmAction The action to execute when the action confirmed
     * @param bottomDivider Whether create the bottom divider
     */
    @Composable
    @NonRestartableComposable
    @Deprecated("USE THE BUILT-ONE FROM EQUINOX")
    // TODO: MORE CUSTOMIZATION IN THE OFFICIAL COMPONENT
    private fun ProfileAction(
        shape: Shape = RoundedCornerShape(
            size = 0.dp
        ),
        leadingIcon: ImageVector,
        actionText: StringResource,
        actionContent: @Composable ColumnScope.() -> Unit,
        dismissAction: (() -> Unit)? = null,
        confirmAction: ProfileScreenViewModel.(MutableState<Boolean>) -> Unit,
        bottomDivider: Boolean = true
    ) {
        ProfileAction(
            shape = shape,
            leadingIcon = leadingIcon,
            actionText = actionText,
            actionContent = actionContent,
            controls = { expanded ->
                ActionControls(
                    expanded = expanded,
                    dismissAction = dismissAction,
                    confirmAction = confirmAction
                )
            },
            bottomDivider = bottomDivider,
        )
    }

    /**
     * The controls action section to manage the [ProfileAction]
     *
     * @param expanded Whether the section is visible
     * @param dismissAction The action to execute when the action dismissed
     * @param confirmAction The action to execute when the action confirmed
     */
    @Composable
    @NonRestartableComposable
    @Deprecated("USE THE BUILT-ONE FROM EQUINOX")
    // TODO: MORE CUSTOMIZATION IN THE OFFICIAL COMPONENT
    private fun ActionControls(
        expanded: MutableState<Boolean>,
        dismissAction: (() -> Unit)?,
        confirmAction: ProfileScreenViewModel.(MutableState<Boolean>) -> Unit,
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ) {
            AnimatedVisibility(
                visible = expanded.value
            ) {
                Row{
                    IconButton(
                        onClick = {
                            dismissAction?.invoke()
                            expanded.value = !expanded.value
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                    IconButton(
                        onClick = {
                            confirmAction.invoke(
                                viewModel!!,
                                expanded
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            AnimatedVisibility(
                visible = !expanded.value
            ) {
                IconButton(
                    onClick = { expanded.value = true }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null
                    )
                }
            }
        }
    }

    /**
     * Section to execute a profile action
     *
     * @param shape The shape for the container
     * @param leadingIcon The representative leading icon
     * @param actionText The representative action text
     * @param actionContent The content to display to execute the action
     * @param bottomDivider Whether create the bottom divider
     */
    @Composable
    @NonRestartableComposable
    @Deprecated("USE THE BUILT-ONE FROM EQUINOX")
    // TODO: MORE CUSTOMIZATION IN THE OFFICIAL COMPONENT
    private fun ProfileAction(
        shape: Shape = RoundedCornerShape(
            size = 0.dp
        ),
        leadingIcon: ImageVector,
        actionText: StringResource,
        actionContent: @Composable ColumnScope.() -> Unit,
        controls: @Composable (MutableState<Boolean>) -> Unit,
        bottomDivider: Boolean = true
    ) {
        Card (
            shape = shape
        ) {
            val expanded = rememberSaveable { mutableStateOf(false) }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null
                )
                Text(
                    modifier = Modifier
                        .padding(
                            start = 10.dp
                        ),
                    text = stringResource(actionText)
                )
                controls.invoke(expanded)
            }
            AnimatedVisibility(
                visible = expanded.value
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    HorizontalDivider()
                    actionContent.invoke(this)
                }
            }
        }
        if(bottomDivider)
            HorizontalDivider()
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
        viewModel!!.profilePic = remember { mutableStateOf(localUser.profilePic ?: "") } // TODO: REMOVE THE ELVIS OPERATION AND USE !!
        viewModel!!.email = remember { mutableStateOf(localUser.email ?: "") } // TODO: REMOVE THE ELVIS OPERATION AND USE !!
        viewModel!!.language = remember { mutableStateOf(localUser.language ?: "") } // TODO: REMOVE THE ELVIS OPERATION AND USE !!
        viewModel!!.currency = remember { mutableStateOf(localUser.currency) } // TODO: REMOVE THE ELVIS OPERATION AND USE !!
        viewModel!!.theme = remember { mutableStateOf(localUser.theme ?: Auto) } // TODO: REMOVE THE ELVIS OPERATION AND USE !!
    }

}