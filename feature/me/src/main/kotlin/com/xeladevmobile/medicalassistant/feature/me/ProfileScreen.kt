package com.xeladevmobile.medicalassistant.feature.me

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.AssuredWorkload
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.WorkOutline
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
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
            LoadingUser()
        }

        is ProfileUiState.Success -> {
            Box(
                modifier = modifier
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
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(0.5f),
                            ) {
                                Text(text = uiState.user.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AssuredWorkload,
                                        contentDescription = "Graduation Date",
                                    )
                                    Text(text = "Date of Birth: ${uiState.user.bornDate}")
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                ) {
                                    Icon(imageVector = Icons.Default.School, contentDescription = "Graduation Date")
                                    Text(text = "Graduation: ${uiState.user.graduationDate}")
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                ) {
                                    Icon(imageVector = Icons.Default.WorkOutline, contentDescription = "Work Icon")
                                    Text(text = uiState.user.occupation)
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                ) {
                                    Icon(imageVector = Icons.Default.WorkspacePremium, contentDescription = "Work Icon")
                                    Text(text = uiState.user.specialty)
                                }
                            }

                            Image(
                                painter = painterResource(id = R.drawable.ic_person_placeholder),
                                contentDescription = "Picture",
                                modifier = Modifier.size(100.dp),
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                        ) {
                            Text(text = "Experience", fontWeight = FontWeight.Bold, fontSize = 18.sp)

                            Spacer(modifier = Modifier.size(8.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PersonOutline,
                                    contentDescription = "Sex",
                                )
                                Text(text = "Sex: ${uiState.user.sex}")
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Directions,
                                    contentDescription = "Address",
                                )
                                Text(text = "Address: ${uiState.user.address}")
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            ExperienceTextAnimated(
                                years = uiState.user.experience,
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
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
            .testTag("profile:loading")
            .alpha(alpha), // Apply fade-in
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val iconTint = LocalTintTheme.current.iconTint
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
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

@Composable
internal fun ExperienceTextAnimated(
    modifier: Modifier = Modifier,
    years: String,
) {
    var scaleRatio by remember { mutableFloatStateOf(0.5f) }
    // Use animateFloatAsState to perform the animation once
    val scale: Float by animateFloatAsState(
        targetValue = scaleRatio,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing,
        ),
        label = "",
    )

    // This LaunchedEffect will reset the scale back to the initial value after the animation reaches the target value
    LaunchedEffect(key1 = scale) {
        scaleRatio = 1.2f
    }

    Column(
        modifier = modifier
            .testTag("profile:experience")
            .padding(bottom = 16.dp)
            .graphicsLayer {
                // Apply the scale to the graphics layer of the Column
                scaleX = scale
                scaleY = scale
            },
        verticalArrangement = Arrangement.Center,
    ) {
        val iconTint = LocalTintTheme.current.iconTint

        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = years,
            textAlign = TextAlign.Center,
            fontSize = with(LocalDensity.current) { 64.sp * scale },
            color = iconTint ?: MaterialTheme.colorScheme.primary,
        )

        Text(
            text = "YEARS",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingUserPreview() {
    MedicalTheme {
        LoadingUser()
    }
}

@Preview
@Composable
fun ProfilePreview() {
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
                    experience = "10",
                    graduationDate = "01/01/2010",
                    name = "Jane Doe",
                    occupation = "Doctor",
                    sex = "Feminine",
                    problemDescription = "",
                    specialty = "Cardiologist",
                    treatmentDate = "",
                    userType = UserType.DOCTOR,
                ),
            ),
        )
    }
}
