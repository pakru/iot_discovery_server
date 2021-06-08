package com.skyshade.iot.discovery.dto

import java.util.*

data class ControllerMessage(
    val id: UUID,
    val name: String,
    val macAddress: String,
    val discoveredAt: String,
    val lastSeenAt: String
)
