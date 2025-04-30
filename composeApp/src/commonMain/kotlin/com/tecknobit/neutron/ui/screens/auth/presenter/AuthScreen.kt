package com.tecknobit.neutron.ui.screens.auth.presenter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.annotations.ScreenSection
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
import com.tecknobit.equinoxcore.helpers.InputsValidator
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.isEmailValid
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.isNameValid
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.isPasswordValid
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.isServerSecretValid
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.isSurnameValid
import com.tecknobit.neutron.CloseApplicationOnNavBack
import com.tecknobit.neutron.displayFontFamily
import com.tecknobit.neutron.ui.screens.auth.presentation.AuthScreenViewModel
import com.tecknobit.neutron.ui.theme.NeutronTheme
import neutron.composeapp.generated.resources.Res
import neutron.composeapp.generated.resources.app_version
import neutron.composeapp.generated.resources.are_you_new_to_neutron
import neutron.composeapp.generated.resources.email
import neutron.composeapp.generated.resources.email_not_valid
import neutron.composeapp.generated.resources.github
import neutron.composeapp.generated.resources.have_an_account
import neutron.composeapp.generated.resources.hello
import neutron.composeapp.generated.resources.host_address
import neutron.composeapp.generated.resources.host_address_not_valid
import neutron.composeapp.generated.resources.name
import neutron.composeapp.generated.resources.name_not_valid
import neutron.composeapp.generated.resources.password
import neutron.composeapp.generated.resources.password_not_valid
import neutron.composeapp.generated.resources.server_secret
import neutron.composeapp.generated.resources.server_secret_not_valid
import neutron.composeapp.generated.resources.sign_in
import neutron.composeapp.generated.resources.sign_in_btn
import neutron.composeapp.generated.resources.sign_up
import neutron.composeapp.generated.resources.sign_up_btn
import neutron.composeapp.generated.resources.surname
import neutron.composeapp.generated.resources.surname_not_valid
import neutron.composeapp.generated.resources.welcome_back
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * The [AuthScreen] class is used to execute the authentication by the user
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxScreen
 */
