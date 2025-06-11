package com.portfolio.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stock {
    private String symbol;
    private String name;
    private double currentPrice;
    private int quantity;
    private double targetAllocation; // Percentage of total portfolio value
    
    public double getCurrentValue() {
        return currentPrice * quantity;
    }
    
    public double getCurrentAllocation(double totalPortfolioValue) {
        return (getCurrentValue() / totalPortfolioValue) * 100;
    }
} 