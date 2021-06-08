package com.skyshade.iot.discovery.controllers

import com.google.common.cache.Cache
import com.skyshade.iot.discovery.dto.Controller
import com.skyshade.iot.discovery.dto.ControllerMessage
import com.skyshade.iot.discovery.dto.DiscoveryMessage
import inet.ipaddr.MACAddressString
import inet.ipaddr.mac.MACAddress
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@RestController
class DiscoveryController(val controllersCache: Cache<MACAddress, Controller>) {

    val logger: Logger = LoggerFactory.getLogger(javaClass)

    @PostMapping("/discovery",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun discovery(@RequestBody discoveryMessage: DiscoveryMessage): ControllerMessage {
        logger.info("Post controller: $discoveryMessage")

        val controllerMacAddress =
            when (MACAddressString(discoveryMessage.macAddress).isValid) {
                true -> MACAddressString(discoveryMessage.macAddress).address
                false -> throw IllegalArgumentException("Wrong MAC address format: ${discoveryMessage.macAddress}")
            }

        val controller = controllersCache.getIfPresent(controllerMacAddress) ?:
            Controller(UUID.randomUUID(), discoveryMessage.name, controllerMacAddress, ZonedDateTime.now(), ZonedDateTime.now())

        controller.lastSeenAt = ZonedDateTime.now() // update LastSeen date

        controllersCache.put(controllerMacAddress, controller)

        return ControllerMessage(
            controller.id, controller.name, controller.macAddress.toColonDelimitedString(),
            controller.discoveredAt.format(
                DateTimeFormatter.ISO_ZONED_DATE_TIME),
            controller.lastSeenAt.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
        )
    }

    @GetMapping("/controllers", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getDiscoveredControllers(): Map<MACAddress, ControllerMessage> {
        logger.info("Controllers: $controllersCache")
        return controllersCache.asMap().mapValues {
            ControllerMessage(
                it.value.id, it.value.name, it.value.macAddress.toColonDelimitedString(),
                it.value.discoveredAt.format(
                    DateTimeFormatter.ISO_ZONED_DATE_TIME),
                it.value.lastSeenAt.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
            )
        }
    }

    @GetMapping("/")
    suspend fun default(): String {
        return "Hello"
    }

}
