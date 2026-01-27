package com.example.broadcast.controller

import com.example.broadcast.model.DTO.GoldAssnResponse
import com.example.broadcast.service.GoldPriceAssnService
import java.io.IOException
import org.springframework.http.ResponseEntity
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/gold-assn")
class GoldPriceAssnController(
        private val jdbcTemplate: JdbcTemplate,
        private val goldScraperService: GoldPriceAssnService
) {

    // ================= Scrapping a gold price from association =====================
    @GetMapping("/scrapping")
    fun scrapGoldPrice(): ResponseEntity<Map<String, String>> =
            try {
                val prices = goldScraperService.getGoldPrices()
                ResponseEntity.ok(prices)
            } catch (e: IOException) {
                // println("Error : ${e.message}")
                ResponseEntity.status(500).body(null)
            }

    // ================= Get latest gold price from DB ===============================
    @GetMapping("/latest")
    fun getGoldPriceAssnLatest(): ResponseEntity<GoldAssnResponse?> =
            try {
                val latest =
                        jdbcTemplate.queryForObject(
                                "SELECT TOP 1 * FROM GoldPrices_Assn ORDER BY id DESC"
                        ) { rs, _ ->
                            GoldAssnResponse(
                                    id = rs.getLong("id"),
                                    sellPrice = rs.getString("sell_price"),
                                    buyPrice = rs.getString("buy_price"),
                                    updatedTime = rs.getString("updated_time"),
                                    createdAt = rs.getString("created_at")
                            )
                        }
                ResponseEntity.ok(latest)
            } catch (e: Exception) {
                // println("Error : ${e.message}")
                ResponseEntity.ok(null)
            }

    // ================= Get latest updated_time from DB =============================
    private fun getUpdateLatest(): String? {
        val sql = "SELECT TOP 1 updated_time FROM GoldPrices_Assn ORDER BY id DESC"
        return try {
            jdbcTemplate.queryForObject(sql, String::class.java)
        } catch (e: Exception) {
            null // if no data yet
        }
    }

    // ================= Insert a price record into DB ===============================
    private fun saveGoldPriceAssn(sell: String?, buy: String?, updatedTime: String?) {
        val sql = "INSERT INTO GoldPrices_Assn (sell_price, buy_price, updated_time) VALUES (?,?,?)"
        jdbcTemplate.update(sql, sell, buy, updatedTime)
    }

    // ================= Check every 5 minutes for a new gold price =================
    @Scheduled(fixedRate = 300_000, initialDelay = 0)
    fun scheduledGoldPriceAssn() {
        println("Scheduler running...")
        try {
            val prices = goldScraperService.getGoldPrices()

            val sell = prices["sellPrice"]
            val buy = prices["buyPrice"]
            val updatedTime = prices["updateTime"]

            val lastUpdatedTime = getUpdateLatest()

            if (lastUpdatedTime == null || updatedTime != lastUpdatedTime) {
                saveGoldPriceAssn(sell, buy, updatedTime)
                println("New gold price saved")
            } else {
                println("No new update")
            }
        } catch (e: Exception) {
            System.err.println("Error fetching/saving gold prices: ${e.message}")
        }
    }

}
