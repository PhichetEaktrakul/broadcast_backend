package com.example.broadcast.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient

@RestController
@RequestMapping("/api/silver-price")
class SilverPriceController(
        private val webClient: WebClient,
        @Value("\${silver.api-key}") private val apiKey: String,
        @Value("\${silver.api-url}") private val apiUrl: String
) {

    // ================= Get latest silver price =================
    @GetMapping("/latest")
    fun getLatestSilverPrice(): ResponseEntity<Any> {
        val response =
                webClient
                        .get()
                        .uri(apiUrl)
                        .header("x-history-key", apiKey)
                        .retrieve()
                        .bodyToMono(Any::class.java)
                        .block()

        return ResponseEntity.ok(response ?: mapOf("error" to "No data returned"))
    }
}
