package com.portfolio.model;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class Asset {
    private String symbol;
    private String name;
    private double quantity;
    private double currentPrice;
    private double targetAllocation;
    private AssetType type;

    public double getCurrentValue() {
        return quantity * currentPrice;
    }

    public double getCurrentAllocation(double totalPortfolioValue) {
        return totalPortfolioValue > 0 ? getCurrentValue() / totalPortfolioValue : 0;
    }

    public double getDeviationFromTarget(double totalPortfolioValue) {
        return getCurrentAllocation(totalPortfolioValue) - targetAllocation;
    }
} 