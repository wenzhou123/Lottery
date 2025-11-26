package com.lottery.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DrawResult {
    private Long id;
    private String lotteryCode;
    private String issueNumber;
    private LocalDate drawDate;
    private String redBalls;
    private String blueBalls;
    private BigDecimal salesAmount;
    private BigDecimal jackpotPool;
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLotteryCode() {
        return lotteryCode;
    }

    public void setLotteryCode(String lotteryCode) {
        this.lotteryCode = lotteryCode;
    }

    public String getIssueNumber() {
        return issueNumber;
    }

    public void setIssueNumber(String issueNumber) {
        this.issueNumber = issueNumber;
    }

    public LocalDate getDrawDate() {
        return drawDate;
    }

    public void setDrawDate(LocalDate drawDate) {
        this.drawDate = drawDate;
    }

    public String getRedBalls() {
        return redBalls;
    }

    public void setRedBalls(String redBalls) {
        this.redBalls = redBalls;
    }

    public String getBlueBalls() {
        return blueBalls;
    }

    public void setBlueBalls(String blueBalls) {
        this.blueBalls = blueBalls;
    }

    public BigDecimal getSalesAmount() {
        return salesAmount;
    }

    public void setSalesAmount(BigDecimal salesAmount) {
        this.salesAmount = salesAmount;
    }

    public BigDecimal getJackpotPool() {
        return jackpotPool;
    }

    public void setJackpotPool(BigDecimal jackpotPool) {
        this.jackpotPool = jackpotPool;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
