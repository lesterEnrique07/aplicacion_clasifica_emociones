@file:OptIn(ExperimentalAnimationApi::class)

package com.xeladevmobile.medicalassistant.feature.login

import android.util.Patterns
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xeladevmobile.medicalassistant.core.designsystem.component.MedicalButton
import com.xeladevmobile.medicalassistant.core.designsystem.component.MedicalLoadingWheel
import com.xeladevmobile.medicalassistant.core.designsystem.component.MedicalOutlinedButton
import com.xeladevmobile.medicalassistant.core.designsystem.theme.MedicalTheme

@Composable
private fun LoadingState(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .wrapContentSize(Alignment.Center),
    ) {
        MedicalLoadingWheel(
            modifier = modifier
                .size(100.dp)
                .testTag("login:loading"),
            contentDesc = stringResource(id = R.string.loading),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginRoute(
    modifier: Modifier = Modifier,
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
    windowSizeClass: WindowSizeClass,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LoginScreen(
        modifier = modifier,
        onLoginSuccess = onLoginSuccess,
        onLoginClick = viewModel::doLogin,
        onDismissDialog = viewModel::errorDismissed,
        uiState = uiState,
        windowSizeClass = windowSizeClass,
    )
}

@ExperimentalMaterial3Api
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLoginSuccess: () -> (Unit) = {},
    onLoginClick: (email: String, password: String) -> Unit,
    onDismissDialog: () -> Unit,
    uiState: LoginUiState,
    windowSizeClass: WindowSizeClass,
) {
    val shouldShowImage = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded
    val emailState = remember { mutableStateOf(TextFieldValue()) }
    val passwordState = remember { mutableStateOf(TextFieldValue()) }
    val focusManager = LocalFocusManager.current
    var isLoading by remember { mutableStateOf(false) }

    val passwordVisibility by remember { mutableStateOf(false) }

    val isEmailValid by remember {
        derivedStateOf {
            emailState.value.text.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(
                emailState.value.text,
            ).matches()
        }
    }

    val isPasswordValid by remember {
        derivedStateOf {
            passwordState.value.text.isNotEmpty() && passwordState.value.text.length >= 8
        }
    }

    var errorMessage: String? = null

    when (uiState) {
        is LoginUiState.Error -> {
            errorMessage = uiState.message
            isLoading = false
        }

        LoginUiState.Initial -> {
            errorMessage = null
        }

        is LoginUiState.Success -> {
            onLoginSuccess()
        }

        LoginUiState.Loading -> {
            isLoading = true
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        if (shouldShowImage) {
            Row {
                Image(
                    painter = painterResource(id = R.drawable.login_screen_logo),
                    contentScale = ContentScale.FillBounds,
                    contentDescription = stringResource(id = R.string.login),
                    modifier = Modifier
                        .weight(0.4f)
                        .fillMaxSize()
                        .testTag("login_logo"),
                )
                Box(
                    modifier = Modifier
                        .weight(0.6f)
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .defaultMinSize(minWidth = 400.dp),
                    ) {
                        Text(
                            stringResource(id = R.string.login),
                            fontSize = 28.sp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = modifier
                                .align(Alignment.Start),
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        LoginContent(
                            true,
                            emailState,
                            passwordState,
                            focusManager,
                            isEmailValid,
                            isPasswordValid,
                            passwordVisibility = remember {
                                mutableStateOf(
                                    passwordVisibility,
                                )
                            },
                            isLoading = remember { mutableStateOf(isLoading) },
                            onLoginClick = onLoginClick,
                        )
                    }
                }
            }
        } else {
            Text(
                stringResource(id = R.string.login),
                fontSize = 28.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = modifier
                    .align(Alignment.Start)
                    .padding(start = 16.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier.padding(16.dp),
            ) {
                LoginContent(
                    false,
                    emailState,
                    passwordState,
                    focusManager,
                    isEmailValid,
                    isPasswordValid,
                    passwordVisibility = remember { mutableStateOf(passwordVisibility) },
                    isLoading = remember { mutableStateOf(isLoading) },
                    onLoginClick = onLoginClick,
                )
            }
        }
    }

    AnimatedVisibility(
        visible = isLoading,
        enter = fadeIn(
            // Overwrites the initial value of alpha to 0.4f for fade in, 0 by default
            initialAlpha = 0f,
            animationSpec = tween(durationMillis = 500),
        ),
        exit = fadeOut(
            // Overwrites the default animation with tween
            animationSpec = tween(durationMillis = 500),
        ),
    ) {
        LoadingState(modifier)
    }

    errorMessage?.let {
        var showDialog by remember { mutableStateOf(true) }
        AnimatedVisibility(
            visible = showDialog,
            enter = fadeIn(
                // Overwrites the initial value of alpha to 0.4f for fade in, 0 by default
                initialAlpha = 0f,
                animationSpec = tween(durationMillis = 500),
            ),
            exit = fadeOut(
                // Overwrites the default animation with tween
                animationSpec = tween(durationMillis = 500),
            ),
        ) {
            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                    onDismissDialog()
                },
                title = { Text(stringResource(id = R.string.login_error)) },
                text = { Text(errorMessage) },
                confirmButton = {
                    Button(
                        onClick = {
                            showDialog = false
                            onDismissDialog()
                        },
                    ) {
                        Text(stringResource(id = R.string.ok))
                    }
                },
            )
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun LoginContent(
    isLandscape: Boolean,
    emailState: MutableState<TextFieldValue>,
    passwordState: MutableState<TextFieldValue>,
    focusManager: FocusManager,
    isEmailValid: Boolean,
    isPasswordValid: Boolean,
    passwordVisibility: MutableState<Boolean>,
    isLoading: MutableState<Boolean>,
    onLoginClick: (email: String, password: String) -> Unit,
) {
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        if (isLandscape) {
            Spacer(modifier = Modifier.height(30.dp))
        }

        OutlinedTextField(
            value = emailState.value,
            onValueChange = {
                emailState.value = it
            },
            label = { Text(stringResource(id = R.string.email)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.outline,
            ),
            shape = RoundedCornerShape(10.dp),
            // isError = !isEmailValid,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            modifier = Modifier.compactAndTableDesign(isLandscape, testTag = "email_text_field"),
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = passwordState.value,
            onValueChange = {
                passwordState.value = it
            },
            label = { Text(stringResource(id = R.string.password)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.outline,
            ),
            shape = RoundedCornerShape(10.dp),
            visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            // isError = !isPasswordValid,
            trailingIcon = {
                IconButton(onClick = { passwordVisibility.value = !passwordVisibility.value }) {
                    Icon(
                        imageVector = if (passwordVisibility.value) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = stringResource(id = R.string.toggle_password),
                    )
                }
            },
            modifier = Modifier.compactAndTableDesign(isLandscape, testTag = "password_text_field"),
        )

        Spacer(modifier = Modifier.height(16.dp))

        MedicalButton(
            onClick = {
                if (isEmailValid && isPasswordValid) {
                    focusManager.clearFocus()
                    isLoading.value = true
                    onLoginClick(emailState.value.text, passwordState.value.text)
                }
            },
            enabled = isEmailValid && isPasswordValid,
            modifier = Modifier
                .compactAndTableDesign(isLandscape, testTag = "login_button")
                .defaultMinSize(minHeight = 48.dp),
        ) {
            Text(text = stringResource(id = R.string.login))
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .align(Alignment.End)
                .testTag("forgot_password_button"),
        ) {
            Text(
                text = stringResource(id = R.string.forgot_password),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun LoginWithCloverButton(isLandscape: Boolean) {
    DrawORWithLines(
        modifier = Modifier.compactAndTableDesign(isLandscape, testTag = "or_lines"),
    )

    Spacer(modifier = Modifier.height(20.dp))

    MedicalOutlinedButton(
        onClick = { // TODO
        },
        modifier = Modifier.compactAndTableDesign(isLandscape, testTag = "outlined_button"),
        enabled = true,
        content = {
            Text(
                text = stringResource(id = R.string.login),
                color = MaterialTheme.colorScheme.primaryContainer,
                // make bold text
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            )
        },
    )
}

@Composable
fun DrawORWithLines(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Divider(
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier
                .weight(1f)
                .height(1.dp),
        )
        Text(
            text = stringResource(id = R.string.or),
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 8.dp),
        )
        Divider(
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier
                .weight(1f)
                .height(1.dp),
        )
    }
}

fun Modifier.compactAndTableDesign(isLandscape: Boolean, testTag: String? = null) = with(this) {
    testTag?.let { this.testTag(testTag) }

    if (isLandscape) {
        this
            .width(width = 400.dp)
    } else {
        this
            .fillMaxWidth()
    }
}

@ExperimentalMaterial3Api
@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun LoginContentPreview() {
    BoxWithConstraints {
        MedicalTheme {
            LoginContent(
                isLandscape = false,
                emailState = remember { mutableStateOf(TextFieldValue("")) },
                passwordState = remember { mutableStateOf(TextFieldValue("")) },
                focusManager = LocalFocusManager.current,
                isEmailValid = true,
                isPasswordValid = true,
                passwordVisibility = remember { mutableStateOf(true) },
                isLoading = remember { mutableStateOf(false) },
                onLoginClick = { _, _ -> /* do nothing in preview */ },
            )
        }
    }
}

@ExperimentalMaterial3Api
@Preview(showBackground = true, device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
fun LoginContentTabletPreview() {
    BoxWithConstraints {
        MedicalTheme {
            LoginContent(
                isLandscape = true,
                emailState = remember { mutableStateOf(TextFieldValue("")) },
                passwordState = remember { mutableStateOf(TextFieldValue("")) },
                focusManager = LocalFocusManager.current,
                isEmailValid = true,
                isPasswordValid = true,
                passwordVisibility = remember { mutableStateOf(true) },
                isLoading = remember { mutableStateOf(false) },
                onLoginClick = { _, _ -> /* do nothing in preview */ },
            )
        }
    }
}

private class UiStateProvider : PreviewParameterProvider<LoginUiState> {
    override val values: Sequence<LoginUiState> = sequenceOf(
        LoginUiState.Initial,
        LoginUiState.Loading,
        LoginUiState.Error("Error message to show"),
    )
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@ExperimentalMaterial3Api
@Preview(showBackground = true, device = "spec:width=411dp,height=891dp,orientation=landscape")
@Composable
fun LoginScreenLandscapePreview(
    @PreviewParameter(UiStateProvider::class) uiState: LoginUiState,
) {
    BoxWithConstraints {
        MedicalTheme {
            LoginScreen(
                onLoginClick = { _, _ -> },
                onDismissDialog = { },
                uiState = uiState,
                windowSizeClass = WindowSizeClass.calculateFromSize(
                    DpSize(
                        width = 411.dp,
                        height = 891.dp,
                    ),
                ),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@ExperimentalMaterial3Api
@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun LoginScreenPortraitPreview(
    @PreviewParameter(UiStateProvider::class) uiState: LoginUiState,
) {
    BoxWithConstraints {
        MedicalTheme {
            LoginScreen(
                onLoginClick = { _, _ -> },
                onDismissDialog = { },
                uiState = uiState,
                windowSizeClass = WindowSizeClass.calculateFromSize(
                    DpSize(
                        width = 411.dp,
                        height = 891.dp,
                    ),
                ),
            )
        }
    }
}

@ExperimentalMaterial3Api
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(
    showBackground = true,
    device = "spec:width=1280dp,height=800dp,dpi=240",
)
@Composable
fun LoginScreenTabletPreview() {
    BoxWithConstraints {
        MedicalTheme {
            LoginScreen(
                onLoginClick = { _, _ -> },
                onDismissDialog = { },
                uiState = LoginUiState.Initial,
                windowSizeClass = WindowSizeClass.calculateFromSize(
                    DpSize(
                        width = 1280.dp,
                        height = 800.dp,
                    ),
                ),
            )
        }
    }
}

@Preview(showBackground = true, device = "spec:parent=pixel_5")
@Composable
fun LoadingStatePreview() {
    BoxWithConstraints {
        MedicalTheme {
            LoadingState()
        }
    }
}
