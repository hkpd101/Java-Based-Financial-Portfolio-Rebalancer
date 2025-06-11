package com.portfolio.strategy;

import com.portfolio.model.Portfolio;
import java.util.HashMap;
import java.util.Map;

public class EqualWeightStrategy implements RebalancingStrategy {
    @Override
    public Map<String, Double> calculateRebalancingTrades(Portfolio portfolio, double threshold) {
        Map<String, Double> trades = new HashMap<>();
        int numAssets = portfolio.getAssets().size();
        
        if (numAssets == 0) {
            return trades;
        }

        double targetAllocation = 1.0 / numAssets;
        double totalValue = portfolio.getTotalValue();
        
        for (var asset : portfolio.getAssets()) {
            double currentValue = asset.getCurrentValue();
            double targetValue = totalValue * targetAllocation;
            double tradeAmount = targetValue - currentValue;
            
            if (Math.abs(tradeAmount) > threshold * totalValue) {
                trades.put(asset.getSymbol(), tradeAmount);
            }
        }
        
        return trades;
    }

    @Override
    public String getStrategyName() {
        return "Equal Weight Strategy";
    }

    @Override
    public String getStrategyDescription() {
        return "Distributes portfolio value equally among all assets";
    }
} 