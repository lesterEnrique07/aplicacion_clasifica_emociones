package com.xeladevmobile.medicalassistant.feature.records

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xeladevmobile.medicalassistant.core.designsystem.component.MedicalLoadingWheel
import com.xeladevmobile.medicalassistant.core.designsystem.theme.LocalTintTheme
import com.xeladevmobile.medicalassistant.core.designsystem.theme.MedicalTheme
import com.xeladevmobile.medicalassistant.core.model.data.Audio
import com.xeladevmobile.medicalassistant.core.model.data.audiosPreview
import com.xeladevmobile.medicalassistant.core.model.data.groupByCreatedDate
import com.xeladevmobile.medicalassistant.core.ui.DevicePreviews

@Composable
internal fun RecordsScreenRoute(
    modifier: Modifier = Modifier,
    onElementClicked: (Audio) -> Unit,
    viewModel: RecordsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    RecordsScreen(
        modifier = modifier,
        onElementClicked = onElementClicked,
        uiState = uiState,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun RecordsScreen(
    modifier: Modifier = Modifier,
    onElementClicked: (Audio) -> Unit,
    uiState: RecordsUiState,
) {
    when (uiState) {
        RecordsUiState.Empty -> EmptyRecords()
        RecordsUiState.Loading -> MedicalLoadingWheel(contentDesc = "Loading records")
        is RecordsUiState.Success -> {
            val groupedAudio = uiState.audioList.groupByCreatedDate()

            LazyColumn(
                modifier = modifier
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                groupedAudio.forEach { (dateString, audioList) ->
                    stickyHeader {
                        DateStickyHeader(dateString)
                    }
                    items(audioList) { audio ->
                        AudioRecordItem(
                            audio = audio,
                            onClick = { onElementClicked(audio) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DateStickyHeader(
    dateString: String,
    paddingValues: PaddingValues = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
) {
    // Animation scale as before
    val scale = remember { Animatable(0.8f) }
    LaunchedEffect(key1 = true) {
        scale.animateTo(targetValue = 1f, animationSpec = tween(durationMillis = 300))
    }

    // Shape for rounded corners
    val shape = RoundedCornerShape(size = 12.dp)

    // You can choose a color that contrasts more with the items
    val backgroundColor = MaterialTheme.colorScheme.surface
    val textColor = MaterialTheme.colorScheme.onPrimaryContainer

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
            .clip(shape)
            .scale(scale.value),
        color = backgroundColor,
        shadowElevation = 4.dp,
    ) {
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent),
        ) {
            Text(
                text = dateString,
                style = MaterialTheme.typography.titleMedium.copy(color = textColor),
                modifier = Modifier.padding(16.dp),
            )
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
            uiState = RecordsUiState.Success(
                audiosPreview,
            ),
        )
    }
}