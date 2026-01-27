package com.example.broadcast.service

import com.example.broadcast.util.PlaywrightManager
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class GoldPriceAssnService(
        private val pwManager: PlaywrightManager,
        @Value("\${assnprice.web-url}") private val URL: String
) {

    fun getGoldPrices(): Map<String, String> {
        val page = pwManager.browser.newPage()
        page.navigate(URL)

        page.waitForSelector("span:has-text(\"ทองคำแท่ง\")")

        val buyPrice = page.textContent("span:has-text(\"รับซื้อ\") + span") ?: ""
        val sellPrice = page.textContent("span:has-text(\"ขายออก\") + span") ?: ""
        val updateTime = page.textContent("span[data-slot=select-value]") ?: ""

        page.close()

        return mapOf("buyPrice" to buyPrice, "sellPrice" to sellPrice, "updateTime" to updateTime)
    }
}
