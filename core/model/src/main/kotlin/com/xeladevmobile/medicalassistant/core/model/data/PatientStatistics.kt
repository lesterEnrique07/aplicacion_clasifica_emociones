package com.xeladevmobile.medicalassistant.core.model.data

data class PatientStatistics(
    val header: String,
    val description: String,
    val value: String,
    val updatedAt: String,
)

val dummyPatientData = listOf(
    PatientStatistics(
        header = "Statistics 1",
        description = "Statistics description with a very large text to check if the text is wrapped correctly",
        value = "A really large and complex explication about the statistics",
        updatedAt = "2021-09-01",
    ),
    PatientStatistics(
        header = "Statistics 2",
        description = "Statistics description with a very large text to check if the text is wrapped correctly",
        value = "A really large and complex explication about the statistics",
        updatedAt = "2021-09-01",
    ),
    PatientStatistics(
        header = "Statistics 3",
        description = "Statistics description with a very large text to check if the text is wrapped correctly",
        value = "A really large and complex explication about the statistics",
        updatedAt = "2021-09-01",
    ),
    PatientStatistics(
        header = "Statistics 4",
        description = "Statistics description with a very large text to check if the text is wrapped correctly",
        value = "A really large and complex explication about the statistics. A really large and complex explication about the statistics" +
                "A really large and complex explication about the statistics. A really large and complex explication about the statistics",
        updatedAt = "2021-09-01",
    ),
    PatientStatistics(
        header = "Statistics 5",
        description = "Statistics description with a very large text to check if the text is wrapped correctly",
        value = "A really large and complex explication about the statistics",
        updatedAt = "2021-09-01",
    ),
    PatientStatistics(
        header = "Statistics 6",
        description = "Statistics description with a very large text to check if the text is wrapped correctly",
        value = "A really large and complex explication about the statistics. A really large and complex explication " +
                "about the statistics. A really large and complex explication about the statistics",
        updatedAt = "2021-09-01",
    ),
)
