package com.xeladevmobile.core.network.model

data class NetworkDetailedResponse(
    val prediction: String,
    val percents: Map<String, Float>,
)
