package com.xeladevmobile.medicalassistant.feature.records

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.xeladevmobile.medicalassistant.core.designsystem.theme.LocalTintTheme
import com.xeladevmobile.medicalassistant.core.designsystem.theme.MedicalTheme
import com.xeladevmobile.medicalassistant.core.ui.DevicePreviews

@Composable
internal fun RecordsScreenRoute(
    modifier: Modifier = Modifier,
    onElementClicked: (String) -> Unit,
    viewModel: RecordsViewModel = hiltViewModel(),
) {
    RecordsScreen(
        modifier = modifier,
        onElementClicked = onElementClicked,
    )
}

@Composable
internal fun RecordsScreen(
    modifier: Modifier = Modifier,
    onElementClicked: (String) -> Unit,
) {
    Box(modifier = modifier) {
        Column {
            EmptyRecords()
        }

    }
}

@Composable
internal fun EmptyRecords(modifier: Modifier = Modifier) {
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
            painter = painterResource(id = R.drawable.img_empty_bookmarks),
            colorFilter = if (iconTint != null) ColorFilter.tint(iconTint) else null,
            contentDescription = null,
        )

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "No hay registros",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Los registros de tus pacientes aparecerán aquí",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Preview
@Composable
private fun EmptyStatePreview() {
    MedicalTheme {
        EmptyRecords()
    }
}

@DevicePreviews
@Composable
internal fun RecordsScreenPreview() {
    MedicalTheme {
        RecordsScreen(
            onElementClicked = { },
        )
    }
}