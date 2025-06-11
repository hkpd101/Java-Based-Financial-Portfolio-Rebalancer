package com.portfolio.model;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class Portfolio {
    private List<Stock> stocks = new ArrayList<>();
    private double totalValue;
    
    public void addStock(Stock stock) {
        stocks.add(stock);
        updateTotalValue();
    }
    
    public void updateTotalValue() {
        totalValue = stocks.stream()
                .mapToDouble(Stock::getCurrentValue)
                .sum();
    }
    
    public double getStockAllocation(String symbol) {
        return stocks.stream()
                .filter(s -> s.getSymbol().equals(symbol))
                .findFirst()
                .map(s -> s.getCurrentAllocation(totalValue))
                .orElse(0.0);
    }
} 