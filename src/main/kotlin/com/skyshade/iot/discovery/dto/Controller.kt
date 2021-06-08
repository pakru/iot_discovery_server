package com.skyshade.iot.discovery.dto

import inet.ipaddr.mac.MACAddress
import java.time.ZonedDateTime
import java.util.*

data class Controller(
    val id : UUID,
    val name: String,
    val macAddress: MACAddress,
    val discoveredAt: ZonedDateTime,
    var lastSeenAt: ZonedDateTime
)
