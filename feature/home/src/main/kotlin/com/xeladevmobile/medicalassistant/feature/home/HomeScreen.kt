package com.xeladevmobile.medicalassistant.feature.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.xeladevmobile.medicalassistant.core.designsystem.theme.MedicalTheme
import com.xeladevmobile.medicalassistant.core.designsystem.R as designSystemR
import com.xeladevmobile.medicalassistant.core.model.data.PatientStatistics
import com.xeladevmobile.medicalassistant.core.model.data.dummyPatientData
import com.xeladevmobile.medicalassistant.core.ui.DevicePreviews

@Composable
internal fun HomeScreenRoute(
    onDashboardClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {

}

@Composable
internal fun HomeScreen(
    onDashboardClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            HomeScreenToolbar(
                modifier = Modifier,
                username = "",
                denomination = "",
                image = "",
            )
        },
        content = {
            HomeScreenContent(
                modifier = Modifier.padding(it),
            )
        },
    )
}

@DevicePreviews
@Composable
internal fun HomeScreenPreview() {
    MedicalTheme {
        HomeScreen(
            onDashboardClick = {},
            modifier = Modifier,
        )
    }
}

@Composable
internal fun HomeScreenToolbar(
    modifier: Modifier = Modifier,
    username: String,
    denomination: String,
    image: String,
) {
    Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth()) {
        Column {
            Text(text = "Hola $username", fontStyle = FontStyle.Normal, fontSize = 24.sp)
            Text(text = denomination, fontStyle = FontStyle.Italic)

        }
        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current).data(data = image).apply(
                    block = fun ImageRequest.Builder.() {
                        crossfade(true)
                        transformations(CircleCropTransformation())
                    },
                ).build(),
                placeholder = painterResource(id = R.drawable.ic_person_placeholder),
            ),
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
        )
    }
}

@DevicePreviews
@Composable
internal fun HomeScreenToolbarPreview() {
    MedicalTheme {
        HomeScreenToolbar(
            modifier = Modifier,
            username = "Jorge",
            denomination = "Doctor",
            image = "",
        )
    }
}

@Composable
internal fun HomeScreenContent(
    modifier: Modifier = Modifier,
    patientStatisticsList: List<PatientStatistics> = listOf(),
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(300.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalItemSpacing = 24.dp,
        modifier = modifier
            .testTag("home:feed"),
    ) {
        items(
            items = patientStatisticsList,
            key = { it.header },
        ) { statistics ->
            // Animate the card visibility
            AnimatedVisibility(
                visible = true, // Control this state as needed
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                StatisticsCard(statistics)
            }
        }
    }
}

@DevicePreviews
@Composable
internal fun HomeScreenContentPreview() {
    MedicalTheme {
        HomeScreenContent(
            modifier = Modifier,
            patientStatisticsList = dummyPatientData,
        )
    }
}

@Composable
internal fun StatisticsCard(statistics: PatientStatistics) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Column(
            modifier = Modifier,
        ) {
            Box {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .alpha(0.5f),
                    contentScale = ContentScale.Crop,
                    painter = painterResource(designSystemR.drawable.ic_placeholder_default),
                    contentDescription = null, // decorative image,
                )
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.Center) {
                    Text(text = statistics.header, style = typography.headlineMedium)
                    Text(text = statistics.description, style = typography.bodyMedium)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Statistics: ${statistics.value}",
                style = typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Updated at ${statistics.updatedAt}",
                style = typography.bodySmall,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            )
        }
    }
}

@DevicePreviews
@Composable
internal fun StatisticsCardPreview() {
    MedicalTheme {
        StatisticsCard(
            statistics = PatientStatistics(
                header = "Statistics",
                description = "Statistics description with a very large text to check if the text is wrapped correctly",
                value = "A really large and complex explication about the statistics",
                updatedAt = "2021-09-01",
            ),
        )
    }
}
