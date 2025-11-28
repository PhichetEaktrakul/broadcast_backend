package com.example.broadcast.controller

import com.example.broadcast.model.DTO.GoldGcapDTO
import org.springframework.http.ResponseEntity
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/gold-gcap")
class GoldPriceGcapController(private val jdbcTemplate: JdbcTemplate) {

    // ================= Get all gold prices ===================
    @GetMapping
    fun getGoldPriceGcapAll(): ResponseEntity<List<GoldGcapDTO>> =
            try {
                val sql = "SELECT * FROM GoldPrices ORDER BY created_at DESC"
                val list =
                        jdbcTemplate.query(sql) { rs, _ ->
                            GoldGcapDTO(
                                    id = rs.getLong("id"),
                                    gold99_buy = rs.getDouble("gold99_buy"),
                                    gold99_sell = rs.getDouble("gold99_sell"),
                                    old_gold99_buy = rs.getDouble("old_gold99_buy"),
                                    old_gold99_sell = rs.getDouble("old_gold99_sell"),
                                    gold96_buy = rs.getDouble("gold96_buy"),
                                    gold96_sell = rs.getDouble("gold96_sell"),
                                    old_gold96_buy = rs.getDouble("old_gold96_buy"),
                                    old_gold96_sell = rs.getDouble("old_gold96_sell"),
                                    created_at = rs.getString("created_at")
                            )
                        }
                ResponseEntity.ok(list)
            } catch (e: Exception) {
                ResponseEntity.status(500).body(emptyList())
            }

    // ================= Get latest gold price =================
    @GetMapping("/latest")
    fun getGoldPriceGcapLatest(): ResponseEntity<GoldGcapDTO?> =
            try {
                val sql = "SELECT TOP 1 * FROM GoldPrices ORDER BY created_at DESC"
                val latest =
                        jdbcTemplate.queryForObject(sql) { rs, _ ->
                            GoldGcapDTO(
                                    id = rs.getLong("id"),
                                    gold99_buy = rs.getDouble("gold99_buy"),
                                    gold99_sell = rs.getDouble("gold99_sell"),
                                    old_gold99_buy = rs.getDouble("old_gold99_buy"),
                                    old_gold99_sell = rs.getDouble("old_gold99_sell"),
                                    gold96_buy = rs.getDouble("gold96_buy"),
                                    gold96_sell = rs.getDouble("gold96_sell"),
                                    old_gold96_buy = rs.getDouble("old_gold96_buy"),
                                    old_gold96_sell = rs.getDouble("old_gold96_sell"),
                                    created_at = rs.getString("created_at")
                            )
                        }
                ResponseEntity.ok(latest)
            } catch (e: Exception) {
                ResponseEntity.status(500).body(null)
            }

    // ================= Save a new gold price =================
    @PostMapping
    fun saveGoldPriceGcap(@RequestBody dto: GoldGcapDTO): ResponseEntity<String> =
            try {
                val sql =
                        """
                        INSERT INTO GoldPrices
                        (gold99_buy, gold99_sell, old_gold99_buy, old_gold99_sell,
                        gold96_buy, gold96_sell, old_gold96_buy, old_gold96_sell)
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                        """
                val rows =
                        jdbcTemplate.update(
                                sql,
                                dto.gold99_buy,
                                dto.gold99_sell,
                                dto.old_gold99_buy,
                                dto.old_gold99_sell,
                                dto.gold96_buy,
                                dto.gold96_sell,
                                dto.old_gold96_buy,
                                dto.old_gold96_sell
                        )
                if (rows > 0) ResponseEntity.ok("Gold price saved successfully")
                else ResponseEntity.badRequest().body("Failed to save gold price")
            } catch (e: Exception) {
                ResponseEntity.status(500).body("Internal server error")
            }

}
