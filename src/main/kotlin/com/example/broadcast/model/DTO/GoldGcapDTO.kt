package com.example.broadcast.model.DTO

data class GoldGcapDTO(
        val id: Long,
        val gold99_buy: Double,
        val gold99_sell: Double,
        val old_gold99_buy: Double,
        val old_gold99_sell: Double,
        val gold96_buy: Double,
        val gold96_sell: Double,
        val old_gold96_buy: Double,
        val old_gold96_sell: Double,
        val created_at: String? = null
)
