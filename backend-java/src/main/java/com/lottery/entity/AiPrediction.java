package com.lottery.entity;

import java.time.LocalDateTime;

public class AiPrediction {
    private Long id;
    private String lotteryCode;
    private String issueNumber;
    private String predictedNumbers;
    private String cotAnalysis;
    private String modelVersion;
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

    public String getPredictedNumbers() {
        return predictedNumbers;
    }

    public void setPredictedNumbers(String predictedNumbers) {
        this.predictedNumbers = predictedNumbers;
    }

    public String getCotAnalysis() {
        return cotAnalysis;
    }

    public void setCotAnalysis(String cotAnalysis) {
        this.cotAnalysis = cotAnalysis;
    }

    public String getModelVersion() {
        return modelVersion;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
