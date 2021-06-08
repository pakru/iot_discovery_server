package com.skyshade.iot.discovery.configuration

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import com.skyshade.iot.discovery.dto.Controller
import inet.ipaddr.mac.MACAddress
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

@Configuration
class ControllersCacheConfig {

    @Bean
    fun controllersCache() : Cache<MACAddress, Controller> {
        return CacheBuilder.newBuilder().expireAfterWrite(2, TimeUnit.MINUTES).build()
    }

}