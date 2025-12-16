package com.example.smartlottery.model

data class Draw(
    val id: Int,
    val lotteryCode: String,
    val issueNumber: String,
    val drawDate: String,
    val redBalls: String,
    val blueBalls: String?,
    val jackpotPool: Long
)
