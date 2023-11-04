package com.xeladevmobile.feature.me

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xeladevmobile.medicalassistant.core.designsystem.theme.LocalTintTheme
import com.xeladevmobile.medicalassistant.core.designsystem.theme.MedicalTheme
import com.xeladevmobile.medicalassistant.core.model.data.DarkThemeConfig
import com.xeladevmobile.medicalassistant.core.model.data.ThemeBrand
import com.xeladevmobile.medicalassistant.core.model.data.UserData
import com.xeladevmobile.medicalassistant.core.model.data.UserType
import com.xeladevmobile.medicalassistant.feature.me.R

@Composable
internal fun ProfileScreenRoute(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ProfileScreen(
        modifier = modifier,
        uiState = uiState,
    )
}

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    uiState: ProfileUiState,
) {
    when (uiState) {
        ProfileUiState.Loading -> {

        }

        is ProfileUiState.Success -> TODO()
    }

    var expanded by remember { mutableStateOf(false) }
    val alpha: Float by animateFloatAsState(if (expanded) 1f else 0.7f, label = "")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                text = "Personal Details",
                fontStyle = FontStyle.Italic,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .padding(16.dp),
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column {
                        Text(text = "Jane Doe", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(text = "Date of Birth: 01/01/1990")
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            Icon(imageVector = Icons.Default.Call, contentDescription = "Call Icon")
                            Text(text = "+1234567890")
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            Icon(imageVector = Icons.Default.Email, contentDescription = "Email Icon")
                            Text(text = "jane.doe@email.com")
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(Color.Gray, shape = MaterialTheme.shapes.medium),
                    ) {
                        // Replace this with an Image composable for the profile picture
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
internal fun LoadingUser(modifier: Modifier = Modifier) {
    val alpha: Float by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = LinearOutSlowInEasing,
        ),
        label = "",
    )

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
            .alpha(alpha)
            .testTag("bookmarks:empty")
            .alpha(alpha), // Apply fade-in
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val iconTint = LocalTintTheme.current.iconTint
        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = painterResource(id = R.drawable.ic_person_placeholder),
            colorFilter = if (iconTint != null) ColorFilter.tint(iconTint) else null,
            contentDescription = null,
        )

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "Loading...",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Preview
@Composable
fun LoadingUser() {
    MedicalTheme {
        LoadingUser()
    }
}

@Preview
@Composable
fun ProfileLoadingPreview() {
    MedicalTheme {
        ProfileScreen(uiState = ProfileUiState.Loading)
    }
}

@Preview
@Composable
fun ProfileSuccessPreview() {
    MedicalTheme {
        ProfileScreen(
            uiState = ProfileUiState.Success(
                UserData(
                    darkThemeConfig = DarkThemeConfig.DARK,
                    shouldHideOnboarding = true,
                    themeBrand = ThemeBrand.ANDROID,
                    useDynamicColor = false,
                    address = "123 Main St",
                    bornDate = "01/01/1990",
                    experience = "10 years",
                    graduationDate = "01/01/2010",
                    name = "Jane Doe",
                    occupation = "Doctor",
                    sex = "Feminine",
                    problemDescription = "",
                    specialty = "Cardiologist",
                    treatmentDate = "",
                    userType = UserType.DOCTOR
                ),
            ),
        )
    }
}
