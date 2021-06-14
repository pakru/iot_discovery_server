package com.skyshade.iot.discovery.controllers

import com.skyshade.iot.discovery.configuration.ControllersCacheConfig
import org.json.JSONObject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient


@ExtendWith(SpringExtension::class)
@WebFluxTest(controllers = [DiscoveryController::class])
@Import(ControllersCacheConfig::class)
internal class DiscoveryControllerTest (@Autowired val webClient: WebTestClient) {

    @Test
    fun helloTest() {
        webClient.get().uri("/")
            .exchange()
            .expectStatus().is2xxSuccessful
    }

    @Test
    fun postControllerTest() {
        val json = JSONObject()
            .put("name", "new controller")
            .put("macAddress", "00:11:22:33:44:55")

        webClient.post().uri("/discovery")
            .bodyValue(json.toString())
            .header("content-type", MediaType.APPLICATION_JSON_VALUE)
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("name").isEqualTo("new controller")
            .jsonPath("macAddress").isEqualTo("00:11:22:33:44:55")
            .jsonPath("discoveredAt").isNotEmpty
            .jsonPath("lastSeenAt").isNotEmpty
    }

    @Test
    fun multipleDiscoveryControllerTest() {
        val json = JSONObject()
            .put("name", "new controller")
            .put("macAddress", "00:11:22:33:44:55")

        webClient.post().uri("/discovery")
            .bodyValue(json.toString())
            .header("content-type", MediaType.APPLICATION_JSON_VALUE)
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("name").isEqualTo("new controller")
            .jsonPath("macAddress").isEqualTo("00:11:22:33:44:55")
            .jsonPath("discoveredAt").isNotEmpty
            .jsonPath("lastSeenAt").isNotEmpty
            .jsonPath("id").isNotEmpty


    }

    @Test
    fun getControllersTest() {
        val jsonController1 = JSONObject()
            .put("name", "controller1")
            .put("macAddress", "11:11:22:33:44:55")
        val jsonController2 = JSONObject()
            .put("name", "controller2")
            .put("macAddress", "22:11:22:33:44:55")

        webClient.post().uri("/discovery")
            .bodyValue(jsonController1.toString())
            .header("content-type", MediaType.APPLICATION_JSON_VALUE)
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectHeader().contentType(MediaType.APPLICATION_JSON)

        webClient.post().uri("/discovery")
            .bodyValue(jsonController2.toString())
            .header("content-type", MediaType.APPLICATION_JSON_VALUE)
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectHeader().contentType(MediaType.APPLICATION_JSON)

        webClient.get().uri("/controllers")
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("11:11:22:33:44:55").exists()
            .jsonPath("22:11:22:33:44:55").exists()

    }

}