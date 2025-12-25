package com.example.broadcast.service

import org.jsoup.Jsoup
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class GoldPriceAssnService(@Value("\${assnprice.web-url}") private val URL: String) {

    fun getGoldPrices(): Map<String, String> {
        val doc =
                Jsoup.connect(URL)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                        .timeout(10_000)
                        .get()

        val sellPrice = doc.select("#DetailPlace_uc_goldprices1_lblBLSell").text()
        val buyPrice = doc.select("#DetailPlace_uc_goldprices1_lblBLBuy").text()
        val updateTime = doc.select("#DetailPlace_uc_goldprices1_lblAsTime").text()

        return mapOf(
                "Sell Price" to sellPrice,
                "Buy Price" to buyPrice,
                "Updated Time" to updateTime
        )
    }
}