class AuthScreen : EquinoxScreen<AuthScreenViewModel>(
    viewModel = AuthScreenViewModel()
) {

    /**
     * Method to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        CloseApplicationOnNavBack()
        NeutronTheme {
            Scaffold(
                snackbarHost = { SnackbarHost(hostState = viewModel.snackbarHostState!!) },
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    HeaderSection()
                    FormSection()
                }
            }
        }
    }

    /**
     * Method to create the header section of the activity
     */
    @Composable
    @ScreenSection
    private fun HeaderSection() {
        Column(
            modifier = Modifier
                .height(125.dp)
                .background(MaterialTheme.colorScheme.primary),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        all = 16.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(
                            if (viewModel.isSignUp.value)
                                Res.string.hello
                            else
                                Res.string.welcome_back
                        ),
                        fontFamily = displayFontFamily,
                        color = Color.White
                    )
                    Text(
                        text = stringResource(
                            if (viewModel.isSignUp.value)
                                Res.string.sign_up
                            else
                                Res.string.sign_in
                        ),
                        fontFamily = displayFontFamily,
                        fontSize = 35.sp,
                        color = Color.White
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.End
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val uriHandler = LocalUriHandler.current
                        IconButton(
                            onClick = {
                                uriHandler.openUri(
                                    uri = "https://github.com/N7ghtm4r3/Neutron-Clients"
                                )
                            }
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.github),
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                        Text(
                            text = "v. ${stringResource(Res.string.app_version)}",
                            fontFamily = displayFontFamily,
                            fontSize = 12.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }

    /**
     * Method to create the form where the user can fill it with his credentials
     */
    @Composable
    @ScreenSection
    private fun FormSection() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                )
                EquinoxOutlinedTextField(
                    value = viewModel.host,
                    label = stringResource(Res.string.host_address),
                    keyboardOptions = keyboardOptions,
                    errorText = stringResource(Res.string.host_address_not_valid),
                    isError = viewModel.hostError,
                    validator = { InputsValidator.isHostValid(it) }
                )
                AnimatedVisibility(
                    visible = viewModel.isSignUp.value
                ) {
                    Column (
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        EquinoxOutlinedTextField(
                            value = viewModel.serverSecret,
                            label = stringResource(Res.string.server_secret),
                            keyboardOptions = keyboardOptions,
                            errorText = stringResource(Res.string.server_secret_not_valid),
                            isError = viewModel.serverSecretError,
                            validator = { isServerSecretValid(it) }
                        )
                        EquinoxOutlinedTextField(
                            value = viewModel.name,
                            label = stringResource(Res.string.name),
                            keyboardOptions = keyboardOptions,
                            errorText = stringResource(Res.string.name_not_valid),
                            isError = viewModel.nameError,
                            validator = { isNameValid(it) }
                        )
                        EquinoxOutlinedTextField(
                            value = viewModel.surname,
                            label = stringResource(Res.string.surname),
                            keyboardOptions = keyboardOptions,
                            errorText = stringResource(Res.string.surname_not_valid),
                            isError = viewModel.surnameError,
                            validator = { isSurnameValid(it) }
                        )
                    }
                }
                EquinoxOutlinedTextField(
                    value = viewModel.email,
                    label = stringResource(Res.string.email),
                    mustBeInLowerCase = true,
                    allowsBlankSpaces = false,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Email
                    ),
                    errorText = stringResource(Res.string.email_not_valid),
                    isError = viewModel.emailError,
                    validator = { isEmailValid(it) }
                )
                var hiddenPassword by remember { mutableStateOf(true) }
                EquinoxOutlinedTextField(
                    value = viewModel.password,
                    label = stringResource(Res.string.password),
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
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    errorText = stringResource(Res.string.password_not_valid),
                    isError = viewModel.passwordError,
                    validator = { isPasswordValid(it) }
                )
                val softwareKeyboardController = LocalSoftwareKeyboardController.current
                Button(
                    modifier = Modifier
                        .padding(
                            top = 10.dp
                        )
                        .height(
                            60.dp
                        )
                        .width(300.dp),
                    shape = RoundedCornerShape(
                        size = 10.dp
                    ),
                    onClick = {
                        softwareKeyboardController?.hide()
                        viewModel.auth()
                    }
                ) {
                    Text(
                        text = stringResource(
                            if (viewModel.isSignUp.value)
                                Res.string.sign_up_btn
                            else
                                Res.string.sign_in_btn
                        )
                    )
                }
                Row (
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        text = stringResource(
                            if (viewModel.isSignUp.value)
                                Res.string.have_an_account
                            else
                                Res.string.are_you_new_to_neutron
                        ),
                        fontSize = 14.sp
                    )
                    Text(
                        modifier = Modifier
                            .clickable { viewModel.isSignUp.value = !viewModel.isSignUp.value },
                        text = stringResource(
                            if (viewModel.isSignUp.value)
                                Res.string.sign_in
                            else
                                Res.string.sign_up
                        ),
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
        viewModel.isSignUp = remember { mutableStateOf(false) }
        viewModel.host = remember { mutableStateOf("") }
        viewModel.hostError = remember { mutableStateOf(false) }
        viewModel.serverSecret = remember { mutableStateOf("") }
        viewModel.serverSecretError = remember { mutableStateOf(false) }
        viewModel.name = remember { mutableStateOf("") }
        viewModel.nameError = remember { mutableStateOf(false) }
        viewModel.surname = remember { mutableStateOf("") }
        viewModel.surnameError = remember { mutableStateOf(false) }
        viewModel.email = remember { mutableStateOf("") }
        viewModel.emailError = remember { mutableStateOf(false) }
        viewModel.password = remember { mutableStateOf("") }
        viewModel.passwordError = remember { mutableStateOf(false) }
    }

}